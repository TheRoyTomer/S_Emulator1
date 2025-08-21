package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;

public class Goto_Label extends S_Instruction
{
    LabelInterface labelToJump;

    public Goto_Label(Context context, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.GOTO_LABEL, context, var, label);
        this.labelToJump = labelToJump;
        this.instructions = this.getSingleExpansion();
    }

    public Goto_Label(Context context,Variable var, LabelInterface labelToJump)
    {
       this(context, var, FixedLabels.EMPTY, labelToJump);

    }

    @Override
    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] GOTO %s (%d)",
                label.getLabelRepresentation(),
                labelToJump.getLabelRepresentation(),
                instructionData.getCycles());

    }

    @Override
    public LabelInterface execute()
    {
        return labelToJump;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {
        List<Instruction> result = new ArrayList<>(List.of(
        new Increase(context, this.var, this.label),
        new JNZ(context, this.var, labelToJump)
        ));
        //instructions = result;

        return result;
    }


}
