package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;
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


    @Override
    /*public String getInstructionRepresentation()
    {
        return String.format("#<%d>(S) [%s] %s <- %d (%d)",
                this.lineIndex,
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                constant,
                instructionData.getCycles());

    }*/


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
}
