package adam.read_stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BasicReadStream {
    private BasicReadStream() {
    }

    public static List<String> read(InputStream stream) {
        // Notice the apparent type on the left and the actual type on the right
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
        {
            {
                String line;
                //in order to read one line at a time
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
        }

        // It is safe to return lines because it is a local variable
        return lines;
    }
}
