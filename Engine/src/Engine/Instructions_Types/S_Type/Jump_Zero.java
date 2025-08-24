package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
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

    public Jump_Zero(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.JUMP_ZERO, context, holder, var, label);
        this.labelToJump = labelToJump;
    }

    public Jump_Zero(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
    {
        this(context, holder, var, FixedLabels.EMPTY, labelToJump);
    }

    public String getInstructionRepresentation()
    {
        return String.format("#<%d>(S) [%s] IF %s = 0 GOTO %s(%d)",
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
        if(value == 0) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);;
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;

        LabelInterface label_A = context.InsertLabelToEmptySpot();

        this.instructions =  new ArrayList<>(List.of(
        new JNZ(context, this, this.var, label_A, this.label),
        new Goto_Label(context, this, z_FAKE, this.labelToJump),
        new Neutral(context, this, Variable.OUTPUT, label_A)
        ));
    }
}
