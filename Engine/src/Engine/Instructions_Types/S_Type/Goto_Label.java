package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Goto_Label extends S_Instruction
{
    LabelInterface labelToJump;

    public Goto_Label(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.GOTO_LABEL, context, holder, var, label);
        this.labelToJump = labelToJump;
        //this.instructions = this.getSingleExpansion();
    }

    public Goto_Label(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
    {
       this(context, holder, var, FixedLabels.EMPTY, labelToJump);

    }


   /* @Override
    public String getInstructionRepresentation()
    {
        return String.format("#<%d>(S) [%s] GOTO %s (%d)",
                this.lineIndex,
                label.getLabelRepresentation(),
                labelToJump.getLabelRepresentation(),
                instructionData.getCycles());

    }*/

    @Override
    public List<Variable> getUsedVariables()
    {
        return List.of();
    }

    @Override
    public List<LabelInterface> getUsedLabels()
    {
        return List.of(label, labelToJump);
    }

    @Override
    public LabelInterface execute()
    {
        return labelToJump;
    }

    @Override
    public void setSingleExpansion()
    {
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;
        this.instructions = new ArrayList<>(List.of(
        new Increase(context, this, Z_FAKE, this.label),
        new JNZ(context, this, Z_FAKE, labelToJump)
        ));
    }

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }


}
