package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Program;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class S_Instruction extends Instruction
{
    protected List<Instruction> instructions = new ArrayList<>();

    public S_Instruction(String name, Program context, int cycles, Variable var, LabelInterface label)
    {
        super(name, context, cycles, var, label);
        this.maxDegree = this.calcMaxDegree();
        this.instructions = this.getSingleExpansion();
    }


    @Override
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::getMaxDegree)
                .max()
                .orElse(-1) + 1; // keep original empty-case behavior
    }

    @Override
    public abstract LabelInterface execute();
    public abstract List<Instruction> getSingleExpansion();

    public List<Instruction> expand(int degree)
    {
        if (degree == 0) {return List.of(this);}
        else if (degree == 1)
        {
            return this.getSingleExpansion();
        }
        else
        {
            List<Instruction> expandedInstructions = this.getSingleExpansion();
            List<Instruction> result = new ArrayList<>();
            for (Instruction instruction : expandedInstructions)
            {
                result.addAll(instruction.expand(degree - 1));
            }
            return result;
        }
    }


}


