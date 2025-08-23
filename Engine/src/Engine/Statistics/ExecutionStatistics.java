package Engine.Statistics;

import Engine.PairDataStructure;
import Engine.Vars.Variable;

import java.util.List;

public class ExecutionStatistics
{
    private final int executeId;
    private final int executeDegree;
    private final List<PairDataStructure<Variable, Long>> Inputs;
    private final long finalYValue;
    private final long totalCycles;

    public ExecutionStatistics(int executeDegree, int executeId, long finalYValue, List<PairDataStructure<Variable, Long>> inputs, long totalCycles)
    {
        this.executeDegree = executeDegree;
        this.executeId = executeId;
        this.finalYValue = finalYValue;
        Inputs = inputs;
        this.totalCycles = totalCycles;
    }

    @Override
    public String toString()
    {
        return "ExecutionStatistics{" +
                "executeId=" + executeId +
                ", executeDegree=" + executeDegree +
                ", Inputs=" + Inputs +
                ", finalYValue=" + finalYValue +
                ", totalCycles=" + totalCycles +
                '}';
    }

    public String GetStatisticRepresentation()
    {
        return "ExecutionStatistics{" +
                "executeId = " + executeId +
                ", executeDegree = " + executeDegree +
                ", Inputs = " + Inputs +
                ", Y = " + finalYValue +
                ", totalCycles = " + totalCycles +
                '}';
    }
}
