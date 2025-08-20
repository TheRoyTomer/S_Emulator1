package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.List;

public abstract class Instruction implements Calculable
{
    protected final String name;
    protected final LabelInterface label; //MyLabel
    protected final int cycles;
    protected int maxDegree;
    protected final Variable var; // The var Im "working" on
    protected Program context;

    public Instruction(String name, Program context, int cycles, Variable var, LabelInterface label)
    {;
        this.name = name;
        this.context = context;
        this.cycles = cycles;
        this.var = var;
        this.label = label;
    }

    @Override
    public abstract int calcMaxDegree();

    @Override
    public abstract LabelInterface execute();

    public abstract List<Instruction> expand(int degree);

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

