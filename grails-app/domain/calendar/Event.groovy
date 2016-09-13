package calendar

class Event {
    String title
    String location
    String description
    Date startTime
    Date endTime

    //Recurring Options
    Boolean isRecurring = false
    EventRecurType recurType
    Integer recurInterval = 1

    Date recurUntil
    Integer recurCount
    Event sourceEvent


    static hasMany = [recurDaysOfWeek: Integer, excludeDays: Date]
    static transients = ['durationMinutes']
    static constraints = {
        title(nullable: false, blank: false)
        location(nullable: true, blank: true)
        description(nullable: true, blank: true)
        recurType(nullable: true)
        recurInterval(nullable: true)
        recurUntil(nullable: true)
        recurCount(nullable: true)
        startTime(nullable: false)
        excludeDays(nullable: true)
        sourceEvent(nullable: true)
        startTime(required: true, nullable: false)
        endTime(required: true, nullable: false, validator: {val, obj ->; val > obj.startTime} )
        recurDaysOfWeek(validator: {val, obj ->;
            if (obj.recurType == EventRecurType.WEEKLY && !val) {return 'null'}
        })

    }

    public int getDurationMinutes() {
        use(groovy.time.TimeCategory) {
            def duration = startTime - endTime

            duration.minutes
        }
    }

}



public enum EventRecurType{
    DAILY('Daily'),
    WEEKLY('Weekly'),
    MONTHLY('Monthly'),
    YEARLY('Yearly')

    String name

    EventRecurType(String name){
        this.name = name
    }


}
