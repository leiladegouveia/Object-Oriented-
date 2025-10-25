package adam.timetable;

import adam.Messages;
import adam.assignment.Assignment;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static adam.ADAM.conf;

public class DailyTimetable {
    public static List<Assignment> assignmentList;
    public static ZonedDateTime startDate;
    public static ZonedDateTime endDate;
    public static Duration daysInTimetable;
    public DailyTimetable(List<Assignment> assignmentList)
    {
        DailyTimetable.assignmentList = assignmentList;

        //only carry out if list is not empty
        if(!assignmentList.isEmpty()) {
            setStartDate();
            setEndDate();
            daysInTimetable = Duration.between(startDate, endDate);
        }


    }

    private void setStartDate(){

        startDate = assignmentList.get(0).getEndDate();
        //getting end date of first assignment, which will definitely be after start of timetable

        for(Assignment assignment : assignmentList){
            if(assignment.getDateToStartWork().isBefore(startDate)){
                startDate = assignment.getDateToStartWork();
            }
        }
    }

    private void setEndDate(){
        endDate = conf.getNow();

        for(Assignment assignment : assignmentList){
            if(assignment.getEndDate().isAfter(endDate)){
                endDate = assignment.getEndDate();
            }
        }
    }

    public void printTimetable(){

        for(Assignment assignment : assignmentList){
            assignment.printComment();
        }

        for (int i = 0; i < daysInTimetable.toDays(); i++) {

            float totalHours = 0;
            ZonedDateTime currentDate = startDate.plusDays(i);
            System.out.println(formatDate(currentDate));

            for(Assignment assignment : assignmentList){
                totalHours = totalHours + assignment.printDaily(currentDate);
            }
            System.out.println(Messages.TOTAL_TOP_LINE);
            System.out.printf(Messages.DAILY_TOTAL + "!!\n", totalHours);
            System.out.println(Messages.TOTAL_BOTTOM_LINE);


        }
    }

    private String formatDate(ZonedDateTime date){
        return String.format("!%s, %s %s, %s", capitalise(date.getDayOfWeek().toString()),
                capitalise(date.getMonth().toString()), date.getDayOfMonth(), date.getYear());
    }

    private String capitalise(String string){
        return (string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());
    }
}
