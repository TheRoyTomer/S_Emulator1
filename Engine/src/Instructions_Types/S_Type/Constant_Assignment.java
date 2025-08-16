package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Constant_Assignment extends S_Instruction
{
    private final long constant;

    public Constant_Assignment(Program holder, Variable var, LabelInterface label, long constant)
    {
        super("CONSTANT_ASSIGNMENT", holder, 2, var, label);
        this.constant = constant;
    }

    public Constant_Assignment(Program holder, Variable var, long constant)
    {
        this(holder, var, FixedLabels.EMPTY, constant);
    }

    @Override
    public String toString()
    {
        //ToDo: String format this bitch
        return "";
    }

    @Override
    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] %s <- %d (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                constant,
                cycles);

    }


    @Override
    public LabelInterface execute()
    {
        var.setValue(constant);
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
