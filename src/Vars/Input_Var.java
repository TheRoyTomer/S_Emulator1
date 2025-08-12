package Vars;

public abstract class Input_Var implements Receivable
{
    protected int serial;

    public Input_Var(int serial)
    {
        this.serial = serial;
    }

    public abstract int getValue();
    public abstract void setValue(int _value);

}
