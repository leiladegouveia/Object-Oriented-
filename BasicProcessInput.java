package adam.syntax_process_input;

import java.util.ArrayList;
import java.util.List;

public class BasicProcessInput {
    private BasicProcessInput() {} // Makes this a utility class

    public static List<SplitInput> splitInput(List<String> fileLines, String splitPattern) {
        /* TODO Use IntelliJ to change the method signature to have
                an extra parameter that specifies what pattern is used
                to split the input. This will make this method more general.
         */

        /* TODO Initialise splitLines with its final size
                 Note the apparent type on the left, the actual type on the right
         */
        List<SplitInput> splitLines = new ArrayList<>();

        for (String line : fileLines) {
            //splits into max two parts
            String[] parts = line.split(splitPattern, 2);
            //if string splits into two parts
            if(parts.length == 2)
            {
                splitLines.add(new SplitInput(parts[0], parts[1]));
            }
            //if no split pattern is present, will not split into two, so must have a null case
            else if(parts.length == 1)
            {
                splitLines.add(new SplitInput(parts[0], null));
            }
        }
        return splitLines;

        /* TODO Iterate over the raw input, splitting, and saving
                the split input into the new structure.
                Try not to split the data component if it contains tabs.
         */

        /* TODO Design decision: where to remove blank lines from the
                processing stream?
                You could do it here, in which case "splitInput" might not
                be the clearest name.
                Or you could have another method that removes blank lines,
                in which case do you put it before or after splitInput?
         */

        /* TODO Optional but recommended:
                1. improve consistency by normalising the form categories
                2. handle lines that do not split
         */

    }

    public static List<String> removeEmptyLines (List<String> fileLines)
    {
        List<String> withoutEmptyLines = new ArrayList<>();

        for (String line : fileLines) {
            //miss out empty lines
            if (!line.isEmpty()) {
                withoutEmptyLines.add(line);
            }
        }
        return withoutEmptyLines;
    }

    public static List<SplitInput> splitInputOnTab(List<String> fileLines){

        return splitInput(fileLines, "\n"); // TODO Add the extra parameter
    }

    public static List<SplitInput> processInput(List<String> fileLines, String splitPattern)
    {
        return splitInput ((removeEmptyLines(fileLines)),splitPattern);
    }

    /* TODO Having normaliseCategory here means your code needs to "remember"
            to normalise categories before creating the object that stores
            a particular line. It also means you are designing in an
            imperative paradigm.
            Look in the Code Quality document for the example on potatoes.
            In the imperative paradigm you have a method that takes a potato
            as a parameter and boils it. An object-oriented potato knows how
            to boil itself. Use that analogy to think where the "best" place
            to have this method is and how to ensure it is always used.
            IntelliJ has tools to help you move members around.
     */
}
