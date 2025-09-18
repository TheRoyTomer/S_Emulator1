package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
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
import java.util.stream.LongStream;

public class Jump_Equal_Constant extends S_Instruction
{
    private final LabelInterface labelToJump;
    private final long constant;

    public Jump_Equal_Constant(Context context, S_Instruction holder, Variable var, LabelInterface label, LabelInterface labelToJump, long constant)
    {
        super(InstructionData.JUMP_EQUAL_CONSTANT, context, holder, var, label);
        this.labelToJump = labelToJump;
        this.constant = constant;
    }

    public Jump_Equal_Constant(Context context, S_Instruction holder, Variable var, LabelInterface labelToJump, long constant)
    {
        this(context, holder, var, FixedLabels.EMPTY, labelToJump, constant);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        String _labelJump = labelToJump != null ? labelToJump.getLabelRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] IF %s != %d GOTO %s ",
                this.lineIndex,
                _label,
                _var,
                this.constant,
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
        if (value == constant) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        Variable Z = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);
        LabelInterface label_A = context.InsertLabelToEmptySpot();

        List<Instruction> result = new ArrayList<>();
        result.add(new Assignment(context, this, Z, this.var, this.label));

        LongStream.range(0, constant)
                .forEach(i -> result.addAll(List.of(
                        new Jump_Zero(context, this, Z, label_A),
                        new Decrease(context, this, Z)
                )));
        result.addAll(List.of(
        new JNZ(context, this, Z,label_A),
        new Goto_Label(context, this, Z_FAKE,label_A),
        new Neutral(context, this, Variable.OUTPUT,label_A)
        ));

        this.instructions = result;
    }

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }

    @Override
    public Optional<Long> getConstantIfExist()
    {
        return Optional.of(this.constant);
    }

    @Override
    public Jump_Equal_Constant createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Jump_Equal_Constant(context, holder, varChanges.get(var), newLabel, labelChanges.get(this.labelToJump), this.constant);
    }
}
