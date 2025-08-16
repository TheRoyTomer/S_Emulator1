package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Jump_Equal_Variable extends S_Instruction
{
    private LabelInterface labelToJump;
    private Variable arg1;

    public Jump_Equal_Variable(Program holder, Variable var, LabelInterface label, LabelInterface labelToJump, Variable arg1)
    {
        super("JUMP_EQUAL_VARIABLE", holder, 2, var, label);
        this.labelToJump = labelToJump;
        this.arg1 = arg1;
    }

    public Jump_Equal_Variable(Program holder, Variable var, LabelInterface labelToJump, Variable arg1)
    {
        this(holder, var, FixedLabels.EMPTY, labelToJump, arg1);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] IF %s = %s GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                arg1.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long valueVar = var.getValue();
        if (valueVar == arg1.getValue()) return labelToJump;
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
