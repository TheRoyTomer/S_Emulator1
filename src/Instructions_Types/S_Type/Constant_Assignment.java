package Instructions_Types.S_Type;

import Instructions_Types.B_Type.Increase;
import Instructions_Types.Calculable;
import Instructions_Types.Instruction;
import Instructions_Types.S_Instruction;
import Programs.Program;
import Vars.Label;
import Vars.Receivable;

import java.util.ArrayList;
import java.util.List;

public class Constant_Assignment extends S_Instruction
{
    public Constant_Assignment(Label label, Receivable input1, Receivable input2)
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
        return 2;
    }

    @Override
    public void calc()
    {
        int keyFirst = inputs.getFirst().getValue();
        int constant = inputs.get(1).getValue();
        if(constant < 0) {constant = 0;}
        Program.setInMapX(keyFirst, constant);
    }

    @Override
    public List<Instruction> BuildInstructions()
    {
        List<Instruction> result = new ArrayList<>();
        Receivable keyFirst = inputs.getFirst();
        int constant = inputs.get(1).getValue();
        result.add(new Zero_Variable(null, keyFirst));
        for (int i = 0; i < constant; i++)
        {
            result.add(new Increase(null, keyFirst));
        }
        return result;
    }
}
