package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.List;

public abstract class B_Instruction extends Instruction
{
    public B_Instruction(InstructionData instructionData, Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(instructionData, context,holder , var, label);
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
    public List<Instruction> getOneExpand()
    {
        return List.of(this);
    }
}

