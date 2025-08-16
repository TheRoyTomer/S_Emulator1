package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Jump_Zero extends S_Instruction
{
    private LabelInterface labelToJump;

    public Jump_Zero(Program holder, Variable var, LabelInterface label, LabelInterface labelToJump)
    {
        super("JUMP_ZERO", holder, 2, var, label);
        this.labelToJump = labelToJump;
    }

    public Jump_Zero(Program holder, Variable var, LabelInterface labelToJump)
    {
        this(holder, var, FixedLabels.EMPTY, labelToJump);
    }

    @Override
    public String toString()
    {
        //ToDo: add data
        return "";
    }


    public String getInstructionRepresentation()
    {
        return String.format("(B) [%s] IF %s=0 GOTO %s(%d)",
                label.getLabelRepresentation(),
                var.getVariableRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        long value = var.getValue();
        if(value == 0) return labelToJump;
        return FixedLabels.EMPTY;
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: BUILD FUNCTION
        List<Instruction> result = new ArrayList<>();
        return result;
    }
}
