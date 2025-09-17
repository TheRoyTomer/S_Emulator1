package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.*;
import java.util.stream.Collectors;

public class  Context
{
    private final Map<Variable, Long> MapForX = new HashMap<>();
    private final Map<Variable, Long> MapForZ = new HashMap<>();
    private final Map<Label_Implement, Long> MapForL = new HashMap<>();
    private final Map<Variable, Long> Y = new HashMap<>();


    public Context()
    {
        Y.put(Variable.OUTPUT, 0L);
    }

    public void insertInputsToMap(List<Long> inputs)
    {
        int counter = 1;
        for (Long input : inputs)
        {
            setVarValue(new VariableImplement(VariableType.INPUT, counter), input);
            counter++;
        }
    }

    public boolean isVariableExistInMap(Variable var)
    {
        return switch (var.getVariableType()){
            case OUTPUT -> true;
            case INPUT -> MapForX.containsKey(var);
            case WORK -> MapForZ.containsKey(var);
        };
    }

    public boolean isLabelExistInMap(Label_Implement label)
    {
        return MapForL.containsKey(label);
    }


    public long getFromMapL(Label_Implement label)
    {
        return MapForL.computeIfAbsent(label, k -> 0L);
    }

    public void setInMapL(Label_Implement label, long value)
    {
        MapForL.put(label, Math.max(0, value));
    }

    public Map<Variable, Long> getrelevantMap(VariableType type)
    {
        return switch (type) {
            case INPUT -> MapForX;
            case WORK -> MapForZ;
            case OUTPUT -> Y;
        };
    }

    public long getVarValue(Variable var)
    {
        VariableType varType = var.getVariableType();

        return switch (varType) {
            case VariableType.OUTPUT -> Y.get(Variable.OUTPUT);
            case VariableType.INPUT -> MapForX.computeIfAbsent(var, k -> 0L);
            case VariableType.WORK -> MapForZ.computeIfAbsent(var, k -> 0L);

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
        }
    }

    //Find the row number for the labels in the list, and insert the row number as value to map.
    public void updateIndexLabels(List<Instruction> instructions)
    {
        MapForL.clear();
        long rowCounter = 0;
        for (Instruction c : instructions) {
            if (c.getLabel() instanceof Label_Implement) {
                MapForL.put((Label_Implement) c.getLabel(), rowCounter);
            }
            rowCounter++;
        }
    }

    //Finds the minimal serial for variable that hasn't been used.
    public VariableImplement InsertVariableToEmptySpot(VariableType type)
    {
        Map<Variable, Long> relevantMap = getrelevantMap(type);
        int availableIndex = 1;
        VariableImplement res = new VariableImplement(type, availableIndex);
        while (relevantMap.containsKey((res))) {
            availableIndex++;
            res = new VariableImplement(type, availableIndex);
        }
        setVarValue(res, 0); //Just to insert to map. Afterward updateLabelIndexes will give true value.
        return res;
    }

    //Finds the minimal serial for label that hasn't been used.
    public Label_Implement InsertLabelToEmptySpot()
    {
        int availableIndex = 1;
        Label_Implement res = new Label_Implement("L" + availableIndex);
        while (MapForL.containsKey((res))) {
            availableIndex++;
            res = new Label_Implement("L" + availableIndex);
        }
        setInMapL(res, 0); //Just to insert to map. Afterward updateLabelIndexes will give true value.
        return res;
    }

    public void clearMaps()
    {
        MapForL.clear();
        MapForX.clear();
        MapForZ.clear();
        Y.put(Variable.OUTPUT, 0L);
    }

    public void resetMapsState()
    {
        MapForL.replaceAll((k, v) -> 0L);
        MapForX.clear();
        setVarValue(Variable.OUTPUT, 0L);
        MapForZ.replaceAll((k, v) -> 0L);
    }

    public TreeSet<Variable> getAll_X_InList(List<Instruction> instructions)
    {
        return instructions.stream()
                .flatMap(inst -> inst.getUsedVariables().stream())
                .filter(v -> v.getVariableType() == VariableType.INPUT)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<Variable> getAllVarsInList(List<Instruction> instructions)
    {
        return instructions.stream()
                .flatMap(inst -> inst.getUsedVariables().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<LabelInterface> getAll_L_InList(List<Instruction> instructions)
    {
        boolean isExitExist = false;
        Set<Label_Implement> All_True_Labels = new TreeSet<>();
        for (Instruction inst : instructions) {

            for (LabelInterface label : inst.getUsedLabels()) {
                if (label instanceof Label_Implement) {
                    All_True_Labels.add((Label_Implement) label);
                } else if (label == FixedLabels.EXIT) {
                    isExitExist = true;
                }
            }
        }
        List<LabelInterface> res = new ArrayList<>(All_True_Labels);
        if (isExitExist) {
            res.add(FixedLabels.EXIT);
        }

        return res;
    }

    //Todo: delete!! just for debugging
    public void printAllMaps()
    {
        System.out.println("X-MAP: " + MapForX + "\n");
        System.out.println("Z-MAP: " + MapForZ + "\n");
        System.out.println("L-MAP: " + MapForL + "\n");
        System.out.println("Y: " + Y + "\n");
    }


}
