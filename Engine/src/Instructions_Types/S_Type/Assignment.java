package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.VariableImplement;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends S_Instruction
{
   private final VariableImplement arg1;

    public Assignment(Program holder, Variable var, LabelInterface label, VariableImplement arg1)
    {
        super("ASSIGNMENT", holder, 4, var, label);
        this.arg1 = arg1;
    }

    public Assignment(Program holder, Variable var, VariableImplement arg1)
    {
        this(holder, var, FixedLabels.EMPTY, arg1);
    }

    @Override
    public String toString()
    {
        //ToDo: String format this bitch
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] %s <- %s (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                arg1.getVariableRepresentation(),
                cycles);

    }


    @Override
    public LabelInterface execute()
    {
        var.setValue(arg1.getValue());
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
