package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Vars.Variable;

import java.util.List;

public interface Calculable
{
    public int calcMaxDegree();
    public LabelInterface execute();
    public List<Variable> getUsedVariables();


    public List<Instruction> getOneExpand();
}
