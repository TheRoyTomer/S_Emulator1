package Vars;

import Programs.Program;

public class Z_Var extends VariableImplement
{
    public Z_Var(Program context, long serial)
    {
        super(context, serial);
    }

/*    @Override
    public String toString()
    {
        return "Z" + serial;
    }*/

    @Override
    public String getVariableRepresentation()
    {
        return "Z" + serial;
    }

    @Override
    public long getValue()
    {
        return context.getFromMapZ(serial);
    }

    @Override
    public void setValue(long _value)
    {
        context.setInMapZ(serial, _value);
    }

    @Override
    public boolean isExist()
    {
        return context.isExistInMapZ(serial);
    }
}
