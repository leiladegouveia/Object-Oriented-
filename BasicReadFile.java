package adam.read_file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BasicReadFile {
    private BasicReadFile() {} // Make the default constructor private

    public static List<String> read (String filename) {

        Path path = Paths.get(filename);
        List<String> lines;

        try
        {
            lines = Files.readAllLines(path);
            return lines;
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            return null;
        }

    }

    public static List<Line> getProcessedFileLines(String filename) {

        List<String> lines = read(filename);
        List<Line> processedLines = new ArrayList<>();
        int lineNumber = 0;

        for (String line : lines) {
            Line tempLine = new Line(line, lineNumber);
            if (!tempLine.getContents().isEmpty()) {
                lineNumber++;
                processedLines.add(tempLine);
            }
        }
        return processedLines;

    }
}
