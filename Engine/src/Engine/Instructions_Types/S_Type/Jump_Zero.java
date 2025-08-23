package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;

public class Jump_Zero extends S_Instruction
{
    private LabelInterface labelToJump;

    public Jump_Zero(Context context, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.JUMP_ZERO, context, var, label);
        this.labelToJump = labelToJump;
        //this.instructions = this.getSingleExpansion();
    }

    public Jump_Zero(Context context, Variable var, LabelInterface labelToJump)
    {
        this(context, var, FixedLabels.EMPTY, labelToJump);
    }

    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] IF %s = 0 GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                instructionData.getCycles());

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if(value == 0) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public void getSingleExpansion()
    {
        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);;
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;

        LabelInterface label_A = context.InsertLabelToEmptySpot();

        this.instructions =  new ArrayList<>(List.of(
        new JNZ(context,this.var, label_A, this.label),
        new Goto_Label(context, z_FAKE, this.labelToJump),
        new Neutral(context, Variable.OUTPUT, label_A)
        ));
    }
}
