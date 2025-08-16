package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;


public class Decrease extends B_Instruction
{
    public Decrease(Program holder , Variable var, LabelInterface label)
    {
        super("Decrease", holder, 1, var, label);
    }



    public Decrease(Program holder, Variable var)
    {
        this(holder, var, FixedLabels.EMPTY);
    }

    @Override
    public String toString()
    {
        //ToDo: Data for debug
        return "";
    }

    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] %s <- %s - 1 (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                var.getVariableRepresentation(),
                cycles);

    }



    @Override
    public LabelInterface execute()
    {
        long value = var.getValue();
        if(value > 0) {value--;}
        var.setValue(value);
        return FixedLabels.EMPTY;
    }


}
