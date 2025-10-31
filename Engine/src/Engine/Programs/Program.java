package Engine.Programs;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Instructions_Types.S_Type.*;
import Engine.Statistics.ExecutionStatistics;
import Engine.Vars.Variable;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
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
    private static final Map<String, Function> nameToFuncMap = new HashMap<>();

    private ExecutionStatistics recentExecutionStatistics;

    public Program(Context context)
    {
        this.context = context;
    }

    public ExecutionStatistics getRecentExecutionStatistics()
    {
        return recentExecutionStatistics;
    }

    public void setRecentExecutionStatistics(int executeDegree, long finalYValue, List<Long> inputsVals, List<VariableDTO> variablesVals, int totalCycles)
    {
        this.recentExecutionStatistics = new ExecutionStatistics(
                executeDegree, finalYValue,
                inputsVals, variablesVals, totalCycles);
    }


    public Program()
    {
        this.context = new Context();
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        if (this.functions != null) {
            for (Function f : this.functions) {
                str.append("   ").append(f.toString()).append("\n");
            }
        }

        String contextIsNull = this.getContext() == null ? "null" : "NotNull";


        return "Program{" +
                "\nname= '" + name + '\'' +
                "\ncontext= " + contextIsNull +
                "functions= \n" + functions +
                '}';
    }

    public Program duplicate()
    {
        Program newP = new Program();
        newP.name = this.name;
        newP.maxDegree = this.maxDegree;

        newP.instructions = this.instructions.stream()
                .map(instruction -> duplicateInstruction(instruction, newP.getContext()))
                .collect(Collectors.toList());

        newP.functions = this.functions;

        return newP;
    }

    public Instruction duplicateInstruction(Instruction instruction, Context newContext)
    {
        return switch (instruction.getInstructionData()) {
            case DECREASE -> new Decrease(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel());
            case INCREASE -> new Increase(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel());
            case NEUTRAL -> new Neutral(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel());
            case JUMP_NOT_ZERO -> new JNZ(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get());
            case ZERO_VARIABLE -> new Zero_Variable(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel());
            case CONSTANT_ASSIGNMENT -> new Constant_Assignment(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getConstantIfExist().get());
            case JUMP_EQUAL_CONSTANT ->
                    new Jump_Equal_Constant(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get(), instruction.getConstantIfExist().get());
            case JUMP_EQUAL_VARIABLE ->
                    new Jump_Equal_Variable(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get(), instruction.getArgIfExist().get());
            case JUMP_ZERO -> new Jump_Zero(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get());
            case GOTO_LABEL ->
                    new Goto_Label(newContext, instruction.getHolder(), instruction.getVar(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get());
            case ASSIGNMENT -> new Assignment(newContext, instruction.getHolder(), instruction.getVar(), instruction.getArgIfExist().get(), instruction.getLabel());
            case QUOTE ->
                    new Quote(newContext, instruction.getHolder(), instruction.getVar(), ((Quote) instruction).getFuncArgs(), ((Quote) instruction).getFunction(), instruction.getLabel());
            case JUMP_EQUAL_FUNCTION ->
                    new JumpEqualFunction(newContext, instruction.getHolder(), instruction.getVar(), ((Quote) instruction).getFuncArgs(), ((Quote) instruction).getFunction(), instruction.getLabel(), instruction.getLabelToJumpIfExist().get());
        };
    }

    public static void clearNameToFuncMap()
    {
        nameToFuncMap.clear();
    }

    public void setExpandedInstructions(List<Instruction> expandedInstructions)
    {
        ExpandedInstructions = expandedInstructions;
    }


    public List<Instruction> getInstructions()
    {
        return instructions;
    }

    public List<Instruction> getExpandedInstructions()
    {
        return ExpandedInstructions;
    }

    public Context getContext()
    {
        return context;
    }

    public String getName()
    {
        return name;
    }

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

    public List<FunctionSelectorChoiseDTO> getFunctionsUserStringAndNames()
    {

        List<FunctionSelectorChoiseDTO> list = new ArrayList<>();
        list.add(new FunctionSelectorChoiseDTO(this.name, this.name));
        for (Function function : functions) {
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
        for (Instruction instruction : this.instructions) {
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
        InitInstructionsExpensions();
        //this.isProgramAlreadyDoneMaxExpensions = true;
        this.maxDegree = this.calcMaxDegree();
        if (this.functions != null) {
            this.functions.forEach(Program::initProgram);
        }

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

    public void setMaxDegree(int maxDegree)
    {
        this.maxDegree = maxDegree;
    }
}



