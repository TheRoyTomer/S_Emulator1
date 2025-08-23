package Engine.Statistics;

import Engine.PairDataStructure;
import Engine.Vars.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsList
{
    private static int nextId = 1;
    private List<ExecutionStatistics> lst = new ArrayList<>();

    @Override
    public String toString()
    {
        return lst.stream()
                .map(ExecutionStatistics::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }


    public String getStatisticsListRepresentation()
    {
        return lst.stream()
                .map(ExecutionStatistics::GetStatisticRepresentation)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void addExecutionStatistics(int executeDegree, long finalYValue, List<PairDataStructure<Variable, Long>> inputs, long totalCycles)
    {
        lst.add(new ExecutionStatistics(nextId++, executeDegree, finalYValue, inputs, totalCycles));
    }
}
