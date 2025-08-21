package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends S_Instruction
{
   private final Variable arg1;

    public Assignment(Context context, Variable var, Variable arg1, LabelInterface label)
    {

        super(InstructionData.ASSIGNMENT, context, var, label);
        this.arg1 = arg1;
        this.instructions = this.getSingleExpansion();
    }

    public Assignment(Context context, Variable var, Variable arg1)
    {
        this(context, var, arg1, FixedLabels.EMPTY);
    }


    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] %s <- %s (%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                arg1.getVariableRepresentation(),
                instructionData.getCycles());

    }


    @Override
    public LabelInterface execute()
    {
        context.setVarValue(var, context.getVarValue(arg1));
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {
        Variable Z = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;
        LabelInterface label_A = context.InsertLabelToEmptySpot();
        LabelInterface label_B = context.InsertLabelToEmptySpot();
        LabelInterface label_C = context.InsertLabelToEmptySpot();

        List<Instruction> result = new ArrayList<>(List.of(
        new Zero_Variable(context,this.var, this.label),
        new JNZ(context, this.arg1, label_A),
        new Goto_Label(context, Z_FAKE,label_C),
        new Decrease(context, this.arg1, label_A),
        new Increase(context, Z),
        new JNZ(context, this.arg1, label_A),
        new Decrease(context, Z, label_B),
        new Increase(context, this.var),
        new Increase(context, this.arg1),
        new JNZ(context, Z, label_B),
        new Neutral(context, this.var, label_C)
        ));

        return result;
    }
}
