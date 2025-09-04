package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Statistics.HistoryList;

import java.util.*;

public class Program
{
    private String name;
    private final Context context;
    private int maxDegree;
    private List<Instruction> instructions;
    private List<Instruction> ExpandedInstructions;
    private final HistoryList history = new HistoryList();
    Convertor convertor;

    public Program()
    {
        this.context = new Context();
        convertor = new Convertor(context);
    }


    public void setExpandedInstructions(List<Instruction> expandedInstructions)
    {
        ExpandedInstructions = expandedInstructions;
    }

    public HistoryList getHistory() {return history;}

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


    //Init program after file Loaded successfully.
    public void initProgram()
    {
        history.reset();
        InitInstructionsExpensions();
        this.maxDegree = this.calcMaxDegree();
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











