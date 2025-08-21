package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

public class Neutral extends B_Instruction
{

    public Neutral(Context context, Variable var, LabelInterface label)
    {
        super(InstructionData.NEUTRAL, context, var, label);
    }

    public Neutral(Context context, Variable var)
    {
        this(context, var, FixedLabels.EMPTY);
    }

    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] %s <- %s (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                var.getVariableRepresentation(),
                instructionData.getCycles());

    }


    @Override
    public LabelInterface execute()
    {
        return FixedLabels.EMPTY;
    }
}
