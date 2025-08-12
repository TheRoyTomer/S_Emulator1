package Instructions_Types;

import Vars.Label;
import Vars.Receivable;

public abstract class B_Instruction extends Instruction
{
    public B_Instruction(Label label, Receivable input1)
    {
        super(label, input1);
        this.cycles = this.calcCycles();
        this.degree = this.calcDegree();
    }

    @Override
    public int calcDegree()
    {
        return 0;
    }

    @Override
    public abstract int calcCycles();

    @Override
    public abstract void calc();
}

