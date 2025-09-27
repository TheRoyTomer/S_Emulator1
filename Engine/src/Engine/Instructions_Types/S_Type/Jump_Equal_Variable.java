package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
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

public class Jump_Equal_Variable extends S_Instruction
{
    private final LabelInterface labelToJump;
    private final Variable arg1;

    public Jump_Equal_Variable(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump, Variable arg1)
    {
        super(InstructionData.JUMP_EQUAL_VARIABLE, context, holder, var, label);
        this.labelToJump = labelToJump;
        this.arg1 = arg1;
    }

    public Jump_Equal_Variable(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump, Variable arg1)
    {
        this(context, holder, var, FixedLabels.EMPTY, labelToJump, arg1);
    }

    @Override
    public List<Variable> getUsedVariables()
    {
        return List.of(var, arg1);
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
        if (value == context.getVarValue(arg1)) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {


        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable z_B = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);

        LabelInterface label_A = context.InsertLabelToEmptySpot();
        LabelInterface label_B = context.InsertLabelToEmptySpot();
        LabelInterface label_C = context.InsertLabelToEmptySpot();

        this.instructions =  new ArrayList<>(List.of(
        new Assignment(context, this, z_A, this.var, this.label),
        new Assignment(context, this, z_B, this.arg1),
        new Jump_Zero(context, this, z_A, label_B, label_C),
        new Jump_Zero(context, this, z_B, label_A),
        new Decrease(context, this, z_A),
        new Decrease(context, this, z_B),
        new Goto_Label(context, this, z_FAKE, label_B),
        new Jump_Zero(context, this, z_B, label_C, this.labelToJump),
        new Neutral(context, this, Variable.OUTPUT, label_A)
        ));
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        String _arg = arg1 != null ? arg1.getVariableRepresentation() : "Null";
        String _labelJump = labelToJump != null ? labelToJump.getLabelRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] IF %s != %s GOTO %s ",
                this.lineIndex,
                _label,
                _var,
                _arg,
                _labelJump);
    }

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }

    @Override
    public Optional<Variable> getArgIfExist()
    {
        return Optional.ofNullable(this.arg1);
    }

    @Override
    public Jump_Equal_Variable createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Jump_Equal_Variable(context, holder, varChanges.get(var), newLabel, labelChanges.get(this.labelToJump), varChanges.get(this.arg1));
    }

}
