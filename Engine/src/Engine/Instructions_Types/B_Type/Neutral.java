package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.Variable;

public class Neutral extends B_Instruction
{

    public Neutral(Program context, Variable var, LabelInterface label)
    {
        super("NEUTRAL", context, 0, var, label);
    }

    public Neutral(Program context, Variable var)
    {
        this(context, var, FixedLabels.EMPTY);
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
