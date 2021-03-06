package calendar

import org.joda.time.DateTime
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.TUESDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY
import static org.joda.time.DateTimeConstants.FRIDAY

class TesteController {

    def index() {
        def now = new DateTime()
        def tomorrow = now.plusDays(1)

        def event = new Event(title: "Repeating MWF Event").with {
            startTime = now.plusDays(1).toDate()
            endTime = now.plusDays(1).plusHours(1).toDate()
            location = "Regular Location"
            recurType = EventRecurType.WEEKLY
            [FRIDAY]*.toInteger().each {addToRecurDaysOfWeek(it)}
            //recurUntil = now.plusMonths(60).toDate()
            addToExcludeDays(now.withDayOfWeek(MONDAY).plusWeeks(1).toDate())
            isRecurring=true
            save(flush:true)
        }
        def event2 = new Event(title: "Repeating W Event").with {
            startTime = now.plusDays(1).toDate()
            endTime = now.plusDays(1).plusHours(1).toDate()
            location = "Regular Location"
            recurType = EventRecurType.WEEKLY
            [WEDNESDAY]*.toInteger().each {addToRecurDaysOfWeek(it)}
            //recurUntil = now.plusMonths(60).toDate()
            addToExcludeDays(now.withDayOfWeek(MONDAY).plusWeeks(1).toDate())
            isRecurring=true
            save(flush:true)
        }
        /*
        def event2 = new Event(title: event.title ).with {
            sourceEvent = event
            startTime = event.startTime
            endTime = event.endTime
            location = "New Location"
            isRecurring = false
            save()
        }
        def event3 = new Event(title: "Just Normal Event").with {
            startTime = tomorrow.toDate()
            endTime= tomorrow.plusDays(1).toDate()
            isRecurring = false
            save()
        }
        */

    }
    def count(){
        println Event.count()
    }

}
