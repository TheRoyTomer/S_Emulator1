package Engine.Vars;

import java.util.List;

public interface Variable extends Comparable<Variable>
{
    public String getVariableRepresentation();

    public int getSerial();

    public VariableType getVariableType();

    VariableImplement OUTPUT = new VariableImplement(VariableType.OUTPUT, 0);
}
