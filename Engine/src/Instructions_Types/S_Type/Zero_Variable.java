package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Zero_Variable extends S_Instruction
{

    public Zero_Variable(Program holder, Variable var, LabelInterface label)
    {
        super("ZERO_VARIABLE", holder, 1, var, label);
    }

    public Zero_Variable(Program holder, Variable var)
    {
        this(holder, var, FixedLabels.EMPTY);
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
        return String.format("(B) [%s] %s <- 0 (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                cycles);

    }


    @Override
    public LabelInterface execute()
    {
        var.setValue(0);
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: Build function
        List<Instruction> result = new ArrayList<>();
        return result;
    }
}
