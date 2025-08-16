package Instructions_Types;

import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

public abstract class B_Instruction extends Instruction
{
    public B_Instruction(String name, Program holder, int cycles, Variable var, LabelInterface label)
    {
        super(name, holder, cycles, var, label);
        this.degree = this.calcDegree();
    }


    @Override
    public int calcDegree()
    {
        return 0;
    }

    public abstract String getInstructionRepresentation();

    @Override
    public abstract LabelInterface execute();


}

