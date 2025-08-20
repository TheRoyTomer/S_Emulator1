package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.JNZ;
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

public class Jump_Zero extends S_Instruction
{
    private LabelInterface labelToJump;

    public Jump_Zero(Program context, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super("JUMP_ZERO", context, 2, var, label);
        this.labelToJump = labelToJump;
        this.instructions = this.getSingleExpansion();
        //this.maxDegree = this.calcMaxDegree();
    }

    public Jump_Zero(Program context, Variable var, LabelInterface labelToJump)
    {
        this(context, var, FixedLabels.EMPTY, labelToJump);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(S) [%s] IF %s=0 GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = context.getVarValue(var);
        if(value == 0) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> getSingleExpansion()
    {
        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);;
        Variable z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);;

        LabelInterface label_A = context.InsertLabelToEmptySpot();

        List<Instruction> result = new ArrayList<>(List.of(
        new JNZ(context,this.var, label_A, this.label),
        new Goto_Label(context, z_FAKE, this.labelToJump),
        new Neutral(context, Variable.OUTPUT, label_A)
        ));

        //instructions = result;
        return result;



    }
}
