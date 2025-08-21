package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;

import java.util.List;

public interface Calculable
{
    public int calcMaxDegree();
    public LabelInterface execute();

    public List<Instruction> expand(int degree);
}
