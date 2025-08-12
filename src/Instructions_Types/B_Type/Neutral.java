package Instructions_Types.B_Type;

import Instructions_Types.B_Instruction;
import Programs.Program;
import Vars.Label;
import Vars.Receivable;

public class Neutral extends B_Instruction
{

    public Neutral(Label label, Receivable input1)
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
        return 0;
    }

    @Override
    public void calc() {}
}
