package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Jump_Equal_Constant extends S_Instruction
{
    private LabelInterface labelToJump;
    private long constant;

    public Jump_Equal_Constant(Program context, Variable var, LabelInterface label, LabelInterface labelToJump, long constant)
    {
        super("JUMP_EQUAL_CONSTANT", context, 2, var, label);
        this.labelToJump = labelToJump;
        this.constant = constant;
        this.instructions = this.getSingleExpansion();
        //this.maxDegree = this.calcMaxDegree();
    }

    public Jump_Equal_Constant(Program context, Variable var, LabelInterface labelToJump, long constant)
    {
        this(context, var, FixedLabels.EMPTY, labelToJump, constant);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] IF %s = %d GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                constant,
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if (value == constant) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {
        Variable Z = context.InsertVariableToEmptySpot(VariableType.WORK);;
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;
        LabelInterface label_A = context.InsertLabelToEmptySpot();

        List<Instruction> result = new ArrayList<>();
        result.add(new Assignment(context,Z, this.var, this.label));

        LongStream.range(0, constant)
                .forEach(i -> result.addAll(List.of(
                        new Jump_Zero(context, Z, label_A),
                        new Decrease(context, Z)
                )));
        result.addAll(List.of(
        new JNZ(context, Z,label_A),
        new Goto_Label(context, Z_FAKE,label_A),
        new Neutral(context, Variable.OUTPUT,label_A)
        ));

        //instructions = result;
        return result;
    }
}
