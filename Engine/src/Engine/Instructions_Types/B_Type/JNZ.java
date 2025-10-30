package Engine.Instructions_Types.B_Type;

import Engine.ArchitectureTypes.ArchitectureTypeEnum;
import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JNZ extends B_Instruction
{
    private final LabelInterface labelToJump;

    public JNZ(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super(InstructionData.JUMP_NOT_ZERO, context, holder, var, label);
        this.labelToJump = labelToJump;
    }

    public JNZ(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump)
    {
        this(context,holder , var, FixedLabels.EMPTY, labelToJump);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        String _labelJump = labelToJump != null ? labelToJump.getLabelRepresentation() : "Null";
        return String.format("#<%d> (B) [%s]  IF %s != 0 GOTO %s",
                this.lineIndex,
                _label,
                _var, _labelJump);
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

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }

    @Override
    public JNZ createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if (label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}

        return new JNZ(context, holder, varChanges.get(var), newLabel, labelChanges.get(this.labelToJump));
    }
}
