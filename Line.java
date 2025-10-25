package adam.read_file;

public class Line {

    public String content;

    public int lineNumber;
    public Line(String content, int lineNumber)
    {
        this.content = content.trim();
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {return lineNumber;}
    public String getContents(){return content;}

}
