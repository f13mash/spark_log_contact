package services.users

import java.io.File
import java.nio.charset.Charset
import java.sql.Timestamp

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.streaming.StreamingContext
import services.events.EventStream
import services.Util

/**
  * Created by mahesh on 17/08/16.
  */
case class User(userId: String, testFinishTime: Timestamp, nickname: String, gender: String)

object User {
  val DELIMITER = ','
  val USER_FEED = "/Users/mahesh/data/affinitas/feeds/users/"
  val USER_DATA = "/Users/mahesh/data/affinitas/tables/users/"

  var ssc: StreamingContext = null
  var sql: SparkSession = null


  lazy val usersFeedDF = sql.read
    .format("com.databricks.spark.csv")
    .option("header", false)
    .schema(StructType(Array(
      StructField("userId", StringType, true),
      StructField("nickname", StringType, true),
      StructField("gender", StringType, true)
    )
    )).load(User.USER_FEED)

  //EventStream.testFinishStream.print()
  lazy val usersMap = usersFeedDF.rdd.map(record => (record.getString(0), (record.getString(1), record.getString(2))))


  def initialize(sscO: StreamingContext, sqlO: SparkSession) = {
    ssc = sscO
    sql = sqlO

    new File(USER_FEED).mkdirs()
    new File(USER_DATA).mkdirs()

    EventStream.testFinishStream.foreachRDD( {
      rdd => {
        val testFinishMap = rdd.map(record => (record.userId, record.timestamp))
        val userData = testFinishMap.join(usersMap)
          .map(record => Array(record._1, record._2._1, record._2._2._1, record._2._2._2))
          .collect()
        Util.writeCsvToDir(userData, DELIMITER.toString, USER_DATA)
      }
    })
  }
}