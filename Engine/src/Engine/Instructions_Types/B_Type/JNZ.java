package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.List;

public class JNZ extends B_Instruction
{
    private LabelInterface labelToJump;

    public JNZ(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.JUMP_NOT_ZERO, context, holder, var, label);
        this.labelToJump = labelToJump;
    }

    public JNZ(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
    {
        this(context,holder , var, FixedLabels.EMPTY, labelToJump);
    }

    public String getCommandRep()
    {
        return String.format("IF %s != 0 GOTO %s",
                this.var.getVariableRepresentation()
                ,this.labelToJump.getLabelRepresentation());
    }
    public String getInstructionRepresentation()
    {
        return String.format("#<%d>(B) [%s] IF %s != 0 GOTO %s(%d)",
                this.lineIndex,
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                instructionData.getCycles());

    }

    @Override
    public List<LabelInterface> getUsedLabels()
    {
        return List.of(label, labelToJump);
    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if(value != 0) return labelToJump;
        return FixedLabels.EMPTY;
    }
}
