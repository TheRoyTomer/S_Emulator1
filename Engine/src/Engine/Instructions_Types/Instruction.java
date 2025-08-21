package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.List;

public abstract class Instruction implements Calculable
{
    protected InstructionData instructionData;
    protected final LabelInterface label; //MyLabel
    protected int maxDegree;
    protected final Variable var; // The var Im "working" on
    protected Context context;

    public Instruction(InstructionData instructionData, Context context, Variable var, LabelInterface label)
    {
        this.instructionData = instructionData;
        this.context = context;
        this.var = var;
        this.label = label;
    }

    @Override
    public abstract int calcMaxDegree();

    @Override
    public abstract LabelInterface execute();

    public abstract List<Instruction> expand(int degree);

    public abstract String getInstructionRepresentation();

    public LabelInterface getLabel()
    {
        return this.label;
    }

    public int getCycles()
    {
        return this.instructionData.getCycles();
    }

    @Override
    public List<Variable> getUsedVariables()
    {
        return List.of(var);
    }


}

