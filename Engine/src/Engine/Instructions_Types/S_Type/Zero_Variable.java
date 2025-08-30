package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Zero_Variable extends S_Instruction
{

    public Zero_Variable(Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(InstructionData.ZERO_VARIABLE, context, holder, var, label);
    }

    public Zero_Variable(Context context, S_Instruction holder, Variable var)
    {
        this(context, holder, var, FixedLabels.EMPTY);
    }

    @Override
    public LabelInterface execute()
    {
        context.setVarValue(var, 0);
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        LabelInterface labelFirstRow = this.label;
        if (Objects.equals(label.getLabelRepresentation(), FixedLabels.EMPTY.getLabelRepresentation()))
        {
            labelFirstRow = context.InsertLabelToEmptySpot();

        }

        this.instructions = new ArrayList<>(List.of(
            new Decrease(context, this, this.var,labelFirstRow),
            new JNZ(context, this, this.var, labelFirstRow)
        ));

    }

}
