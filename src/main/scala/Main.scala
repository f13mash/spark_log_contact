import java.io.File
import java.nio.charset.Charset
import org.apache.spark.SparkContext

import com.google.inject.Inject
import org.apache.commons.io.FileUtils
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType, TimestampType}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import services.events.EventStream
import services.messages.Contacts
import services.users.User
import services.Util
import com.redislabs.provider.redis._
import scala.collection.mutable

object Main {
  def main (args: Array[String]): Unit = {
    val APP_NAME = "Affinitas"
    val APP_MASTER = "local[4]"
    //setup spark conf
    val conf = new SparkConf().setMaster(APP_MASTER).setAppName(APP_NAME)
      // initial redis host - can be any node in cluster mode
      .set("redis.host", "localhost")
      // initial redis port
      .set("redis.port", "6379")

    //setup streaming context
    val ssc = new StreamingContext(conf, Seconds(10))

    val sql = SparkSession.builder()
      .master(APP_MASTER)
      .appName(APP_MASTER)

      .getOrCreate()

    val sc = sql.sparkContext


    EventStream.initialize(ssc, sql)
    User.initialize(ssc, sql)
    Contacts.initialize(ssc, sql)



    ssc.start()
    ssc.awaitTermination()

    conf.set("spark.streaming.stopGracefullyOnShutdown","true")

  }
}
