package Engine.Instructions_Types.S_Type;

import Engine.ArchitectureTypes.ArchitectureTypeEnum;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Jump_Zero extends S_Instruction
{
    private final LabelInterface labelToJump;

    public Jump_Zero(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.JUMP_ZERO, context, holder, var, label);
        this.labelToJump = labelToJump;
    }

    public Jump_Zero(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
    {
        this(context, holder, var, FixedLabels.EMPTY, labelToJump);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        String _labelJump = labelToJump != null ? labelToJump.getLabelRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] IF %s = 0 GOTO %s ",
                this.lineIndex,
                _label,
                _var,
                _labelJump);
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
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);

        LabelInterface label_A = context.InsertLabelToEmptySpot();

        this.instructions =  new ArrayList<>(List.of(
        new JNZ(context, this, this.var,this.label, label_A),
        new Goto_Label(context, this, z_FAKE, this.labelToJump),
        new Neutral(context, this, Variable.OUTPUT, label_A)
        ));
    }

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }

    @Override
    public Jump_Zero createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Jump_Zero(context, holder, varChanges.get(var), newLabel, labelChanges.get(this.labelToJump));
    }
}
