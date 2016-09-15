package calendar

import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.Instant

import java.text.SimpleDateFormat

import static org.joda.time.DateTimeConstants.FRIDAY
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY

class CalendarController {
    def eventService

    def index() {

    }
    def list = {
        def now = new DateTime()
        //def (startRange, endRange) = [params.long('start'), params.long('end')].collect { new Instant(it  * 1000L).toDate() }

        println params as JSON
        def startRange = now.minusDays(13).toDate()

        def endRange = now.plusDays(13).toDate()

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
        def displayDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        def eventList = []
        events.each {event ->
            def dates = eventService.findOccurrencesInRange(event,startRange,endRange)
            dates.each { date ->
                DateTime startTime = new DateTime(date)
                DateTime endTime = startTime.plusMinutes(event.durationMinutes)

                /*
                    start/end and occurrenceStart/occurrenceEnd are separate because fullCalendar will use the client's local timezone (which may be different than the server's timezone)
                    start/end are used to render the events on the calendar and the occurrenceStart/occurrenceEnd values are passed along to the show popup
                */

                eventList << [
                        id: event.id,
                        title: event.title,
                        allDay: false,
                        start: displayDateFormatter.format(startTime.toDate()),
                        end: displayDateFormatter.format(endTime.toDate()),
                        occurrenceStart: startTime.toInstant().millis,
                        occurrenceEnd: endTime.toInstant().millis
                ]
            }


        }


        //println Event.list() as JSON

        render eventList as JSON



    }
}
