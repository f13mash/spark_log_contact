package services.events

import java.sql.Timestamp

import org.joda.time.format.DateTimeFormat

/**
  * Created by mahesh on 17/08/16.
  */
object EventRecordParser {
  val DELIMITER = ','
  val COLUMNS = 5
  val dateFormatter  = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")


  def parseRecord(record: String): EventRecord = {
    val fields = record.split(DELIMITER)
    if(fields.length == COLUMNS) {
      return EventRecord(
        parseTimestamp(fields(0).trim),
        EventTypes.getEvent(Integer.parseInt(fields(1).trim)),
        fields(2).trim,
        fields(3).trim,
        Integer.parseInt(fields(4).trim)
      )
    }
    else return null
  }

  def parseTimestamp(str: String) = {
    new Timestamp(dateFormatter.parseMillis(str))
  }
}
