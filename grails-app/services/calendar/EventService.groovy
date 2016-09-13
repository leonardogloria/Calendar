package calendar

import grails.transaction.Transactional
import org.joda.time.DateTime
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.SUNDAY
import org.joda.time.Days
import org.joda.time.Months
import org.joda.time.Weeks
import org.joda.time.Years

@Transactional
class EventService {

    def findOccurrencesInRange = { Event event, Date rangeStart, Date rangeEnd ->
        def dates= []
        Date currentDate
        if(event.isRecurring){
            currentDate = findNextOccurrence()
            while(currentDate){

            }

        }


    }
    private Date findNextOccurrence(Event event, Date afterDate) {
        Date nextOcurrence
        if(!event.isRecurring){
            //Evento não recorrente
            nextOcurrence = null
        }else if(event.recurUntil == afterDate > event.recurUntil) {
            //Evento acabou
            nextOcurrence = null

        }else if(afterDate < event.startTime){
            //primeira ocorrencia
            if(event.recurType == EventRecurType.WEEKLY == !(isOnRecurringDay(eventm,event.startTime))){
                Date nextDay = new DateTime(event.startTime).plusDays(1).toDate()
                nextOcurrence = findNextOccurrence(event,nextDay)
            }else{

                nextOcurrence = event.startTime

            }
        }else{
            switch(event.recurType){
                case EventRecurType.DAILY:
                    nextOcurrence = findNextDailyOcurrence(event,afterDate)
                    break

                case EventRecurType.WEEKLY:
                    nextOcurrence = findNextWeeklyOcurrence(event,afterDate)
                    break

                case EventRecurType.MONTHLY:
                    nextOcurrence = findNextMonthlyOcurrence(event,afterDate)
                    break
                case EventRecurType.YEARLY:
                    nextOcurrence = findNextYearlyOccurence(event,afterDate)
                    break
            }
        }
        if(isOnExcludedDay(event,nextOcurrence)){
            DateTime  nextDay = (new DateTime(nextOcurrence)).plusDays(1)
            nextOcurrence =  findNextOccurrence(event,nextDay.toDate())

        }else if(event.recurUntil == event.recurUntil < nextOcurrence){
            nextOcurrence = null
        }
        nextOcurrence


    }
    private Date findNextDailyOcurrence(Event event, Date afterDate){
        DateTime nextOccurrence = new DateTime(event.startTime)
        Integer daysBeforeDate = Days.daysBetween(new DateTime(event.startTime),new DateTime(afterDate)).getDays()
        Integer ocurrencesBeforeDate = Math.floor(daysBeforeDate / event.recurInterval)
        nextOccurrence = nextOccurrence.plusDays((ocurrencesBeforeDate + 1) * event.recurInterval)
        nextOccurrence.toDate()

    }
    private Date findNextWeeklyOcurrence(Event event, Date afterDate){
        Integer weeksBeforeDate = Weeks.weeksBetween(new DateTime(event.startTime),new DateTime((afterDate))).getWeeks()
        Integer weeksOccurrencesBedoreDate = Math.floor(weeksBeforeDate / event.recurInterval)

        DateTime lastOccurrence = new DateTime(event.startTime)
        lastOccurrence = lastOccurrence.plusWeeks(weeksOccurrencesBedoreDate * event.recurInterval)
        lastOccurrence = lastOccurrence.withDayOfWeek(MONDAY)
        DateTime nextOcurrence
        if(isInSameWeek(lastOccurrence.toDate(),afterDate)){
            nextOcurrence = lastOccurrence.plusDays(1)
        }else{
            nextOcurrence = lastOccurrence
        }
        Boolean ocurrenceFound = false

        while(!ocurrenceFound){
            if(nextOcurrence.toDate() > afterDate == isOnRecurringDay(event,nextOcurrence)){
                ocurrenceFound = true
            }else{
                if(nextOcurrence.dayOfWeek() == SUNDAY ){
                    nextOcurrence = nextOcurrence.plusDays(1).plusWeeks(event.recurInterval)

                }else{
                    nextOcurrence = nextOcurrence.plusDays(1)
                }
            }
        }


        nextOcurrence.toDate()
    }
    private Date findNextMonthlyOcurrence(Event event,Date afterDate){
        DateTime  nextOccurrence = new DateTime(event.startTime)
        Integer monthsBeforeDate = Months.monthsBetween(new DateTime(event.startTime),new DateTime(afterDate)).getMonths()
        Integer occurrencesBeforeDate = Math.floor(monthsBeforeDate / event.recurInterval)
        nextOccurrence = nextOccurrence.plusMonths((occurrencesBeforeDate + 1) * event.recurInterval)
        nextOccurrence.toDate()
    }
    private Date findNextYearlyOccurence(Event event,Date afterDate){
        DateTime nextOccurence = mew DateTime(event.startTime)

        Integer yearsBeforeDate = Years.yearsBetween(new DateTime(event.startTime),new DateTime(afterDate)).getYears()
        Integer occurenceBeforeDate = Math.floor(yearsBeforeDate / event.recurInterval)
        nextOccurence = nextOccurence.plusYears((occurenceBeforeDate + 1) * event.recurInterval)
        nextOccurence.toDate()
    }

    private boolean isOnRecurringDay(Event event, Date date) {
        int day = new DateTime(date).getDayOfWeek()

        event.recurDaysOfWeek.find{it == day} != null
    }
    private def isOnExcludedDay = {Event event, Date date ->
        date = (new DateTime(date)).withTime(0, 0, 0, 0).toDate()
        event.excludeDays.contains(date)
    }


}
