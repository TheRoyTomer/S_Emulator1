package Engine.Vars;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Labels.LabelInterface;
import Engine.Programs.Context;

import java.util.ArrayList;
import java.util.List;

public interface Variable extends Comparable<Variable>
{
    public String getVariableRepresentation();

    public int getSerial();

    public VariableType getVariableType();

    VariableImplement OUTPUT = new VariableImplement(VariableType.OUTPUT, 0);
}

