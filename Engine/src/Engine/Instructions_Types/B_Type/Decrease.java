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


public class Decrease extends B_Instruction
{
    public Decrease(Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(InstructionData.DECREASE, context, holder, var, label);
    }



    public Decrease(Context context, S_Instruction holder, Variable var)
    {
        this(context, holder, var, FixedLabels.EMPTY);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        return String.format("#<%d> (B) [%s] %s <- %s - 1",
                this.lineIndex,
                _label,
                _var, _var);
    }

    public List<Variable> getChangedVariables()
    {
        if (context.getVarValue(var) != 0) {return List.of(var);}
        return List.of();
    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if(value > 0) {value--;}
        context.setVarValue(var, value);
        return FixedLabels.EMPTY;
    }
    @Override
    public Decrease createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Decrease(context, holder, varChanges.get(var), newLabel);
    }

}
