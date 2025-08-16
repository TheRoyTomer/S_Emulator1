package Vars;

public class X_Var extends VariableImplement
{

    public X_Var()
    {
        super(0);
    }

    public X_Var(long serial)
    {
        super(serial);
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
