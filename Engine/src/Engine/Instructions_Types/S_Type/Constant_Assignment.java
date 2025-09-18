package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;

public class Constant_Assignment extends S_Instruction
{
    private final long constant;

    public Constant_Assignment(Context context, S_Instruction holder, Variable var, LabelInterface label, long constant)
    {
        super(InstructionData.CONSTANT_ASSIGNMENT, context, holder, var, label);
        this.constant = constant;
    }

    public Constant_Assignment(Context context, S_Instruction holder, Variable var, long constant)
    {
        this(context, holder, var, FixedLabels.EMPTY, constant);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] %s <- %d",
                this.lineIndex,
                _label,
                _var, this.constant);
    }


    public List<Variable> getChangedVariables()
    {
        if (context.getVarValue(var) != constant) {return List.of(var);}
        return List.of();
    }




    public LabelInterface execute()
    {
        context.setVarValue(var, constant);
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        List<Instruction> result = new ArrayList<>();

        result.add(new Zero_Variable(context, this, this.var, this.label));

        result.addAll(LongStream.range(0, constant)
                .mapToObj(i -> new Increase(context, this, this.var))
                .toList()
        );
        this.instructions = result;
    }

    @Override
    public Optional<Long> getConstantIfExist()
    {
        return Optional.of(this.constant);
    }

    @Override
    public Constant_Assignment createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Constant_Assignment(context, holder, varChanges.get(var), newLabel, this.constant);
    }
}
