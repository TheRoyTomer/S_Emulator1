package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.OutputWrapper;
import Engine.Vars.Variable;

public class JNZ extends B_Instruction
{
    private LabelInterface labelToJump;

    public JNZ(Program context, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super("JUMP_NOT_ZERO", context, 2, var, label);
        this.labelToJump = labelToJump;
    }

    public JNZ(Program context, Variable var, LabelInterface labelToJump)
    {
        this(context, var, FixedLabels.EMPTY, labelToJump);
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
        long value = context.getVarValue(var);
        if(value != 0) return labelToJump;
        return FixedLabels.EMPTY;
    }
}
