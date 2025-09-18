package Engine.Instructions_Types.S_Type;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Vars.Variable;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Assignment extends S_Instruction
{
   private final Variable arg1;

    public Assignment(Context context, S_Instruction holder, Variable var, Variable arg1, LabelInterface label)
    {

        super(InstructionData.ASSIGNMENT, context, holder, var, label);
        this.arg1 = arg1;
    }

    public Assignment(Context context, S_Instruction holder, Variable var, Variable arg1)
    {
        this(context, holder, var, arg1, FixedLabels.EMPTY);
    }

    //For debug
    @Override
    public String toString()
    {
        String _label = label != null ? label.getLabelRepresentation() : "Null";
        String _var = var != null ? var.getVariableRepresentation() : "Null";
        String _arg = arg1 != null ? arg1.getVariableRepresentation() : "Null";
        return String.format("#<%d> (S) [%s] %s <- %s",
                this.lineIndex,
                _label,
                _var, _arg);
    }



    @Override
    public List<Variable> getUsedVariables()
    {
        //Todo: Delete
        if (var == null || arg1 == null) {
            throw new IllegalStateException("Variable is null! var=" + var + ", arg1=" + arg1 + ", holder" + holder.getName() + holder.getLineIndex());
        }
        System.out.println(var.getVariableRepresentation() +"  "+ arg1.getVariableRepresentation()); //Todo: delete
        return List.of(var,  arg1);
    }

    public List<Variable> getChangedVariables()
    {
        if (context.getVarValue(var) != context.getVarValue(arg1)) {return List.of(var);}
        return List.of();
    }


    @Override
    public LabelInterface execute()
    {
        context.setVarValue(var, context.getVarValue(arg1));
        return FixedLabels.EMPTY;
    }



    @Override
    public void setSingleExpansion()
    {
        Variable Z = context.InsertVariableToEmptySpot(VariableType.WORK);
        Variable Z_FAKE = context.InsertVariableToEmptySpot(VariableType.WORK);
        LabelInterface label_A = context.InsertLabelToEmptySpot();
        LabelInterface label_B = context.InsertLabelToEmptySpot();
        LabelInterface label_C = context.InsertLabelToEmptySpot();

        this.instructions = new ArrayList<>(List.of(
        new Zero_Variable(context, this, this.var, this.label),
        new JNZ(context, this, this.arg1, label_A),
        new Goto_Label(context, this, Z_FAKE,label_C),
        new Decrease(context, this, this.arg1, label_A),
        new Increase(context, this, Z),
        new JNZ(context, this, this.arg1, label_A),
        new Decrease(context, this, Z, label_B),
        new Increase(context, this, this.var),
        new Increase(context, this, this.arg1),
        new JNZ(context, this, Z, label_B),
        new Neutral(context, this, this.var, label_C)
        ));
    }

    @Override
    public Optional<Variable> getArgIfExist()
    {
        return Optional.ofNullable(this.arg1);
    }

    @Override
    public Assignment createCopy(Context context, S_Instruction holder, Map<Variable,
            Variable> varChanges, Map<LabelInterface, Label_Implement> labelChanges)
    {
        LabelInterface newLabel;
        if(label == FixedLabels.EMPTY) {newLabel = FixedLabels.EMPTY;}
        else {newLabel = labelChanges.get(label);}
        return new Assignment(context, holder, varChanges.get(var), varChanges.get(arg1),newLabel);
    }
}
