package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Programs.Program;
import Vars.Label;
import Vars.Receivable;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends S_Instruction
{
    public Assignment(Label label, Receivable input1, Receivable input2)
    {
        super(label, input1, input2);
    }

    @Override
    public String toString()
    {
        //ToDo: String format this bitch
        return "";
    }

    @Override
    public int calcCycles()
    {
        return 4;
    }

    @Override
    public void calc()
    {
        int keyFirst = inputs.getFirst().getValue();
        int keySecond = inputs.get(1).getValue();
        Program.setInMapX(keyFirst, Program.getFromMapX(keySecond));
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: Build function
        List<Instruction> result = new ArrayList<>();
        return result;
    }
}
