package Engine.Programs;

import Engine.Instructions_Types.Calculable;
import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.*;

import java.util.*;

public class Program implements Calculable
{
    private int degree;
    private int cycles;
    private List<Instruction> instructions = new ArrayList<>();
    private final Map<Integer, Long> MapForX = new HashMap<>();
    private final Map<Integer, Long> MapForZ = new HashMap<>();
    private final Map<Label_Implement, Long> MapForL = new HashMap<>();

    public Program(/*, List<Instruction> instructions*/)
    {
        this.degree = this.calcMaxDegree();
        this.cycles = this.calcCycles();
        //this.instructions = instructions;
    }

    public void addInstruction(Instruction instruction)
    {
        instructions.add(instruction);
    }

    @Override
    public String toString()
    {
       return ""; //ToDo: ??
    }

    public String getProgramRepresentation()
    {
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        for (Instruction c : instructions) {
            sb.append("#<" + counter + '>').append(c.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int getCycles()
    {
        return this.cycles;
    }

    @Override
    public int getMaxDegree()
    {
        return this.degree;
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

    public Map<Integer, Long> getrelevantMap(VariableImplement var)
    {
        if (var.getVariableType() == VariableType.INPUT)
        {
            return MapForX;
        }
        return MapForZ;
    }

    public long getVarValue(Variable var)
    {
        VariableType varType = var.getVariableType();

        switch(varType) {
            case VariableType.OUTPUT:
                return ((OutputWrapper)var).getValue(); //TODO: NOT ALWAYS WRAPPER
            case VariableType.INPUT:
                return MapForX.computeIfAbsent(var.getSerial(), k -> 0L);
            case VariableType.WORK:
                return MapForZ.computeIfAbsent(var.getSerial(), k -> 0L);
                default:
                    return -1L;

        }

    }


    public void setVarValue(Variable var, long value)
    {
        long fixedValue = Math.max(0, value);
        VariableType varType = var.getVariableType();

        switch(varType) {
            case VariableType.OUTPUT:
                ((OutputWrapper)var).setValue(fixedValue); //TODO: NOT ALWAYS WRAPPER
                break;

            case VariableType.INPUT:
                MapForX.put(var.getSerial(), fixedValue);
                break;

            case VariableType.WORK:
                MapForZ.put(var.getSerial(), fixedValue);
                break;

            default:


        }
    }


    public boolean isExistInMap(VariableImplement var)
    {

        VariableType varType = var.getVariableType();

        switch(varType) {
            case VariableType.INPUT:
                return MapForX.containsKey(var.getSerial());

            case VariableType.WORK:
                return MapForZ.containsKey(var.getSerial());

            default:
                return true;
                //IF NOT X AND NOT Z THAN Y AND WE ALWAYS HAVE Y


        }
    }


    @Override
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::getMaxDegree)
                .max()
                .orElse(-1) + 1; // keep original empty-case behavior
    }

    public int calcCycles()
    {
        return instructions.stream()
                .mapToInt(Instruction::getCycles)
                .sum();
    }

    private void CreateMapForL()
    {
        MapForL.clear();
        long rowCounter = 0;
        for (Instruction c : instructions)
        {
            //ToDo: Change With Stream
            if (c.getLabel() instanceof Label_Implement)
            {
                MapForL.put((Label_Implement) c.getLabel(), rowCounter);
            }
            rowCounter++;
        }
    }



    @Override
    public LabelInterface execute()
    {
         this.CreateMapForL();
        LabelInterface label = null;
        for (long PC = 0;PC < instructions.size();)
        {
            label = instructions.get((int)PC).execute();
            if (label == FixedLabels.EMPTY)
            {
                PC++;
            }
            else if (label == FixedLabels.EXIT || !isExistInMapL(label))
            {
                break;
            }
            else
            {
                PC = MapForL.get(label);
            }
        }
        return FixedLabels.EXIT;
    }

    public VariableImplement InsertVariableToEmptySpot(VariableType type)
    {
        Map<Integer, Long> relevantMap;
        if (type == VariableType.INPUT) {relevantMap  = MapForX;}
        else {relevantMap = MapForZ;}

        int availableKey = 0;
        while  (relevantMap.containsKey(availableKey)) {availableKey++;}

        VariableImplement res = new VariableImplement(type,availableKey);
        setVarValue(res, 0);
        return res;

    }

    public Label_Implement InsertLabelToEmptySpot()
    {
        int availableIndex = 1;
        Label_Implement res = new Label_Implement("L" + availableIndex);
        while (MapForL.containsKey((res)))
        {
            availableIndex++;
            res = new Label_Implement("L" + availableIndex);
        }
        //Label_Implement res = new Label_Implement("L" + availableIndex);
        setInMapL(res, 0); //Just to insert to map. Afterwards CreateMapForL will give true value.
        return res;
    }

    public List<Instruction> expand(int degree)
    {
        List<Instruction> res = new ArrayList<>();
        if (degree == 0) {return this.instructions;}
        for (Instruction instruction : instructions)
        {
            res.addAll(instruction.expand(degree));

        }
        return res;
    }
}


