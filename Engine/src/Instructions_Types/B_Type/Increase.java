package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

public class Increase extends B_Instruction
{

    public Increase(Program holder, Variable var, LabelInterface label)
    {
        super("Increase", holder, 1, var, label);
    }

    public Increase(Variable var, Program holder)
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
        return String.format("(B) [%s] %s <- %s + 1 (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                var.getVariableRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = var.getValue();
        value++;
        var.setValue(value);
        return FixedLabels.EMPTY;
    }
}
