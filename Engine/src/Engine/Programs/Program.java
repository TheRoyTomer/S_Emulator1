package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Statistics.StatisticsList;

import java.util.*;

public class Program
{
    private String name;
    private final Context context;
    private int maxDegree;
    private List<Instruction> instructions;/* = new ArrayList<>();*/

    public void setExpandedInstructions(List<Instruction> expandedInstructions)
    {
        ExpandedInstructions = expandedInstructions;
    }

    private List<Instruction> ExpandedInstructions;
    private StatisticsList instructionsStats;

    //ToDo: delete?
    private List<Long> inputs = new ArrayList<>();

    public Program()
    {
        //ToDO: what else?
        this.context = new Context();
        this.instructionsStats = new StatisticsList();
    }

    public List<Instruction> getInstructions() {return instructions;}

    public List<Instruction> getExpandedInstructions() {return ExpandedInstructions;}

    public Context getContext()
    {
        return context;
    }

    public String getName() {return name;}

    public void setName(String name)
    {
        this.name = name;
    }

    public void InitInstructionsExpensions()
    {
        instructions.stream()
                .filter(S_Instruction.class::isInstance)
                .map(S_Instruction.class::cast)
                .forEach(S_Instruction::InitMaxExpansions);

        setListIndices(1, this.instructions);

    }

    public void setListIndices(int startIndex, List<Instruction> lst)
    {
        for (Instruction instruction : this.instructions)
        {
            instruction.setLineIndex(startIndex);
            startIndex++;
        }
    }


    public void initProgram()
    {
        context.clearMaps();
        InitInstructionsExpensions();
        this.maxDegree = this.calcMaxDegree();
    }

    @Override
    public String toString()
    {
        return "Program{" +
                "maxDegree=" + maxDegree +
                ", " + context.toString() +
                '}';
    }



    public void setInstructions(List<Instruction> instructions)
    {
        this.instructions = instructions;
    }
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::calcMaxDegree)
                .max()
                .orElse(0);
    }

    public List<Instruction> getProperListByDegree(int degree)
    {
        return degree == 0
                ? this.instructions
                : this.ExpandedInstructions;
    }







}











