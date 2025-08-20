package Engine.Vars;

public interface Variable
{
    public String getVariableRepresentation();

    public int getSerial();


    public VariableType getVariableType();

    OutputWrapper OUTPUT = new OutputWrapper();
}
