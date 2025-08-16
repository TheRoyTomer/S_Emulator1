package Instructions_Types;

import Labels.LabelInterface;
import Programs.Program;
import Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class S_Instruction extends Instruction
{
    protected List<Instruction> instructions = new ArrayList<>();

    public S_Instruction(String name, Program holder, int cycles, Variable var, LabelInterface label)
    {
        super(name, holder, cycles, var, label);
        this.degree = this.calcDegree();
    }


    @Override
    public int calcDegree()
    {
        //ToDo: Implement with stream
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
    public abstract LabelInterface execute();
    public abstract List<Instruction> BuildInstructions();
}


