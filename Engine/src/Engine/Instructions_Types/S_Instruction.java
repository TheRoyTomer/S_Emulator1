package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class S_Instruction extends Instruction
{
    protected List<Instruction> instructions = new ArrayList<>();

    public S_Instruction(InstructionData instructionData, Context context, Variable var, LabelInterface label)
    {
        super(instructionData, context, var, label);
        this.maxDegree = this.calcMaxDegree();
    }


    @Override
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::calcMaxDegree)
                .max()
                .orElse(-1) + 1;
    }

    @Override
    public abstract LabelInterface execute();
    public abstract void getSingleExpansion();

    public List<Instruction> expand(int degree)
    {
        if (degree == 0) {return List.of(this);}
        else if (degree == 1)
        {
            return this.instructions;
        }
        else
        {
            List<Instruction> expandedInstructions = this.instructions;
            List<Instruction> result = new ArrayList<>();
            for (Instruction instruction : expandedInstructions)
            {
                result.addAll(instruction.expand(degree - 1));
            }
            return result;
        }
    }
    public int getMaxDegree()
    {
        return this.maxDegree;
    }

}


