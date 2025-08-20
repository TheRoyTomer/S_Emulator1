package Engine.Vars;

public class VariableImplement implements Variable
{
    private VariableType type;
    private int serial;

    public VariableImplement(VariableType type, int serial)
    {
        this.type = type;
        this.serial = serial;
    }

    @Override
    public String getVariableRepresentation()
    {
        return type.getVariableRepresentation(serial);
    }

    @Override
    public int getSerial()
    {
        return serial;
    }


    @Override
    public VariableType getVariableType()
    {
        return type;
    }
}
