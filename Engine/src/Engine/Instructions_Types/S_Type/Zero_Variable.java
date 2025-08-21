package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Zero_Variable extends S_Instruction
{

    public Zero_Variable(Context context, Variable var, LabelInterface label)
    {
        super(InstructionData.ZERO_VARIABLE, context, var, label);
        this.instructions = this.getSingleExpansion();
    }

    public Zero_Variable(Context context, Variable var)
    {
        this(context, var, FixedLabels.EMPTY);
    }

    @Override
    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] %s <- 0 (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                instructionData.getCycles());

    }


    @Override
    public LabelInterface execute()
    {
        context.setVarValue(var, 0);
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {
        List<Instruction> result = new ArrayList<>();
        LabelInterface labelFirstRow = this.label;
        if (Objects.equals(label.getLabelRepresentation(), FixedLabels.EMPTY.getLabelRepresentation()))
        {
            labelFirstRow = context.InsertLabelToEmptySpot();

        }

        result.add(new Decrease(context, this.var,labelFirstRow));
        result.add(new JNZ(context,this.var, labelFirstRow));
        return result;
    }
}
