package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;

public class Jump_Equal_Variable extends S_Instruction
{
    private LabelInterface labelToJump;
    private Variable arg1;

    public Jump_Equal_Variable(Program context, Variable var, LabelInterface label, LabelInterface labelToJump, Variable arg1)
    {
        super("JUMP_EQUAL_VARIABLE", context, 2, var, label);
        this.labelToJump = labelToJump;
        this.arg1 = arg1;
    }

    public Jump_Equal_Variable(Program context, Variable var, LabelInterface labelToJump, Variable arg1)
    {
        this(context, var, FixedLabels.EMPTY, labelToJump, arg1);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] IF %s = %s GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                arg1.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if (value == context.getVarValue(arg1)) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {


        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable z_B = context.InsertVariableToEmptySpot(VariableType.WORK);;
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;

        LabelInterface label_A = context.InsertLabelToEmptySpot();
        LabelInterface label_B = context.InsertLabelToEmptySpot();
        LabelInterface label_C = context.InsertLabelToEmptySpot();

        List<Instruction> result = new ArrayList<>(List.of(
        new Assignment(context, z_A, this.var, this.label),
        new Assignment(context, z_B, this.arg1),
        new Jump_Zero(context, z_A, label_B, label_C),
        new Jump_Zero(context, z_B, label_A),
        new Decrease(context, z_A),
        new Decrease(context, z_B),
        new Goto_Label(context, z_FAKE, label_B),
        new Jump_Zero(context, z_B, label_C, this.labelToJump),
        new Neutral(context, Variable.OUTPUT, label_A)
        ));

        return result;
    }

}
