package Vars;

import Programs.Program;

public abstract class VariableImplement implements Variable
{
    protected long serial;
    protected Program context;

    public VariableImplement(long serial)
    {
        this.serial = serial;
    }

    public long getSerial()
    {
        return serial;
    }

    public abstract long getValue();
    public abstract void setValue(long _value);
    public abstract boolean isExist();


}
