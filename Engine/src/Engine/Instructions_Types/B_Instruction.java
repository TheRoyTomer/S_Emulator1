package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.List;

public abstract class B_Instruction extends Instruction
{
    public B_Instruction(InstructionData instructionData, Context context, Variable var, LabelInterface label)
    {
        super(instructionData, context, var, label);
        this.maxDegree = this.calcMaxDegree();
    }


    @Override
    public int calcMaxDegree()
    {
        return 0;
    }

    public abstract String getInstructionRepresentation();

    @Override
    public abstract LabelInterface execute();

    @Override
    public List<Instruction> expand(int degree)
    {
        return List.of(this);
    }
}

