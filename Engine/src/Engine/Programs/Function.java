package Engine.Programs;

public class Function extends Program
{
    String userString;

    public Function(String userString)
    {
        super();
        this.userString = userString;
    }

    public String getUserString()
    {
        return this.userString;
    }

}
