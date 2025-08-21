package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.*;

public class Context
{
    private final Map<Variable, Long> MapForX = new HashMap<>();
    private final Map<Variable, Long> MapForZ = new HashMap<>();
    private final Map<Label_Implement, Long> MapForL = new HashMap<>();
    private final Map<Variable, Long> Y = new HashMap<>();
    Set<Variable> usedVars = new TreeSet<>();


    public Context()
    {
        Y.put(Variable.OUTPUT, 0L);
    }

    @Override
    public String toString()
    {
        return "Context{" +
                "MapForX=" + MapForX +
                ", MapForZ=" + MapForZ +
                ", MapForL=" + MapForL +
                ", Y=" + getVarValue(Variable.OUTPUT) +
                '}';
    }

    public long getFromMapL(Label_Implement label)
    {
        Long value = MapForL.computeIfAbsent(label, k -> 0L);
        return value;
    }

    public void setInMapL(Label_Implement label, long value)
    {
        MapForL.put(label, Math.max(0, value));
    }

    public Boolean isExistInMapL(LabelInterface key)
    {
        return MapForL.containsKey(key);
    }

    public Map<Variable, Long> getrelevantMap(VariableType type)
    {
        return switch (type) {
            case INPUT -> MapForX;
            case WORK -> MapForZ;
            case OUTPUT -> Y;
            default -> null; //TODO: Handle with exceptions
        };
    }

    public long getVarValue(Variable var)
    {
        VariableType varType = var.getVariableType();

        return switch (varType) {
            case VariableType.OUTPUT -> Y.get(Variable.OUTPUT);
            case VariableType.INPUT -> MapForX.computeIfAbsent(var, k -> 0L);
            case VariableType.WORK -> MapForZ.computeIfAbsent(var, k -> 0L);
            default -> -1L; //TODO: Handle with exceptions

        };

    }


    public void setVarValue(Variable var, long value)
    {
        long fixedValue = Math.max(0, value);
        VariableType varType = var.getVariableType();

        switch (varType) {
            case VariableType.OUTPUT:
                Y.put(Variable.OUTPUT, fixedValue);
                break;

            case VariableType.INPUT:
                MapForX.put(var, fixedValue);
                break;

            case VariableType.WORK:
                MapForZ.put(var, fixedValue);
                break;

            default:


        }
    }


    public boolean isExistInMap(VariableImplement var)
    {

        VariableType varType = var.getVariableType();

        return switch (varType) {
            case VariableType.INPUT -> MapForX.containsKey(var);
            case VariableType.WORK -> MapForZ.containsKey(var);
            case VariableType.OUTPUT -> true; //We Always Have Y.

            default -> true; //TODO: Handle with exceptions


        };
    }

    public void collectVarsAndIndexLabels(List<Instruction> instructions)
    {
        usedVars.clear();
        MapForL.clear();
        long rowCounter = 0;
        for (Instruction c : instructions) {
            usedVars.addAll(c.getUsedVariables());
            if (c.getLabel() instanceof Label_Implement) {
                MapForL.put((Label_Implement) c.getLabel(), rowCounter);
            }
            rowCounter++;
        }
    }

    public VariableImplement InsertVariableToEmptySpot(VariableType type)
    {
        //TODO: Handle with exceptions if type is output
        Map<Variable, Long> relevantMap = getrelevantMap(type);
        int availableIndex = 1;
        VariableImplement res = new VariableImplement(type, availableIndex);
        while (relevantMap.containsKey((res))) {
            availableIndex++;
            res = new VariableImplement(type, availableIndex);
        }
        setVarValue(res, 0); //Just to insert to map. Afterwards updateLabelIndexes will give true value.
        return res;
    }

    public Label_Implement InsertLabelToEmptySpot()
    {
        int availableIndex = 1;
        Label_Implement res = new Label_Implement("L" + availableIndex);
        while (MapForL.containsKey((res))) {
            availableIndex++;
            res = new Label_Implement("L" + availableIndex);
        }
        setInMapL(res, 0); //Just to insert to map. Afterwards updateLabelIndexes will give true value.
        return res;
    }

    public List<Variable> getUsedVarsInOrder()
    {
        return usedVars.stream().toList();
    }


}

