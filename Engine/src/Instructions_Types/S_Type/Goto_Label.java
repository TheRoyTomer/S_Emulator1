package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public class Goto_Label extends S_Instruction
{
    LabelInterface labelToJump;

    public Goto_Label(Program holder, LabelInterface label, LabelInterface labelToJump)
    {
        super("GOTO_LABEL", holder,1, null, label);
        this.labelToJump = labelToJump;
    }

    public Goto_Label(Program holder, LabelInterface labelToJump)
    {
       this(holder, FixedLabels.EMPTY, labelToJump);

    }

    @Override
    public String getInstructionRepresentation()
    {
        return String.format("(B)[%s] GOTO %s (%d)",
                label.getLabelRepresentation(),
                labelToJump.getLabelRepresentation(),
                cycles);

    }

    @Override
    public LabelInterface execute()
    {
        return labelToJump;
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: BUILD FUNCTION
        List<Instruction> result = new ArrayList<>();
        return result;
    }


}
