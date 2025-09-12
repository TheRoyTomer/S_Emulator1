package EngineObject;

import java.util.List;
import java.util.stream.Collectors;

public record StatisticDTO(
        int executeID,
        int degree,
        List<Long> inputs,
        List<VariableDTO> variables,
        long outPutVal,
        int totalCycles)
{
    public String getStatRepresentation()
    {
        return String.format("Degree: %d\nInputs: %s\nOutput: %d\nTotal cycles: %d\n",
                this.degree,
                inputs.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")),
                this.outPutVal,
                this.totalCycles);
    }
}
