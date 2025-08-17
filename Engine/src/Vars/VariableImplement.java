package Vars;

import Programs.Program;

public abstract class VariableImplement implements Variable
{
    protected long serial;
    protected Program context;

    public VariableImplement(Program context, long serial)
    {
        this.serial = serial;
        this.context = context;
    }

    public long getSerial()
    {
        return serial;
    }

    public abstract long getValue();
    public abstract void setValue(long _value);
    public abstract boolean isExist();


}
