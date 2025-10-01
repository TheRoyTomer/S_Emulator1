package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;
import Engine.Vars.Variable;

import java.util.List;

public interface Calculable
{
    int calcMaxDegree();
    LabelInterface execute();
    List<Variable> getUsedVariables();

    int getCycles();


    List<Instruction> getOneExpand();
}
