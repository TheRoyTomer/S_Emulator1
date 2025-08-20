package Engine.Vars;

public class OutputWrapper implements Variable
{
    private Variable inner;
    private long value;

    public OutputWrapper()
    {
        this.inner = new VariableImplement(VariableType.OUTPUT, 0);
        this.value = 0;
    }

    @Override
    public String getVariableRepresentation()
    {
        return inner.getVariableRepresentation();
    }

    @Override
    public int getSerial()
    {
        return inner.getSerial();
    }

    @Override
    public VariableType getVariableType()
    {
        return inner.getVariableType();
    }

    public long getValue()
    {
        return value;
    }

    public void setValue(long value)
    {
        this.value = value;
    }
}
