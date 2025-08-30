package Engine.Vars;

public interface Variable extends Comparable<Variable>
{
    String getVariableRepresentation();

    int getSerial();

    VariableType getVariableType();

    VariableImplement OUTPUT = new VariableImplement(VariableType.OUTPUT, 0);
}

