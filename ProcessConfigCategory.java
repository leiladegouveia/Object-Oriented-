package adam.semantic_process_input;

import adam.Messages;
import adam.syntax_process_input.SplitInput;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static adam.ADAM.conf;
import static adam.ApplicationConfigurationConstants.*;
import static adam.Messages.SETTING_APP_CONFIG_ITEM;
import static adam.syntax_process_input.SplitInput.normaliseCategory;
import static java.time.format.DateTimeFormatter.*;

public class ProcessConfigCategory {
    public static String ASSIGNMENT_FILE_LOADING = normaliseCategory("assignment_file_loading");
    public static String ASSIGNMENT_FILE = normaliseCategory("assignment_file");
    public static String ASSIGNMENT_ITEM_PROCESSING = normaliseCategory("assignment_item_processing");
    public static String ASSIGNMENT_ITEM = normaliseCategory("assignment_item");
    public static String EXIT_MESSAGE = normaliseCategory("exit_message");
    public static String IGNORE_INCOMPLETE_ASSIGNMENTS =normaliseCategory("ignore_incomplete_assignments");
    public static String IGNORE_INCOMPLETE =normaliseCategory("ignore_incomplete");
    public static String SHOW_COMMENTS = normaliseCategory("show_comments");
    public static String SHOW_CONFIG_STATUS = normaliseCategory("show_config_status");
    public static String SHOW_CONFIG = normaliseCategory("show_config");
    public static String WELCOME_MESSAGE = normaliseCategory("welcome_message");
    public static String COMMENT = normaliseCategory("comment");
    public static String NOW = normaliseCategory("now");
    public static String OUTPUT_TYPE = normaliseCategory("output_type");
    public static String OUTPUT = normaliseCategory("output");

    public static boolean configChanged = false;

    public static String validEntries = String.format("[%s, %s, %s, %s, %s, %s, %s, %s, %s, %s]",
            ASSIGNMENT_FILE_LOADING,
            ASSIGNMENT_ITEM_PROCESSING,
            COMMENT,
            EXIT_MESSAGE,
            IGNORE_INCOMPLETE_ASSIGNMENTS,
            NOW,
            OUTPUT_TYPE,
            SHOW_COMMENTS,
            SHOW_CONFIG_STATUS,
            WELCOME_MESSAGE);

    private ProcessConfigCategory() {} // Utility class: do not instantiate

    public static void processCategories(List<SplitInput> splitLines) {

        //takes a list of split inputs, this is the information about the configuration
        for ( SplitInput splitLine : splitLines ) {

            if(splitLine != null){

                String category = splitLine.category();
                category = standardise(category);

                //System.out.println("category: " + category);

                String data = splitLine.data();
                boolean invalid = false;

                if(category.equals(OUTPUT_TYPE) || category.equals(OUTPUT)) {processOutput(data);}
                else if(category.equals(ASSIGNMENT_FILE_LOADING) || category.equals(ASSIGNMENT_FILE)) {conf.setAssignmentFileLoading("true");}
                else if(category.equals(ASSIGNMENT_ITEM_PROCESSING) || category.equals(ASSIGNMENT_ITEM)) {conf.setAssignmentItemProcessing("true");}
                else if(category.equals(COMMENT)) {conf.setComment(data);
                }
                else if(category.equals(EXIT_MESSAGE)) {conf.setExitMessage(data);}
                else if(category.equals(IGNORE_INCOMPLETE_ASSIGNMENTS) || category.equals(IGNORE_INCOMPLETE)) {conf.setIgnoreIncompleteAssignments(data);}
                else if(category.equals(NOW)) {conf.setNow(ZonedDateTime.parse(data, ISO_ZONED_DATE_TIME));}
                    //System.out.println(data);
                    //System.out.println(ZonedDateTime.parse(data));
                    //conf.setNow(ZonedDateTime.parse(data).withFixedOffsetZone());}
                else if(category.equals(SHOW_COMMENTS)) {processComment(category, data);}//conf.setShowComments(data);}
                else if(category.equals(SHOW_CONFIG_STATUS) || category.equals(SHOW_CONFIG)) {conf.setShowConfigStatus(data);}
                else if(category.equals(WELCOME_MESSAGE)) {conf.setWelcomeMessage(data);}
                else{
                    System.out.printf(Messages.INVALID_APP_CONFIG_ITEM + "\n", category);
                    System.out.printf(Messages.VALID_POSSIBILITIES.toString(), validEntries + "\n");
                    invalid = true;
                    }

                if(!invalid){printSetting(category,data);}
            }

        }
    }

    private static void processComment(String category, String data){

        Truthy evaluate = new Truthy("evaluate", true);
        evaluate.setStatus(data);
        //System.out.println("status is: " + evaluate.getStatus());

        category = "COMMENTS";

        if(configChanged == false){
            if(evaluate.getStatus()){System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", alterCat(category), SHOWING);}
            else{System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", alterCat(category), NOT_SHOWING);
                conf.setComment(data);}
            configChanged = true;
        }
        else{
            if(evaluate.getStatus() == conf.getShowComments()) {
                //System.out.printf("1. already the same, current status is: %s, trying to set to %s\n",
                //        conf.getShowComments(), evaluate.getStatus());
            }
            else{
                //System.out.printf("2. not the same, current status is: %s, trying to set to %s\n",
                //        conf.getShowComments(), evaluate.getStatus());

                if(conf.getShowComments()){
                    System.out.printf(Messages.OVERWRITING_APP_CONFIG_ITEM + "\n", category, SHOWING, NOT_SHOWING);
                    conf.setShowComments("false");
                }
                else{
                    System.out.printf(Messages.OVERWRITING_APP_CONFIG_ITEM + "\n", category, NOT_SHOWING, SHOWING);
                    conf.setShowComments("true");
                }
                conf.setComment(data);
            }
        }

    }

    public static void printSetting(String category, String data){

        if(category.equals(COMMENT)) {
            if(conf.getShowComments()){
                System.out.println(data);
            }
        }
        else if(category.equals(NOW)){
            System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", category, data);
        }
        else if(category.equals(SHOW_COMMENTS)){
            category = "COMMENTS";}
            //processComment(category, data);}
        else if(!category.equals(OUTPUT_TYPE) && !category.equals(OUTPUT)){

            if(category.equals(SHOW_CONFIG_STATUS)){category = "CONFIG_STATUS";}

            Truthy evaluate = new Truthy("evaluate", false);
            evaluate.setStatus(data);
            if(evaluate.getStatus()){System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", alterCat(category), SHOWING);}
            else{System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", alterCat(category), NOT_SHOWING);}
        }
    }

    public static String alterCat(String category){
        if(category.equals(SHOW_CONFIG)){return normaliseCategory("CONFIG_STATUS");}
        else if(category.equals(ASSIGNMENT_ITEM)){return normaliseCategory(ASSIGNMENT_ITEM_PROCESSING);}
        else if(category.equals(ASSIGNMENT_FILE)){return normaliseCategory(ASSIGNMENT_FILE_LOADING);}
        else if(category.equals(IGNORE_INCOMPLETE)){return normaliseCategory(IGNORE_INCOMPLETE_ASSIGNMENTS);}
        else if(category.equals(OUTPUT)){return normaliseCategory(OUTPUT_TYPE);}
        else{return category;}
    }
    private static void processOutput(String data){
        if(normaliseCategory(data).equals(normaliseCategory("daily"))) {
            conf.setOutputType(OutputConfig.DAILY);
            System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", "OUTPUT_TYPE", "DAILY");
        }
        else if(normaliseCategory(data).equals(normaliseCategory("summary"))) {
            conf.setOutputType(OutputConfig.SUMMARY);
            System.out.printf(Messages.SETTING_APP_CONFIG_ITEM + "\n", "OUTPUT_TYPE", "SUMMARY");
        }
        else {
            System.out.printf(Messages.INVALID_SUMMARY_TYPE.toString(), normaliseCategory(data), "[DAILY, SUMMARY]\n" );
        }
    }

    private static String standardise(String category){

        String[] words = category.split(" ");

        for (String word: words) {
            //System.out.println("word is: " + word);
        }

        String categoryUnderscored = "";

        if(words.length > 1){

            for (int i = 0; i < words.length; i++) {

                    categoryUnderscored = categoryUnderscored + words[i] + "_";
            }
        }
        else{categoryUnderscored = category;}

        //System.out.println(categoryUnderscored);

        while(categoryUnderscored.charAt(categoryUnderscored.length()-1) == '_'){
            categoryUnderscored = categoryUnderscored.substring(0, categoryUnderscored.length() - 1);
        }

        //System.out.println(categoryUnderscored);

        return categoryUnderscored;
    }

}

