package adam.semantic_process_input;

import java.time.ZonedDateTime;

import static adam.ApplicationConfigurationConstants.*;
import static adam.syntax_process_input.SplitInput.normaliseCategory;

public class Configuration {
    private static OutputConfig outputType = OutputConfig.valueOf(DEFAULT_OUTPUT_TYPE);
    private volatile static Configuration instance;
    private static String timezone;
    public static Truthy assignmentFileLoading = new Truthy(normaliseCategory("assignment_file_loading"), DEFAULT_ASSIGNMENT_FILE_LOADING);
    public static Truthy assignmentItemProcessing = new Truthy(normaliseCategory("assignment_item_processing"), DEFAULT_ASSIGNMENT_ITEM_PROCESSING);
    public static Truthy exitMessage = new Truthy(normaliseCategory("exit_message"), DEFAULT_SHOW_EXIT_MESSAGE);
    public static Truthy ignoreIncompleteAssignments = new Truthy(normaliseCategory("ignore_incomplete_assignments"), DEFAULT_IGNORE_INCOMPLETE_ASSIGNMENTS);
    public static Truthy showComments = new Truthy(normaliseCategory("show_comments"), DEFAULT_SHOW_COMMENTS);
    public static Truthy showConfigStatus = new Truthy(normaliseCategory("show_config_status"), DEFAULT_SHOW_CONFIG_STATUS);
    public static Truthy welcomeMessage = new Truthy(normaliseCategory("welcome_message"), DEFAULT_SHOW_WELCOME_MESSAGE);

    public static String comment = null;

    public static ZonedDateTime now = DEFAULT_NOW;
    private Configuration() {
    }
    public static Configuration getInstance() {
        if(instance == null){
            synchronized (Configuration.class){
                if (instance == null)
                {
                    instance = new Configuration();
                }
            }
        }
        return instance;
    }
    public OutputConfig getOutputType() {
        return outputType;
    }
    public void setOutputType(OutputConfig outputType) {
        Configuration.outputType = outputType;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        Configuration.timezone = timezone;
    }
    public void setAssignmentFileLoading(String input){
        assignmentFileLoading.setStatus(input);
    }
    public boolean getAssignmentFileLoading(){
        return assignmentFileLoading.getStatus();
    }
    public void setAssignmentItemProcessing(String input){
        assignmentItemProcessing.setStatus(input);
    }
    public boolean getAssignmentItemProcessing(){return assignmentItemProcessing.getStatus();}

    public void setComment(String comment){
        Configuration.comment = comment;}

    public String getComment(){return comment;}
    public void setExitMessage(String input){exitMessage.setStatus(input);}
    public boolean getExitMessage(){return exitMessage.getStatus();}
    public void setIgnoreIncompleteAssignments(String input){ignoreIncompleteAssignments.setStatus(input);}
    public boolean getIgnoreIncompleteAssignments(){return ignoreIncompleteAssignments.getStatus();}

    public void setNow(ZonedDateTime input){//System.out.println("setting con now to: " + input);
        now = input;}

    public ZonedDateTime getNow(){return now;}
    public void setShowComments(String input){showComments.setStatus(input);}
    public boolean getShowComments(){return showComments.getStatus();}
    public void setShowConfigStatus(String input){showConfigStatus.setStatus(input);}
    public boolean getShowConfigStatus(){return showConfigStatus.getStatus();}
    public void setWelcomeMessage(String input){welcomeMessage.setStatus(input);}
    public boolean getWelcomeMessage(){return welcomeMessage.getStatus();}
}