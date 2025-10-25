package adam;

import adam.assignment.Assignment;
import adam.read_file.BasicReadFile;
import adam.read_stream.BasicReadStream;
import adam.semantic_process_input.Configuration;
import adam.semantic_process_input.OutputConfig;
import adam.semantic_process_input.ProcessAssignmentCategory;
import adam.semantic_process_input.ProcessConfigCategory;
import adam.syntax_process_input.BasicProcessInput;
import adam.syntax_process_input.SplitInput;
import adam.timetable.DailyTimetable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static adam.ApplicationConfigurationConstants.*;
import static adam.semantic_process_input.ProcessConfigCategory.*;

public class ADAM {

  protected final String[] args;
  public static Configuration conf = Configuration.getInstance();
  public List<Assignment> assignments;

  // You MUST NOT edit the signature of this constructor
  public ADAM(String[] args) {
    // You MAY edit the body of this constructor

    assignments = new ArrayList<>();
    this.args = args;
  }

  // You MUST NOT edit the signature of this method
  public void run() {

    setConfig();

    if(conf.getShowConfigStatus()){showConfigStatus();}
    if(conf.getWelcomeMessage()){System.out.println(Messages.WELCOME);}

    addAssignments();
    removeNullAssignments();

    if(conf.getOutputType() == OutputConfig.DAILY) {printDaily();}
    else {printSummary();}

    if(conf.getExitMessage()){System.out.println(Messages.EXIT);}
  }

  private void removeNullAssignments(){
    // Remove all null entries
    for (int x = 0; x < assignments.size(); x++) {
      if (assignments.get(x) == null) {
        assignments.remove(x);
        x--; // Adjust index to account for removed element
      }
    }
    if(assignments.isEmpty()){System.out.println(Messages.NO_ASSIGNMENTS);}

  }

  private void setConfig()
  {
    //read config from stream reader
    List<String> unprocessedConfig = BasicReadStream.read(System.in);

    //syntax process
    List<SplitInput> processedConfig = BasicProcessInput.processInput(unprocessedConfig, "\t");

    //semantic processing input
    ProcessConfigCategory.processCategories(processedConfig);
  }

  private void addAssignments()
  {
    //processing each file in list of files
    for(String file : args) {

      //if loading file true
      if(conf.getAssignmentFileLoading()) {
        System.out.printf(Messages.LOADING_ASSIGNMENT.toString(), processedFileName(file) + "\n");}

      //processing file
      List<String> unprocessedAssignment = BasicReadFile.read(file);
      List<SplitInput> processedAssignment = BasicProcessInput.processInput(unprocessedAssignment, "\t");

      //if item processing true
      if(conf.getAssignmentItemProcessing()){
        for (int x = 0; x < processedAssignment.size(); x++) {
          String configurationString = String.format("ConfigurationItem[filename=%s, " +
                          "command=%s, data=%s, lineNumber=%s]\n", processedFileName(file), processedAssignment.get(x).category(),
                  processedAssignment.get(x).data(), x + 1);
          System.out.printf(Messages.READ_CONFIGURATION_ITEM.toString(), configurationString);
        }
      }

      assignments.add(ProcessAssignmentCategory.processCategories(processedAssignment, file));
    }

    if(assignments.isEmpty()){System.out.println(Messages.NO_ASSIGNMENTS);}

  }

  private String processedFileName(String string){

    String[] parts = string.split("/");
    String fileName = parts[parts.length - 1];
    //String[] fileNameParts = fileName.split("\\.");

    //return fileNameParts[0];
    return fileName;
  }

  private void showConfigStatus(){
    System.out.println(Messages.APPLICATION_SETTINGS);
    System.out.printf(Messages.CONFIG_STATUS.toString(), ASSIGNMENT_FILE_LOADING, toUpper(conf.getAssignmentFileLoading()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), ASSIGNMENT_ITEM_PROCESSING, toUpper(conf.getAssignmentItemProcessing()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), COMMENT, toUpper(conf.getShowComments()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), EXIT_MESSAGE, toUpper(conf.getExitMessage()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), IGNORE_INCOMPLETE_ASSIGNMENTS, toUpper(conf.getIgnoreIncompleteAssignments()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), NOW, conf.getNow());
    System.out.printf(Messages.CONFIG_STATUS.toString(), OUTPUT_TYPE, conf.getOutputType());
    System.out.printf(Messages.CONFIG_STATUS.toString(), SHOW_COMMENTS, toUpper(conf.getShowComments()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), SHOW_CONFIG_STATUS, toUpper(conf.getShowConfigStatus()));
    System.out.printf(Messages.CONFIG_STATUS.toString(), WELCOME_MESSAGE, toUpper(conf.getWelcomeMessage()));
  }

  private static String formatNow(){
    ZonedDateTime now = conf.getNow();
    System.out.println(now.getOffset());
    //2024-04-29T09:00:00+00:00[Europe/London]
    if(now.getHour() == 9){
      return String.format("%s-%s-%sT%s:%s+00:00[Europe/London]", now.getYear(),
              normaliseInt(now.getMonthValue()),normaliseInt(now.getDayOfMonth()), normaliseInt(now.getHour()),
              normaliseInt(now.getMinute()),normaliseInt(now.getSecond()), now.getOffset());
    }
    return String.format("%s-%s-%sT%s:%s:%s%s[Europe/London]", now.getYear(),
            normaliseInt(now.getMonthValue()),normaliseInt(now.getDayOfMonth()), normaliseInt(now.getHour()),
            normaliseInt(now.getMinute()),normaliseInt(now.getSecond()), now.getOffset());
  }
  private static String normaliseInt(int number){
    if(number > 9){return String.valueOf(number);}
    else{
      return ("0" + String.valueOf(number));
    }
  }

  private String toUpper(boolean bool) {
    if(bool){return SHOWING;}
    return NOT_SHOWING;
  }

  private void printDaily(){
    DailyTimetable dailyTimetable = new DailyTimetable(assignments);
    dailyTimetable.printTimetable();
  }

  private void printSummary() {
    for (Assignment assignment : assignments) {
      assignment.printComment();
    }

    for (Assignment assignment : assignments) {
      assignment.printSummary();
    }
  }
}
