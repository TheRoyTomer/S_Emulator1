package Instructions_Types;

import Vars.Label;
import Vars.Receivable;

import java.util.ArrayList;
import java.util.List;

public abstract class S_Instruction extends Instruction
{
    protected List<Instruction> instructions = new ArrayList<>();

    public S_Instruction(Label label, Receivable input1)
    {
        super(label, input1);
        this.cycles = this.calcCycles();
        this.degree = this.calcDegree();
        this.instructions = this.BuildInstructions();
    }


    public S_Instruction(Label label, Receivable input1, Receivable input2)
    {
        super(label, input1,  input2);
        this.cycles = this.calcCycles();
        this.degree = this.calcDegree();
    }



    @Override
    public int calcDegree()
    {
        int max = -1;
        for (Instruction c : instructions)
        {
            if (max < c.getDegree())
            {
                max = c.getDegree();
            }
        }
        return max + 1;
    }

    @Override
    public abstract int calcCycles();

    @Override
    public abstract void calc();
    public abstract List<Instruction> BuildInstructions();
}


