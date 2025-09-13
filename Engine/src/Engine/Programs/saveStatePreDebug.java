package Engine.Programs;

import Out.ExecuteResultDTO;

import java.util.ArrayList;
import java.util.List;

public class saveStatePreDebug
{
    private int cycle;
    private List<Long> inputs;
    private int degree;

    public saveStatePreDebug(int degree,  List<Long> inputs)
    {
        this.degree = degree;
        this.inputs = inputs;
        this.cycle = 0;
    }

    public saveStatePreDebug()
    {
        this.cycle = 0;
        this.inputs = new ArrayList<>();
        this.degree = 0;
    }

    public int getCycle()
    {
        return cycle;
    }

    public void setCycle(int cycle)
    {
        this.cycle = cycle;
    }

    public List<Long> getInputs()
    {
        return inputs;
    }

    public int getDegree()
    {
        return degree;
    }

    public void increaseCyclesByNumber(int number)
    {
        this.cycle += number;
    }

}
