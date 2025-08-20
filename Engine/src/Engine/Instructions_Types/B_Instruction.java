package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.List;

public abstract class B_Instruction extends Instruction
{
    public B_Instruction(String name, Program context, int cycles, Variable var, LabelInterface label)
    {
        super(name, context, cycles, var, label);
        this.maxDegree = this.calcMaxDegree();
    }


    @Override
    public int calcMaxDegree()
    {
        return 0;
    }

    public abstract String getInstructionRepresentation();

    @Override
    public abstract LabelInterface execute();


    @Override
    public List<Instruction> expand(int degree)
    {
        return List.of(this);
    }
}

