package Programs;

import Instructions_Types.Calculable;
import Instructions_Types.Instruction;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Labels.Label_Implement;
import Vars.Variable;
import Vars.Y_Var;

import java.util.*;

public class Program implements Calculable
{
    private int degree;
    private int cycles;
    private List<Instruction> instructions = new ArrayList<>();
    private final Map<Long, Long> MapForX = new HashMap<>();
    private final Map<Long, Long> MapForZ = new HashMap<>();
    private final Map<Label_Implement, Long> MapForL = new HashMap<>();
    public static Variable outPut = new Y_Var();

    public Program(/*, List<Instruction> instructions*/)
    {
        this.degree = this.calcDegree();
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
    public int getDegree()
    {
        return this.degree;
    }

    public long getFromMapX(long key)
    {
        Long value = MapForX.get(key);
        if (value == null) {
            value = Long.valueOf(0);
        }
        return value;
    }

    public void setInMapX(long key, long value)
    {
        if (value < 0) {
            value = 0;
        }
        MapForX.put(key, value);
    }

    public Boolean isExistInMapX(long key)
    {
        Long value = MapForX.get(key);
        if (value == null) {
            return false;
        }
        return true;
    }

    public long getFromMapZ(long key)
    {
        Long value = MapForZ.get(key);
        if (value == null) {
            value = Long.valueOf(0);
        }
        return value;
    }

    public void setInMapZ(long key, long value)
    {
        if (value < 0) {
            value = 0;
        }
        MapForZ.put(key, value);
    }

    public Boolean isExistInMapZ(long key)
    {
        Long value = MapForZ.get(key);
        if (value == null) {
            return false;
        }
        return true;
    }

    public long getFromMapL(long key)
    {
        Long value = MapForL.get(key);
        if (value == null) {
            value = Long.valueOf(0);
        }
        return value;
    }

    public void setInMapL(long key, long value)
    {
        if (value < 0) {
            value = 0;
        }
        MapForZ.put(key, value);
    }

    public Boolean isExistInMapL(LabelInterface key)
    {
        Long value = MapForL.get(key);
        if (value == null) {
            return false;
        }
        return true;
    }




    public long get_Y_Output()
    {
        return outPut.getValue();
    }

    public void set_Y_Output(long value)
    {
        outPut.setValue(value);
    }

    @Override
    public int calcDegree()
    {
        int max = -1;
        for (Instruction c : instructions) {
            if (max < c.getDegree()) {
                max = c.getDegree();
            }
        }
        return max + 1;

    }


    public int calcCycles()
    {
        int sum = 0;
        for (Instruction c : instructions) {
            sum += c.getCycles();
        }
        return sum;
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
}
