package Engine.Instructions_Types;

import Engine.Labels.LabelInterface;

import java.util.List;

public interface Calculable
{
    public int getCycles();
    public int calcMaxDegree();
    public int getMaxDegree();
    public LabelInterface execute();

    //public List<Instruction> getSingleExpansion();

    public List<Instruction> expand(int degree);
}
