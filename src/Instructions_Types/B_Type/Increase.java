package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Programs.Program;
import Vars.Label;
import Vars.Receivable;

public class Increase extends B_Instruction
{
    public Increase(Label label, Receivable input1)
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
        Program.setInMapX(key, Program.getFromMapX(key) + 1);
    }
}
