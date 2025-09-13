package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.List;

public class Increase extends B_Instruction
{

    public Increase(Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(InstructionData.INCREASE, context, holder, var, label);
    }

    public Increase(Context context, S_Instruction holder, Variable var)
    {
        this(context, holder , var, FixedLabels.EMPTY);
    }

    public List<Variable> getChangedVariables() {return List.of(var);}


    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        value++;
        context.setVarValue(var, value);
        return FixedLabels.EMPTY;
    }
}
