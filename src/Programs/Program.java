package Programs;

import Instructions_Types.Calculable;
import Instructions_Types.Instruction;
import Vars.Y_Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program implements Calculable
{
    private int degree;
    private int cycles;
    private List<Instruction> instructions = new ArrayList<>();
    private static final Map<Integer, Integer> MapForX = new HashMap<>();
    private static final Map<Integer, Integer> MapForZ = new HashMap<>();
    //private static final Map<Integer, ?> MapForL = new HashMap<>();
    private static Y_Var output = new Y_Var();

    public Program(int degree, int cycles, List<Instruction> instructions)
    {
        this.degree = degree;
        this.cycles = cycles;
        this.instructions = instructions;
    }

    @Override
    public String toString()
    {
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        for(Instruction c : instructions)
        {
            sb.append("#<" + counter +'>').append(c.toString()).append("\n");
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

    public static int getFromMapX(int key)
    {
        Integer value =  MapForX.get(key);
        if(value == null)
        {
            value = 0;
        }
        return value;
    }

    public static void setInMapX(int key, int value)
    {
        MapForX.put(key, value);
    }

    public static Boolean isExistInMapX(int key)
    {
        Integer value = MapForX.get(key);
        if (value == null)
        {
            return false;
        }
        return true;
    }

    public static int getFromMapZ(int key)
    {
        Integer value =  MapForZ.get(key);
        if(value == null)
        {
            value = 0;
        }
        return value;
    }

    public static void setInMapZ(int key, int value)
    {
        MapForZ.put(key, value);
    }

    public static Boolean isExistInMapZ(int key)
    {
        Integer value = MapForZ.get(key);
        if (value == null)
        {
            return false;
        }
        return true;
    }

   /* public static int getFromMapL(int key)
    {
        Integer value =  MapForL.get(key);
        if(value == null)
        {
            value = 0;
        }
        return value;
    }

    public static void setInMapL(int key, int value)
    {
        MapForL.put(key, value);
    }

    public static Boolean isExistInMapL(int key)
    {
        Integer value = MapForL.get(key);
        if (value == null)
        {
            return false;
        }
        return true;
    }*/

    public static int get_Y_Output()
    {
        return output.getValue();
    }

    public static void set_Y_Output(int value)
    {
         output.setValue(value);
    }

    @Override
    public int calcDegree()
    {
        int max = -1;
        for (Instruction c : instructions)
        {
            if (max < c.getDegree())
            {
                max = c.getDegree();
            }
        }
        return max + 1;

    }

    @Override
    public int calcCycles()
    {
        int sum = 0;
        for (Instruction c : instructions) {sum+=c.getCycles();}
        return sum;
    }

    @Override
    public void calc()
    {

    }
}
