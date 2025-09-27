package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Programs.Convertor;
import Engine.Programs.Function;
import Engine.Programs.ReadOnlyMainContextWrapper;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.*;
import java.util.stream.Collectors;

public class JumpEqualFunction extends S_Instruction
{

    private final List<String> functionArguments;
    private final Function function;
    private int cycles;
    private final LabelInterface labelToJump;

    private ReadOnlyMainContextWrapper mainContextWrapper;


    public JumpEqualFunction(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function, LabelInterface label, LabelInterface labelToJump)
    {

        super(InstructionData.JUMP_EQUAL_FUNCTION, context, holder, var, label);
        this.functionArguments = functionArguments;
        this.function = function;
        this.cycles = 0;
        this.labelToJump = labelToJump;
    }

    public JumpEqualFunction(Context context, S_Instruction holder, Variable var, List<String> functionArguments, Function function, LabelInterface labelToJump)
    {
        this(context, holder, var, functionArguments, function, FixedLabels.EMPTY,  labelToJump);
    }

    public void setMainContextWrapper(ReadOnlyMainContextWrapper mainContextWrapper)
    {
        this.mainContextWrapper = mainContextWrapper;
    }

    @Override
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.ofNullable(this.labelToJump);
    }


    @Override
    public LabelInterface execute()
    {
        Instruction quoteInst =  this.instructions.getFirst();
       quoteInst.execute();
        long funcResValue = this.function.getContext().getVarValue(Variable.OUTPUT);
        long varValue = this.context.getVarValue(this.var);
        if (funcResValue == varValue) {return this.labelToJump;}
        return FixedLabels.EMPTY;
    }

    @Override
    public void setSingleExpansion()
    {
        Variable z_A = context.InsertVariableToEmptySpot(VariableType.WORK);
        this.instructions = new ArrayList<>();
        this.instructions.add(new Quote(context, this, z_A, this.functionArguments, this.function, this.label));
        this.instructions.add((new Jump_Equal_Variable(context, this, this.var, this.label, this.labelToJump, z_A)));

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

        return new JumpEqualFunction(context, holder, varChanges.get(this.var), newFuncArgs, function, newLabel,labelChanges.get(this.labelToJump));
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
    public List<Variable> getChangedVariables() {return List.of(var);}

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

}
