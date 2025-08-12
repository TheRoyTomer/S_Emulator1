package Vars;

public class Y_Var implements Receivable
{

    int value;

    public Y_Var()
    {
        this.value = 0;
    }

    @Override
    public String toString()
    {
        return "Y";
    }

    @Override
    public int getValue()
    {
        return value;
    }

    public void setValue(int _value)
    {
        this.value = _value;
    }
}
