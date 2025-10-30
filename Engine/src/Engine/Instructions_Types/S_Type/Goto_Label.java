package Engine.Instructions_Types.S_Type;

import Engine.ArchitectureTypes.ArchitectureTypeEnum;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
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

public class Goto_Label extends S_Instruction
{
    LabelInterface labelToJump;

    public Goto_Label(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.GOTO_LABEL, context, holder, var, label);
        this.labelToJump = labelToJump;
    }

    public Goto_Label(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
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
        return String.format("#<%d> (S) [%s] Goto %s (var is %s)",
                this.lineIndex,
                _label,
                _labelJump, _var);
    }

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
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);
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

    @Override
    public Goto_Label createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Goto_Label(context, holder, varChanges.get(var), newLabel, labelChanges.get(this.labelToJump));
    }


}
