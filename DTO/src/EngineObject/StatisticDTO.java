package EngineObject;

import java.util.List;
import java.util.stream.Collectors;

public record StatisticDTO(
        String name,
        int executeID,
        int degree,
        List<Long> inputs,
        List<VariableDTO> variables,
        long outPutVal,
        int totalCycles,
        boolean isFunction,
        int arcType)

{
    public String getStatRepresentation()
    {
        String archRep = switch (this.arcType) {
        case 1 -> "I";
        case 2 -> "II";
        case 3 -> "III";
        case 4 -> "IV";
        default -> "";
    };
        return String.format("Name: %s\nDegree: %d\nInputs: %s\nOutput: %d\nTotal cycles: %d\n ProgramType: %s\n Architecture: %s\n",
                this.name,
                this.degree,
                inputs.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")),
                this.outPutVal,
                this.totalCycles,
                isFunction ? "Func" : "Program",
                archRep);
    }
}
