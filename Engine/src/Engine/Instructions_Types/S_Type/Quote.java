package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.*;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Quote extends S_Instruction
{
    private final List<String> functionArguments;
    private final Function function;
    private int cycles;

    private ReadOnlyMainContextWrapper mainContextWrapper;
    private int argumentsCycles;

    public Quote(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function, LabelInterface label)
    {

        super(InstructionData.QUOTE, context, holder, var, label);
        this.functionArguments = functionArguments;
        this.function = function;
        this.cycles = 5;
    }

    public Quote(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function)
    {
        this(context, holder, var, functionArguments, function, FixedLabels.EMPTY);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] %s <- (%s,%s) ",
                this.lineIndex,
                _label,
                _var,
                function.getUserString(),
                functionArguments.toString());
    }

    public List<Long> getInputs()
    {
        return this.functionArguments.stream().map(this::convertInputToLong).toList();
    }

    public void setMainContextWrapper(ReadOnlyMainContextWrapper mainContextWrapper)
    {
        this.mainContextWrapper = mainContextWrapper;
    }

    public long convertInputToLong(String input)
    {

        if (Convertor.isArgVar(input))
        {
            if (this.mainContextWrapper != null) {
                Variable var = Convertor.convertStringToVar(input);
                long result = this.mainContextWrapper.getVarValueFromMainContext(var);
                return result;
            }
            Variable var = Convertor.convertStringToVar(input);
            long result = context.getVarValue(var);
            return result;
        }
        else
        {
            String relatedArgs;
            String relatedFuncName;
            Context tempContext = new Context();
            int ind = input.indexOf(',');
            if (ind == -1) {relatedArgs = ""; relatedFuncName = input;}
            else {relatedArgs = input.substring(ind + 1); relatedFuncName = input.substring(0, ind);}


            Function func = function.getFunctionByName(relatedFuncName);

            Quote instruction = new Quote(tempContext, null,
                    Variable.OUTPUT, Convertor.argsToStringList(relatedArgs), func);


            if (this.mainContextWrapper != null)
            {
                instruction.setMainContextWrapper(mainContextWrapper);
            }
            else
            {
                instruction.setMainContextWrapper(new ReadOnlyMainContextWrapper(this.context));
            }

            instruction.execute();
            this.argumentsCycles += instruction.getCycles() - 5;

            return tempContext.getVarValue(Variable.OUTPUT);
        }
    }
    @Override
    public List<Variable> getUsedVariables()
    {
        List<Variable> res = new ArrayList<>();
        res.add(this.var);
        res.addAll(functionArguments.stream()
                .flatMap(arg -> Convertor.extractVariables(arg).stream())
                .distinct()
                .map(Convertor::convertStringToVar)
                .toList());

        return res;

    }

    @Override
    public LabelInterface execute()
    {
        resetCycles();
        function.getContext().resetMapsState();
        function.getContext().insertInputsToMap(this.getInputs());

        List<Instruction> instructions = function.getInstructions();
        function.getContext().updateIndexLabels(instructions);

        int sumCycles = 5 + this.argumentsCycles;

        for (long PC = 0; PC < instructions.size();)
        {
            Instruction currentInstruction = instructions.get((int) PC);
            sumCycles += currentInstruction.getCycles();
            LabelInterface label = currentInstruction.execute();

            if (label == FixedLabels.EMPTY)
            {
                PC = PC + 1;
            } else if (label == FixedLabels.EXIT)
            {
                PC = instructions.size();
            } else {
                PC = function.getContext().getFromMapL((Label_Implement) label);
            }
        }
        context.setVarValue(var, function.getContext().getVarValue(Variable.OUTPUT));
        this.cycles = sumCycles;


        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        Map<Variable, Variable> variableChanges = new HashMap<>();
        Map<LabelInterface, Label_Implement> labelChanges = new HashMap<>();
        List<Instruction> funcInstructions = function.getInstructions();
        TreeSet<Variable> variables = function.getContext().getAllVarsInList(funcInstructions);
        List<LabelInterface> labels = context.getAll_L_InList(funcInstructions);
        this.instructions = new ArrayList<>();
        if (this.label != FixedLabels.EMPTY)
        {this.instructions.add(new Neutral(context, this, Variable.OUTPUT, this.label));}

        Iterator<String> iterator = functionArguments.iterator();

            for (Variable variable : variables)
            {
                Variable changedVar;
                if (variable.getVariableType() == VariableType.INPUT && iterator.hasNext())
                {
                    changedVar = context.InsertVariableToEmptySpot(VariableType.WORK);
                    String argument = iterator.next();
                    String tempArg;
                    int indexComma = argument.indexOf(',');
                    if (indexComma == -1) {tempArg = argument;}
                    else {tempArg = argument.substring(0, indexComma);}

                    if (function.isNameFuncExistInMap(tempArg))
                    {
                        String newFuncArgs = tempArg.length() == argument.length()
                                ? ""
                                : argument.substring(indexComma + 1);
                        List<String> res = Convertor.argsToStringList(newFuncArgs);
                        this.instructions.add(new Quote(context, this, changedVar, res, function.getFunctionByName(tempArg)));
                    }
                    else
                    {
                        Variable newVariable = Convertor.convertStringToVar(argument);
                        this.instructions.add(new Assignment(context, this, changedVar, newVariable));
                    }

                } else if (!context.isVariableExistInMap(variable))
                {
                    changedVar = variable;
                }
                else
                {
                    changedVar = context.InsertVariableToEmptySpot(VariableType.WORK);
                }
                variableChanges.put(variable, changedVar);
            }

        boolean isExitExists = false;
        for (LabelInterface label : labels)
        {
            if (label instanceof FixedLabels)
            {
                if (label == FixedLabels.EXIT) { isExitExists = true; }
            }
            else if (!context.isLabelExistInMap((Label_Implement) label))
            {
                labelChanges.put(label, (Label_Implement) label);
            }
            else
            {
                labelChanges.put(label, context.InsertLabelToEmptySpot());
            }
        }

        if (isExitExists) { labelChanges.put(FixedLabels.EXIT, context.InsertLabelToEmptySpot());}

        for (Instruction inst : funcInstructions)
        {
            this.instructions.add(inst.createCopy(context, this, variableChanges, labelChanges));
        }

        LabelInterface labelEnd = FixedLabels.EMPTY;
        if (isExitExists)
        {
            labelEnd = labelChanges.get(FixedLabels.EXIT);
        }
        this.instructions.add(new Assignment(context, this, this.var,variableChanges.get(Variable.OUTPUT), labelEnd));

    }

    //For the DTO
    @Override
    public Optional<String> getFuncUserInputIfExist()
    {
        return Optional.of(this.function.getUserString());
    }


    //For the DTO
    @Override
    public Optional<String> getFuncArgsToDisplayIfExist()
    {
        String argsCombined = this.functionArguments.stream()
                .map(arg -> Convertor.isArgVar(arg) ? arg : "(" + arg + ")")
                .collect(Collectors.joining(","));
        return Optional.of(function.changeFuncArgsToPrint(argsCombined));
    }


    @Override
    public Instruction createCopy(Context context, S_Instruction holder, Map<Variable, Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        List<String> newFuncArgs = new ArrayList<>();
        for (String arg : this.functionArguments)
        {
            newFuncArgs.add(function.changeOneArgNames(arg, varChanges));
        }

        return new Quote(context, holder, varChanges.get(this.var), newFuncArgs, function, newLabel);
    }

    public void resetCycles()
    {
        this.cycles = 5;
        this.argumentsCycles = 0;
    }



    public List<Variable> getChangedVariables() {return List.of(var);}

    @Override
    public int getCycles() {
        return cycles;
    }

}
