package Engine.Programs;

import Engine.Instructions_Types.Calculable;
import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.*;

import java.util.*;

public class Program implements Calculable
{
    private final String name;
    private final Context context;
    private int maxDegree;
    private int cycles;
    private List<Instruction> instructions = new ArrayList<>();
    private List<Instruction> ExpandedInstructions;

    public Program(String name)
    {
        this.name = name;
        //ToDO: what else?
        this.context = new Context();
    }

    //ToDo:NEED?
    public Context getContext()
    {
        return context;
    }

    public void initProgram(List<Instruction> instructions)
    {
        this.instructions = instructions;
        this.maxDegree = this.calcMaxDegree();
        this.cycles = this.calcCycles();
    }

    //ToDo:NEED?
    public void addInstruction(Instruction instruction)
    {
        instructions.add(instruction);
    }

    @Override
    public String toString()
    {
        return "Program{" +
                "maxDegree=" + maxDegree +
                ", cycles=" + cycles +
                context.toString() +
                '}';
    }

    public String getProgramRepresentation()
    {
        int counter =1;
        String res = "";
        for (Instruction c : instructions)
        {
            res += String.format("#<%d> %s \n", counter++, c.getInstructionRepresentation());
        }
        return res;
    }

    public void setInstructions(List<Instruction> instructions)
    {
        this.instructions = instructions;
    }
    @Override
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::calcMaxDegree)
                .max()
                .orElse(0);
    }

    public int calcCycles()
    {
        return instructions.stream()
                .mapToInt(Instruction::getCycles)
                .sum();
    }


    @Override
    public LabelInterface execute()
    {
        context.updateLabelIndexes(this.ExpandedInstructions);
        LabelInterface label = null;
        for (long PC = 0; PC < ExpandedInstructions.size(); ) {
            label = ExpandedInstructions.get((int) PC).execute();
            if (label == FixedLabels.EMPTY) {
                PC++;
            } else if (label == FixedLabels.EXIT || !context.isExistInMapL(label)) {
                break;
            } else {
                PC = context.getFromMapL((Label_Implement) label);
            }
        }
        return FixedLabels.EXIT;
    }


    public List<Instruction> expand(int degree)
    {
        List<Instruction> res = new ArrayList<>();
        if (degree == 0)
        {
            return this.instructions;
        }
        for (Instruction instruction : instructions)
        {
            res.addAll(instruction.expand(degree));
        }
        return res;
    }

        public void initVarMap(Variable... vars)
    {
        for (Variable var : vars) {context.setVarValue(var,0);}
    }

    public void initLabelMap(LabelInterface... labels)
    {
        for (LabelInterface label : labels) {
            if (label instanceof Label_Implement)
            {
                context.setInMapL((Label_Implement) label, 0);
            }
        }
    }

    /*public void getNewExpandInstructionList(int degree)
    {
        //this.initProgram(expand(degree));
        this.ExpandedInstructions = expand(degree);
    }*/
}




