package adam.syntax_process_input;

import static adam.semantic_process_input.ProcessConfigCategory.*;

/* TODO When you finished this class, IntelliJ can probably turn it
        into a record. Before you do that, duplicate the entire contents and
        put the duplicate contents into a block comment. After you have asked
        IntelliJ to convert the class into a record, you can compare the
        difference in source code between the record and the original class.
 */
public final class SplitInput {
    private final String category;
    private final String data;

    /**
     * @param category The name of the line from the input
     * @param data     The String that accompanied the command in the input
     */
    public SplitInput(String category, String data) {
        this.category = normaliseCategory(category);
        this.data     = data;
    }

    public String category() {return category;}

    public String data() {return data;}

    public static String normaliseCategory(String rawCommand) {

        rawCommand = rawCommand.trim().toUpperCase();


        if(rawCommand.equals(SHOW_CONFIG)){rawCommand = normaliseCategory(SHOW_CONFIG_STATUS);}
        else if(rawCommand.equals(ASSIGNMENT_ITEM)){rawCommand = normaliseCategory(ASSIGNMENT_ITEM_PROCESSING);}
        else if(rawCommand.equals(ASSIGNMENT_FILE)){rawCommand = normaliseCategory(ASSIGNMENT_FILE_LOADING);}
        else if(rawCommand.equals(IGNORE_INCOMPLETE)){rawCommand = normaliseCategory(IGNORE_INCOMPLETE_ASSIGNMENTS);}
        else if(rawCommand.equals(OUTPUT)){rawCommand = normaliseCategory(OUTPUT_TYPE);}

        return rawCommand;
    }
}

