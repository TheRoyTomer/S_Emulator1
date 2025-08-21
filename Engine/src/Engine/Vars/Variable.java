package Engine.Vars;

public interface Variable
{
    public String getVariableRepresentation();

    public int getSerial();


    public VariableType getVariableType();

    VariableImplement OUTPUT = new VariableImplement(VariableType.OUTPUT, 0);
}
