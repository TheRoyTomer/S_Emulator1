package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Programs.Convertor;
import Engine.Programs.Function;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.*;
import java.util.List;

public class Quote extends S_Instruction
{
    private final List<String> functionArguments;
    private final Function function;
    private int cycles;

    public Quote(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function, LabelInterface label)
    {

        super(InstructionData.QUOTE, context, holder, var, label);
        this.functionArguments = functionArguments;
        this.function = function;
        this.cycles = 0;
    }

    public Quote(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function)
    {
        this(context, holder, var, functionArguments, function, FixedLabels.EMPTY);
    }

    public List<Long> getInputs()
    {
        return this.functionArguments.stream().map(this::convertInputToLong).toList();
    }

    public long convertInputToLong(String input)
    {
        return context.getVarValue(Convertor.convertStringToVar(input));
    }

    @Override
    public List<Variable> getUsedVariables()
    {
        return function.getContext().getAllVarsInList(function.getInstructions()).stream().toList();
    }

    //Todo: Implement
    @Override
    public LabelInterface execute()
    {
        function.getContext().resetMapsState();
        function.getContext().insertInputsToMap(this.getInputs());

        List<Instruction> instructions = function.getInstructions();
        function.getContext().updateIndexLabels(instructions);

        int sumCycles = 0;
        for (long PC = 0; PC < instructions.size();)
        {
            Instruction currentInstruction = instructions.get((int) PC);
            sumCycles += currentInstruction.getCycles();
            System.out.println(currentInstruction.getLineIndex() + currentInstruction.getName());
            LabelInterface label = currentInstruction.execute();
            System.out.println(label.getLabelRepresentation());

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
        TreeSet<Variable> variables = context.getAllVarsInList(funcInstructions);
        List<LabelInterface> labels = context.getAll_L_InList(funcInstructions);
        this.instructions = new ArrayList<>();
        Iterator<String> iterator = functionArguments.iterator();
        for (Variable variable : variables)
        {
            Variable changedVar;
            if (variable.getVariableType() == VariableType.INPUT && iterator.hasNext())
            {
                changedVar = Convertor.convertStringToVar(iterator.next());
                this.instructions.add(new Assignment(context, this, changedVar, variable));
            }
            else if(!context.isVariableExistInMap(variable))
            {
                changedVar = variable;
            }
            else
            {
                changedVar = context.InsertVariableToEmptySpot(VariableType.WORK);
            }

            variableChanges.put(variable, changedVar);
            /*if (!context.isVariableExistInMap(variable))
            {
                variableChanges.put(variable, variable);
            }
            else
            {
                variableChanges.put(variable, context.InsertVariableToEmptySpot(VariableType.WORK));
            }*/
        }

        /*TreeSet<Variable> xList = context.getAll_X_InList(funcInstructions);
        Iterator<Variable> xLstIter = xList.iterator();

        for(String arg :functionArguments)
        {
            Variable argVar =
            Variable xVar = xLstIter.next();
            variableChanges.put(xVar, argVar);
            this.instructions.add(new Assignment(context, this, variableChanges.get(xVar), argVar));//???
        }*/

        boolean isExitExists = false;
        for (LabelInterface label : labels)
        {
            if (label instanceof FixedLabels)
            {
                if (label == FixedLabels.EXIT)
                {
                    isExitExists = true;
                }
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
        Variable outputVar = variableChanges.get(Variable.OUTPUT);
        if (outputVar == null) System.out.println("no output");
        System.out.println("Output variable mapping: " + outputVar.getVariableRepresentation());
        Variable newVar = variableChanges.get(Variable.OUTPUT);
        this.instructions.add(new Assignment(context, this, this.var,newVar));


        if (isExitExists)
        {
            this.instructions.add(new Neutral(context, this, Variable.OUTPUT, labelChanges.get(FixedLabels.EXIT)));
        }
    }

    //For the DTO
    @Override
    public Optional<String> getFuncUserInputIfExist()
    {
        return Optional.of(this.function.getUserString());
    }


    //For the DTO
    @Override
    public Optional<String> getFuncArgsIfExist() {return Optional.of(String.join( ",", this.functionArguments));}

    //TODO: Implement
    @Override
    public Instruction createCopy(Context context, S_Instruction holder, Map<Variable, Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        System.out.println("Implement that you fool");
        return null;
    }

    public List<Variable> getChangedVariables() {return List.of(var);}
}
