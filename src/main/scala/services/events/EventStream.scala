
package services.events

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._

object EventStream {
  /* this is the directory where log files are *atomically* placecd.
    They should not be copied, but log files must be copied to a staging
    directory and then moved into EVEN_FEED directory. Only the new
    files added after spark streaming process starts are considered.
    Older files are considered stale  */
  val EVENT_FEED = "/Users/mahesh/data/affinitas/feeds/events/"

  //These are the events we first filter out to reduct the scope of our searches.
  val EVENT_OF_INTEREST = List(TEST_FINISHED, TEXTMESSAGE_SENT_NEW, TEXTMESSAGE_SENT_REPLY)

  var ssc: StreamingContext = null
  var sql: SparkSession = null

  //first level stream. every record is converted into a EventRecord object before filter noise
  lazy val eventStream = ssc.textFileStream(EVENT_FEED)
    .map(EventRecordParser.parseRecord)
    .filter(record => record != null && record.event != null && EVENT_OF_INTEREST.contains(record.event))
  //select only test_finished events
  lazy val testFinishStream = eventStream.filter(record => record.event == TEST_FINISHED)
  //select all message stream events
  lazy val messageStream = eventStream.filter(record => record.event == TEXTMESSAGE_SENT_NEW || record.event == TEXTMESSAGE_SENT_REPLY)

  def initialize(ssc0: StreamingContext, sql0: SparkSession) = {
    ssc = ssc0
    sql = sql0
  }

}



