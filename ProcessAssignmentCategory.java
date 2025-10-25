package adam.semantic_process_input;

import adam.Messages;
import adam.assignment.Assignment;
import adam.syntax_process_input.SplitInput;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static adam.ADAM.conf;
import static adam.syntax_process_input.SplitInput.normaliseCategory;

public class ProcessAssignmentCategory {
    public static final String NAME_ASSIGNMENT = normaliseCategory("name");
    public static final String NAME_COURSE     = normaliseCategory("course");
    public static final String NAME_START      = normaliseCategory("start");
    public static final String NAME_END        = normaliseCategory("end");
    public static final String NAME_HOURS      = normaliseCategory("hours");
    public static final String NAME_COMMENT    = normaliseCategory("comment");
    public static final String NAME_POINTS    = normaliseCategory("points");

    public static final String validEntries = String.format("[%s, %s, %s, %s, %s, %s, %s]",
            NAME_COMMENT,
            NAME_COURSE,
            NAME_END,
            NAME_HOURS,
            NAME_ASSIGNMENT,
            NAME_POINTS,
            NAME_START);

    private ProcessAssignmentCategory() {} // Utility class: do not instantiate

    public static Assignment processCategories(List<SplitInput> splitLines, String fileName) {

        String name = null;
        String course = null;
        ZonedDateTime start = null;
        ZonedDateTime end = null;
        BigDecimal hours = new BigDecimal(0);
        String comment = null;
        String points = null;

        for (SplitInput splitLine : splitLines) {

            if (splitLine != null) {

                String category = splitLine.category();
                String data = splitLine.data();

                if (category.equals(NAME_ASSIGNMENT)) {
                    name = data;
                } else if (category.equals(NAME_COURSE)) {
                    course = data;
                } else if (category.equals(NAME_START)) {
                    start = ZonedDateTime.parse(data);
                } else if (category.equals(NAME_END)) {
                    end = ZonedDateTime.parse(data);
                } else if (category.equals(NAME_HOURS)) {
                    hours = new BigDecimal(data);
                } else if (category.equals(NAME_COMMENT)) {
                    comment = data;
                } else if (category.equals(NAME_POINTS)) {
                    points = data;
                } else {
                    System.out.printf(Messages.UNKNOWN_ASSIGNMENT_COMMAND + "\n", splitLine.category());
                    System.out.printf(Messages.VALID_POSSIBILITIES.toString(), validEntries + "\n");
                }
            }


        }

        return checkMissing(start, end, hours, name, course, comment, points, fileName);
    }

    private static Assignment checkMissing(ZonedDateTime start,
                                           ZonedDateTime end, BigDecimal hours,
                                           String name, String course,
                                           String comment, String points, String fileName){

        //if any required field are missing will not instantiate a new assignment and will just return null
        String missing = " ";
        if (name == null) {
            missing = missing + NAME_ASSIGNMENT + " ";
        }
        if (hours.equals(new BigDecimal(0))) {
            missing = missing + NAME_HOURS + " ";
        }
        if (start == null) {
            missing = missing + NAME_START + " ";
        }
        if (end == null) {
            missing = missing + NAME_END + " ";
        }

        //if assignment is not complete
        if (!missing.equals(" ")) {
            if(conf.getIgnoreIncompleteAssignments())
            {
                System.out.printf(Messages.MISSING_ASSIGNMENT_ITEM + "\n", fileName, missing);
                return null;
            }
            //in the case that assignment not on that setting
            return null;
        }
        return new Assignment(start, end, hours, name, course, comment, points);
    }
}

