package Vars;

public class ConstInput implements Receivable
{
    private final int data;

    public ConstInput(int data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return String.valueOf(data);
    }

    @Override
    public int getValue()
    {
        return data;
    }
}
