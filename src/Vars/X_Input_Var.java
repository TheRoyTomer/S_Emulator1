package Vars;

public class X_Input_Var extends Input_Var
{

    public X_Input_Var()
    {
        super(0);
    }

    public X_Input_Var(int serial)
    {
        super(serial);
    }

    @Override
    public String toString()
    {
        return "X" + serial;
    }

    @Override
    public int getValue()
    {
        //ToDo: Map
        return 0;
    }

    @Override
    public void setValue(int _value)
    {
        //ToDo: Change value in map
    }
}
