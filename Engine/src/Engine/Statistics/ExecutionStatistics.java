package Engine.Statistics;

import Engine.Vars.Variable;

import java.util.List;

public class ExecutionStatistics
{
    private final int executeId;
    private final int executeDegree;
    private final List<Long> inputsVals;
    private final long finalYValue;
    private final int totalCycles;

    public ExecutionStatistics( int executeId,int executeDegree, long finalYValue, List<Long> inputsVals, int totalCycles)
    {
        this.executeDegree = executeDegree;
        this.executeId = executeId;
        this.finalYValue = finalYValue;
        this.inputsVals = inputsVals;
        this.totalCycles = totalCycles;
    }

    public int getExecuteId()
    {
        return executeId;
    }

    public int getExecuteDegree()
    {
        return executeDegree;
    }

    public List<Long> getInputsVals()
    {
        return inputsVals;
    }

    public long getFinalYValue()
    {
        return finalYValue;
    }

    public int getTotalCycles()
    {
        return totalCycles;
    }
}
