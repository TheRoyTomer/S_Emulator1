package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;


public class Decrease extends B_Instruction
{
    public Decrease(Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(InstructionData.DECREASE, context, holder, var, label);
    }



    public Decrease(Context context, S_Instruction holder, Variable var)
    {
        this(context, holder, var, FixedLabels.EMPTY);
    }

    public String getInstructionRepresentation()
    {
        return String.format("#<%d>(B) [%s] %s <- %s - 1 (%d)",
                this.lineIndex,
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                var.getVariableRepresentation(),
                instructionData.getCycles());

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if(value > 0) {value--;}
        context.setVarValue(var, value);
        return FixedLabels.EMPTY;
    }

}
