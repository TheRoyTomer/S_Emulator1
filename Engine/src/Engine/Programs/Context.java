package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context
{
    private final Map<Integer, Long> MapForX = new HashMap<>();
    private final Map<Integer, Long> MapForZ = new HashMap<>();
    private final Map<Label_Implement, Long> MapForL = new HashMap<>();
    private final Map<Variable, Long> Y = new HashMap<>();

   /* private final Map<Integer, Long> InitMapX;
    private final Map<Integer, Long> InitMapZ;
    private final Map<Label_Implement, Long> InitMapL;*/

    public Context(/*Map<Integer, Long> userX,
                   Map<Integer, Long> userZ,
                   Map<Label_Implement, Long> userL*/)
    {
        /*this.InitMapX = userX;
        this.InitMapZ = userZ;
        this.InitMapL = userL;*/
        Y.put(Variable.OUTPUT, 0L);
    }

    @Override
    public String toString()
    {
        return "Context{" +
                "MapForX=" + MapForX +
                ", MapForZ=" + MapForZ +
                ", MapForL=" + MapForL +
                ", Y=" +getVarValue(Variable.OUTPUT) +
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

    public Map<?, Long> getrelevantMap(VariableImplement var)
    {
        VariableType type = var.getVariableType();
        switch (type){
            case INPUT:
                return MapForX;
            case WORK:
                return MapForZ;
            case OUTPUT:
                return Y;

            default:
                return null; //TODO: Handle with exceptions
        }
    }

    public long getVarValue(Variable var)
    {
        VariableType varType = var.getVariableType();

        switch (varType) {
            case VariableType.OUTPUT:
                return Y.get(Variable.OUTPUT);
            case VariableType.INPUT:
                return MapForX.computeIfAbsent(var.getSerial(), k -> 0L);
            case VariableType.WORK:
                return MapForZ.computeIfAbsent(var.getSerial(), k -> 0L);
            default:
                return -1L; //TODO: Handle with exceptions

        }

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

        switch (varType) {
            case VariableType.INPUT:
                return MapForX.containsKey(var.getSerial());

            case VariableType.WORK:
                return MapForZ.containsKey(var.getSerial());

            case VariableType.OUTPUT:
                return true; //We Always Have Y.

            default:
                return true; //TODO: Handle with exceptions


        }
    }

    public void updateLabelIndexes(List<Instruction> instructions)
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

    public VariableImplement InsertVariableToEmptySpot(VariableType type)
    {
        Map<Integer, Long> relevantMap;
        switch (type) {
            case VariableType.INPUT:
                relevantMap = MapForX;
                break;
            case VariableType.WORK:
                relevantMap = MapForZ;
                break;
            default:
                relevantMap = new HashMap<Integer, Long>(); //TODO: Handle with exceptions if type is output
        }
                int availableKey = 1;
                while (relevantMap.containsKey(availableKey)) {
                    availableKey++;
                }

            VariableImplement res = new VariableImplement(type, availableKey);
            setVarValue(res, 0);

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
    }

