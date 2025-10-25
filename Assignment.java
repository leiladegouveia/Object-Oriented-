package adam.assignment;

import adam.Messages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;

import static adam.ADAM.conf;
import static adam.ApplicationConfigurationConstants.*;


public class Assignment {
    private final ZonedDateTime endDate;

    private final ZonedDateTime actualEndDate;
    private final ZonedDateTime startDate;
    private ZonedDateTime       dateToStartWork;
    private final BigDecimal    hoursRequired;
    private final Duration      duration;
    private final float hoursPerDay;
    private final String name;
    private final String course;
    private final String points;

    private final String comment;

    public Assignment(ZonedDateTime startDate, ZonedDateTime endDate, BigDecimal hoursRequired, String name, String course, String comment, String points) {

        //if it is midnight
        if(endDate.getHour() == 0 && endDate.getMinute() == 0){
            this.actualEndDate      = endDate;
            this.endDate            = endDate.plusDays(1);
        }
        else {
            this.actualEndDate = endDate;
            this.endDate = endDate.plusDays(1);
        }
        if(startDate.getHour() == 0 && startDate.getMinute() == 0){
            this.startDate      = startDate;
        }
        else {
            this.startDate = startDate;
        }

        //this.startDate          = startDate;
        this.hoursRequired      = hoursRequired;
        this.name               = name;

        setDateToStartWork();

        //will always be initialised, just may be null
        this.course        = course;
        this.comment      = comment;
        this.points        = points;

        //setting duration
        duration = Duration.between(dateToStartWork, actualEndDate.plusDays(1));

        //setting hours per day
        float days = duration.toDays();
        BigDecimal hoursPerDay = hoursRequired.divide(BigDecimal.valueOf(days), DECIMAL_PLACES, ROUNDING_MODE);
        this.hoursPerDay = hoursPerDay.floatValue();
    }

    public ZonedDateTime getDateToStartWork(){
        return dateToStartWork;
    }

    public void setDateToStartWork(){
        if(conf.getNow().isAfter(startDate))
        {
            dateToStartWork = conf.getNow();
        }
        else
            dateToStartWork = startDate;
    }
    public boolean isActiveCurrently(ZonedDateTime currentDate){
        return (currentDate.isBefore(endDate) &&
                (currentDate.isAfter(getDateToStartWork()) || currentDate.isEqual(getDateToStartWork())));
    }

    public String getName() {return name;}

    public String getCourse(){return course;}

    public ZonedDateTime getEndDate() {return endDate;}

    public Duration getDuration(){
        return duration;
    }

    public void printSummary() {

        long days = getDuration().toDays();
        String courseHolder = course;

        if(course == null){
            courseHolder = String.format(Messages.DEFAULT_COURSE.toString());
        }
        System.out.printf(Messages.TIMETABLE_SUMMARY + "\n", name, courseHolder, hoursPerDay, days, actualEndDate);

    }

    public void printComment() {
        if (conf.getShowComments() && !(comment == null)) {
            System.out.printf(Messages.APPLICATION_COMMENT + "\n", comment);
        }
    }
    public float printDaily(ZonedDateTime currentDate){
        if(isActiveCurrently(currentDate)){

            String courseHolder = course;

            if(course == null){
                courseHolder = String.format(Messages.DEFAULT_COURSE.toString());
            }

            System.out.printf(Messages.HOURS_ON + "\n", hoursPerDay, name, courseHolder);
            return hoursPerDay;
        }
        return 0;
    }
}