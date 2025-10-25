package adam.semantic_process_input;

import static adam.syntax_process_input.SplitInput.normaliseCategory;

public class Truthy {
    public String name;
    public boolean status;
    public Truthy(String name, boolean status)
    {
        this.status = status;
        this.name = name;
    }

    public void setStatus(String value)
    {
        String[] truthyValues = new String[]{normaliseCategory("display"),
                normaliseCategory("on"),
                normaliseCategory("show"),
                normaliseCategory("true"),
                normaliseCategory("yes")};

        status = false;

        for(String truthyValue : truthyValues)
        {
            if (normaliseCategory(value).equals(truthyValue)) {
                status = true;
                break;
            }
        }
    }
    public boolean getStatus(){return status;}
}
