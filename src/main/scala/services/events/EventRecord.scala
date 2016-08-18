package services.events

import java.sql.Timestamp

/**
  * Created by mahesh on 17/08/16.
  */
case class EventRecord(timestamp: Timestamp, event: EventTypes, userId: String, recepientUserId: String, siteId: Integer)

