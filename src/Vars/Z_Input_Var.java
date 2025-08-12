package Vars;

public class Z_Input_Var extends Input_Var
{

    public Z_Input_Var()
    {
        super(0);
    }

    public Z_Input_Var(int serial)
    {
        super(serial);
    }

    @Override
    public String toString()
    {
        return "Z" + serial;
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
