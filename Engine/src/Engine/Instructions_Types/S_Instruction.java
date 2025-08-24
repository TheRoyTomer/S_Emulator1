
package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class S_Instruction extends Instruction
{
    protected List<Instruction> instructions = new ArrayList<>();

    public S_Instruction(InstructionData instructionData, Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        super(instructionData, context, holder, var, label);
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
    public abstract void setSingleExpansion();

    public List<Instruction> getOneExpand()
    {
        return this.instructions;

    }
    public int getMaxDegree()
    {
        return this.maxDegree;
    }

    public void InitMaxExpansions()
    {
        this.setSingleExpansion();
        instructions.stream()
                .filter(S_Instruction.class::isInstance)
                .map(S_Instruction.class::cast)
                .forEach(S_Instruction::InitMaxExpansions);
    }

}

/*

public void foo()
{
    this.Instructions = getSingleExpansion();
    for (Instruction c : this. Instructions)
    {
        if (instruction instanceof S_Instruction)
        {
            c.foo();
        }
    }
}*/
