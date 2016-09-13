package calendar

import org.joda.time.DateTime
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY
import static org.joda.time.DateTimeConstants.FRIDAY

class TesteController {

    def index() {
        def now = new DateTime()
        def tomorrow = now.plusDays(1)

        def event = new Event(title: "Repeating MWF Event").with {
            startTime = now.toDate()
            endTime = now.plusHours(1).toDate()
            location = "Regular Location"
            recurType = EventRecurType.WEEKLY
            [MONDAY,WEDNESDAY,FRIDAY]*.toInteger().each {addToRecurDaysOfWeek(it)}
            addToExcludeDays(now.withDayOfWeek(MONDAY).plusWeeks(1).toDate())
            isRecurring=true
            save(flush:true)
        }
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
            endTime= tomorrow.plusMinutes(30).toDate()
            isRecurring = false
            save()
        }

    }
    def cout(){
        println Event.count()
    }

}
