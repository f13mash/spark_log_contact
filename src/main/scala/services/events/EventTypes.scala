package services.events

/**
  * Created by mahesh on 17/08/16.
  */
@SerialVersionUID(100L)
class EventTypes(val code: Integer) extends Serializable

case object TEST_FINISHED extends EventTypes(code = 111)
case object TEXTMESSAGE_SENT_NEW extends EventTypes(code = 401)
case object TEXTMESSAGE_SENT_REPLY extends EventTypes(code = 416)

object EventTypes {
  def getEvent(code: Integer) = code match {
    case TEST_FINISHED.code => TEST_FINISHED
    case TEXTMESSAGE_SENT_NEW.code => TEXTMESSAGE_SENT_NEW
    case TEXTMESSAGE_SENT_REPLY.code => TEXTMESSAGE_SENT_REPLY
    case _ => null  //no match
  }
}
