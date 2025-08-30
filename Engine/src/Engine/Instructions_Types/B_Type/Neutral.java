package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

public class Neutral extends B_Instruction
{

    public Neutral(Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(InstructionData.NEUTRAL, context, holder, var, label);
    }

    public Neutral(Context context, S_Instruction holder, Variable var)
    {
        this(context, holder, var, FixedLabels.EMPTY);
    }

    @Override
    public LabelInterface execute()
    {
        return FixedLabels.EMPTY;
    }
}
