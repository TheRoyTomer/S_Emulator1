package Instructions_Types;

import Vars.Label;
import Vars.Receivable;

import java.util.ArrayList;
import java.util.List;

public abstract class Instruction implements Calculable
{
    protected Label label;
    protected int cycles;
    protected int degree;
    protected List<Receivable> inputs = new ArrayList<>();

    public Instruction(Label label, Receivable input1)
    {
        this.label = label;
        inputs.add(input1);
    }

    public Instruction(Label label, Receivable input1, Receivable input2)
    {
        this.label = label;
        inputs.add(input1);
        inputs.add(input2);
    }

    @Override
    public abstract int calcDegree();

    @Override
    public int getDegree()
    {
        return this.degree;
    }

    @Override
    public abstract int calcCycles();

    @Override
    public abstract void calc();

    public int getCycles()
    {
        return this.cycles;
    }
}

