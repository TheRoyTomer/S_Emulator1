package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

public class Increase extends B_Instruction
{

    public Increase(Context context, Variable var, LabelInterface label)
    {
        super(InstructionData.INCREASE, context, var, label);
    }

    public Increase(Context context, Variable var)
    {
        this(context, var, FixedLabels.EMPTY);
    }

    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] %s <- %s + 1 (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                var.getVariableRepresentation(),
                instructionData.getCycles());

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        value++;
        context.setVarValue(var, value);
        return FixedLabels.EMPTY;
    }
}
