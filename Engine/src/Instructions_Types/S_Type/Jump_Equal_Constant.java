package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Jump_Equal_Constant extends S_Instruction
{
    private LabelInterface labelToJump;
    private long constant;

    public Jump_Equal_Constant(Program holder, Variable var, LabelInterface label, LabelInterface labelToJump, long constant)
    {
        super("JUMP_EQUAL_CONSTANT", holder, 2, var, label);
        this.labelToJump = labelToJump;
        this.constant = constant;
    }

    public Jump_Equal_Constant(Program holder, Variable var, LabelInterface labelToJump, long constant)
    {
        this(holder, var, FixedLabels.EMPTY, labelToJump, constant);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] IF %s = %d GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                constant,
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = var.getValue();
        if (value == constant) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: BUILD FUNCTION
        List<Instruction> result = new ArrayList<>();
        return result;
    }
}
