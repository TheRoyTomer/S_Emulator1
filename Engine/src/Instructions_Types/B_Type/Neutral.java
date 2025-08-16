package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

public class Neutral extends B_Instruction
{

    public Neutral(Program holder, Variable var, LabelInterface label)
    {
        super("NEUTRAL", holder, 0, var, label);
    }

    public Neutral(Program holder, Variable var)
    {
        this(holder, var, FixedLabels.EMPTY);
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
                var.getVariableRepresentation(),
                cycles);

    }


    @Override
    public LabelInterface execute()
    {
        return FixedLabels.EMPTY;
    }
}
