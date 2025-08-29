package Engine.Statistics;

import Engine.Programs.Convertor;
import EngineObject.StatisticDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryList
{
    private final List<ExecutionStatistics> historyList = new ArrayList<>();

    @Override
    public String toString()
    {
        return historyList.stream()
                .map(ExecutionStatistics::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void reset() { historyList.clear();}


    public String getStatisticsListRepresentation()
    {
        return historyList.stream()
                .map(ExecutionStatistics::GetStatisticRepresentation)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void addExecutionStatistics(int executeDegree, long finalYValue, List<Long> inputsVals, int totalCycles)
    {
        historyList.add(new ExecutionStatistics(historyList.size() + 1, executeDegree, finalYValue, inputsVals, totalCycles));
    }

    public List<StatisticDTO> getListAsDTOs() {
        return historyList.stream()
                .map(Convertor::convertStatisticToDTO)
                .toList();
    }
}
