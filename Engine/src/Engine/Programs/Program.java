package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Statistics.HistoryList;
import Engine.Vars.Variable;
import Out.FunctionSelectorChoiseDTO;

import java.util.*;
import java.util.stream.Collectors;

public class Program
{
    private String name;
    private final Context context;
    private int maxDegree;
    private List<Instruction> instructions;
    private List<Instruction> ExpandedInstructions;
    private List<Function> functions;
    private final HistoryList history = new HistoryList();
    private static final Map<String, Function> nameToFuncMap = new HashMap<>();

    //protected boolean isProgramAlreadyDoneMaxExpensions = false;

    //private static Program selectedProgram;

    public Program()
    {
        this.context = new Context();
        //Program.selectedProgram = this;
    }

    public static void clearNameToFuncMap() {nameToFuncMap.clear();}

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

    public void setFunctions(List<Function> functions)
    {
        this.functions = functions;
    }

    public List<Function> getFunctions()
    {
        return functions;
    }

    public int getMaxDegree()
    {
        return maxDegree;
    }

/*
    public boolean isProgramAlreadyDoneMaxExpensions()
    {
        return isProgramAlreadyDoneMaxExpensions;
    }

    public void setProgramAlreadyDoneMaxExpensions(boolean programAlreadyDoneMaxExpensions)
    {
        isProgramAlreadyDoneMaxExpensions = programAlreadyDoneMaxExpensions;
    }
*/

    public List<FunctionSelectorChoiseDTO> getFunctionsUserStringAndNames()
    {

        List<FunctionSelectorChoiseDTO> list = new ArrayList<>();
        list.add(new FunctionSelectorChoiseDTO(this.name, this.name));
        for (Function function : functions)
        {
            list.add(new FunctionSelectorChoiseDTO(function.getName(), function.getUserString()));
        }
        return list;
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

    public Function getFunctionByName(String name)
    {
        return nameToFuncMap.get(name);
    }

    public static String getUseStringByName(String name)
    {
        return nameToFuncMap.get(name).getUserString();
    }

    public boolean isNameFuncExistInMap(String name)
    {
        return nameToFuncMap.containsKey(name);
    }

    public void setFunctionInMap(Function function)
    {
        nameToFuncMap.put(function.getName(), function);
    }

    //Init program after file Loaded successfully.
    public void initProgram()
    {
/*        if (!isProgramAlreadyDoneMaxExpensions)
        {*/
            history.reset();
            InitInstructionsExpensions();
            //this.isProgramAlreadyDoneMaxExpensions = true;
            this.maxDegree = this.calcMaxDegree();
            if (this.functions != null) {this.functions.forEach(Program::initProgram);}

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



