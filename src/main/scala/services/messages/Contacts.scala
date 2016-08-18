package services.messages

import java.io.File
import java.sql.Timestamp

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import services.Util
import services.events.EventStream
import com.redislabs.provider.redis._

/**
  * Created by mahesh on 18/08/16.
  */
case class Contacts(userId: String, userId2: String, establishedTime: Timestamp)

object Contacts {
  val DELIMITER = ','
  val CONTACTS_DATA = "/Users/mahesh/data/affinitas/tables/contacts/"

  var ssc: StreamingContext = null
  var sql: SparkSession = null
  var sc: SparkContext = null

  def initialize(sscO: StreamingContext, sqlO: SparkSession) = {
    ssc = sscO
    sql = sqlO
    sc = sql.sparkContext

    new File(CONTACTS_DATA).mkdirs()

    EventStream.messageStream.foreachRDD(rdd => {
      val redisSetKeys = rdd.map(message => ("contacts:"+message.userId+"_"+message.recepientUserId, "contacts:"+message.recepientUserId+"_"+message.userId, message))

      val redisLookupKeys = redisSetKeys.flatMap(obj => Array(obj._1, obj._2)).collect()

      val listedKeys = sql.sparkContext.fromRedisKV(redisLookupKeys).collect().map(kv => kv._1)

      val redisUpdate = redisSetKeys.filter(kv => !listedKeys.contains(kv._1))

      //send only the updates
      sc.toRedisKV(redisUpdate.map(kv => (kv._1, kv._3.timestamp.toString)))

      //now reverse check in listedKeys, if due to the update sent to redis we have completed the 2-way message
      val contactEstablished = redisUpdate.filter(kv => listedKeys.contains(kv._2))
        .map(kv => Contacts(kv._3.userId, kv._3.recepientUserId, kv._3.timestamp))

      Util.writeCsvToDir(contactEstablished.map(contact => Array(contact.userId, contact.userId2, contact.establishedTime)).collect(),
        DELIMITER.toString, CONTACTS_DATA)
    })

  }

}
