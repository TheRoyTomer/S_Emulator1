package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.Variable;


public class Decrease extends B_Instruction
{
    public Decrease(Program context, Variable var, LabelInterface label)
    {
        super("Decrease", context, 1, var, label);
    }



    public Decrease(Program context, Variable var)
    {
        this(context, var, FixedLabels.EMPTY);
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
        long value = context.getVarValue(var);
        if(value > 0) {value--;}
        context.setVarValue(var, value);
        return FixedLabels.EMPTY;
    }

}
