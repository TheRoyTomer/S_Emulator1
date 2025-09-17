package Engine.Instructions_Types.B_Type;

import Engine.Instructions_Types.B_Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.List;
import java.util.Map;

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

    @Override
    public Increase createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if (label == FixedLabels.EMPTY) {
            newLabel = FixedLabels.EMPTY;
        } else {
            newLabel = labelChanges.get(label);
        }
        return new Increase(context, holder, varChanges.get(var), newLabel);
    }
}
