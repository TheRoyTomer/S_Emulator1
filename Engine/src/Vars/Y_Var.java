package Vars;

public class Y_Var implements Variable
{

    long value;

    public Y_Var()
    {
        this.value = 0;
    }

  /*  @Override
    public String toString()
    {
        return "Y";
    }*/

    @Override
    public String getVariableRepresentation()
    {
        return "Y";
    }

    @Override
    public long getValue()
    {
        return value;
    }

    public void setValue(long _value)
    {
        this.value = _value;
    }
}
