package Instructions_Types;

import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

public abstract class Instruction implements Calculable
{
    protected final String name;
    protected final LabelInterface label; //MyLabel
    protected final int cycles;
    protected int degree;
    protected final Variable var; // The var Im "working" on
    protected Program holder;

    public Instruction(String name, Program holder, int cycles, Variable var, LabelInterface label)
    {;
        this.name = name;
        this.holder = holder;
        this.cycles = cycles;
        this.var = var;
        this.label = label;
    }

    @Override
    public abstract int calcDegree();

    @Override
    public int getDegree()
    {
        return this.degree;
    }

    @Override
    public abstract LabelInterface execute();

    public abstract String getInstructionRepresentation();

    public int getCycles()
    {
        return this.cycles;
    }

    public LabelInterface getLabel()
    {
        return this.label;
    }

}

