package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

public class JNZ extends B_Instruction
{
    private LabelInterface labelToJump;

    public JNZ(Program holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super("JUMP_NOT_ZERO", holder, 2, var, label);
        this.labelToJump = labelToJump;
    }

    public JNZ(Program holder, Variable var, LabelInterface labelToJump)
    {
        this(holder, var, FixedLabels.EMPTY, labelToJump);
    }

    @Override
    public String toString()
    {
        //ToDo: String format this bitch
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] IF %s!=0 GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = var.getValue();
        if(value != 0) return labelToJump;
        return FixedLabels.EMPTY;
    }
}
