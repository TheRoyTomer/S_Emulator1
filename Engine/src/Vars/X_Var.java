package Vars;

import Programs.Program;

public class X_Var extends VariableImplement
{
    public X_Var(Program context, long serial)
    {
        super(context, serial);
    }

   /* @Override
    public String toString()
    {
        return "X" + serial;
    }*/

    @Override
    public String getVariableRepresentation()
    {
        return "X" + serial;
    }

    @Override
    public long getValue()
    {
        return context.getFromMapX(serial);
    }

    @Override
    public void setValue(long _value)
    {
        context.setInMapX(serial, _value);
    }

    @Override
    public boolean isExist()
    {
        return context.isExistInMapX(serial);
    }
}
