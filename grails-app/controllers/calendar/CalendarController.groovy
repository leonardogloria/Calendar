package calendar

import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.Instant

import static org.joda.time.DateTimeConstants.FRIDAY
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY

class CalendarController {
    def eventService

    def index() {

    }
    def list = {
        println params as JSON
        def now = new DateTime()
        def startRange = now.minusMonths(1).toDate()

        def endRange = now.plusMonths(2).toDate()

        //def(startRange,endRange) = [now.toS,plusMonth.millis].collect{new Instant(it *1000L).toDate()}
        def criteria = Event.createCriteria()
        def events = criteria.list {
            or {
                and {
                    eq("isRecurring", false)
                    between("startTime", startRange, endRange)
                }
                and{
                    eq("isRecurring",true)
                    or{
                        isNull("recurUntil")
                        ge("recurUntil",startRange)
                    }
                }
            }
        }
        eventService.findOccurrencesInRange(events[0],startRange,endRange)

        println events as JSON

        println Event.list() as JSON

        render Event.list() as JSON


    }
}
