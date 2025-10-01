package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Instruction implements Calculable
{
    protected InstructionData instructionData;
    protected final LabelInterface label; //MyLabel
    protected int maxDegree;
    protected final Variable var; // The var I'm "working" on
    protected Context context;
    protected S_Instruction holder;
    protected int lineIndex;

    public Instruction(InstructionData instructionData, Context context, S_Instruction holder, Variable var, LabelInterface label)
    {
        this.instructionData = instructionData;
        this.context = context;
        this.var = var;
        this.label = label;
        this.holder = holder;
    }

    @Override
    public abstract int calcMaxDegree();

    @Override
    public abstract LabelInterface execute();

    public abstract List<Instruction> getOneExpand();


    public LabelInterface getLabel()
    {
        return this.label;
    }

    @Override
    public int getCycles()
    {
        return this.instructionData.getCycles();
    }

    public String getName()
    {
        return this.instructionData.getName();
    }

    public S_Instruction getHolder() {return this.holder;}

    public InstructionData getInstructionData() {return instructionData;}

    public Variable getVar() {return this.var;}

    @Override
    public List<Variable> getUsedVariables()
    {
        return List.of(var);
    }

    public List<LabelInterface> getUsedLabels()
    {
        return List.of(label);
    }

    public int getLineIndex()
    {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex)
    {
        this.lineIndex = lineIndex;
    }

    public List<Variable> getChangedVariables() {return List.of();}

    //For the DTO
    public Optional<Variable> getArgIfExist()
    {
        return Optional.empty();
    }

    //For the DTO
    public Optional<Long> getConstantIfExist()
    {
        return Optional.empty();
    }

    //For the DTO
    public Optional<LabelInterface> getLabelToJumpIfExist()
    {
        return Optional.empty();
    }

    //For the DTO
    public Optional<String> getFuncUserInputIfExist()
    {
        return Optional.empty();
    }

    //For the DTO
    public Optional<String> getFuncArgsToDisplayIfExist()
    {
        return Optional.empty();
    }

    public abstract Instruction createCopy(Context context, S_Instruction holder,
                                              Map<Variable, Variable> varChanges,
                                              Map<LabelInterface, Label_Implement> labelChanges);


}

