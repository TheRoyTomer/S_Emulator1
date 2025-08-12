package Instructions_Types.S_Type;

import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Programs.Program;
import Vars.Label;
import Vars.Receivable;

import java.util.ArrayList;
import java.util.List;

public class Zero_Variable extends S_Instruction
{
    public Zero_Variable(Label label, Receivable input1)
    {
        super(label, input1);
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
        return 1;
    }

    @Override
    public void calc()
    {
        int key =  inputs.getFirst().getValue();
        Program.setInMapX(key, 0);
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        //ToDo: Build function
        List<Instruction> result = new ArrayList<>();
        return result;
    }
}
