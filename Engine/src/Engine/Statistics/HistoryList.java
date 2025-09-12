package Engine.Statistics;

import Engine.Programs.Convertor;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;

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

    public void addExecutionStatistics(int executeDegree, long finalYValue, List<Long> inputsVals, List<VariableDTO> variablesVals, int totalCycles)
    {
        int newExecuteID = historyList.size() + 1;
        historyList.add(new ExecutionStatistics(newExecuteID, executeDegree, finalYValue, inputsVals, variablesVals, totalCycles));
    }

    public List<StatisticDTO> getListAsDTOs() {
        return historyList.stream()
                .map(Convertor::convertStatisticToDTO)
                .toList();
    }
}
