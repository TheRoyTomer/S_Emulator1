package Vars;

public class Z_Var extends VariableImplement
{

    public Z_Var()
    {
        super(0);
    }

    public Z_Var(long serial)
    {
        super(serial);
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
