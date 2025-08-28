package UI;

import EngineObject.InstructionDTO;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.StatisticsViewDTO;
import Out.ViewResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayDTOs
{
    private String getFullPathInstructionRepresentation(InstructionDTO inst)
    {
        StringBuilder str =  new StringBuilder();
        str.append(inst.getInstructionRepresentation());
        InstructionDTO holder = inst.holder();
        while (holder != null)
        {
            str.append(" >> ").append(holder.getInstructionRepresentation());
            holder = holder.holder();
        }
        return str.toString();
    }

    private String getProgramRepresentation(List<InstructionDTO> instructions)
    {
        return instructions.stream()
                .map(this::getFullPathInstructionRepresentation)
                .collect(Collectors.joining("\n"));
    }

    public String getExecuteDTORepresentation(ExecuteResultDTO result)
    {
        return String.format("%s\n%s\n Y = %d\nUsed Vars: %s\nTotal cycles: %d\n",
                result.programName(),
                this.getProgramRepresentation(result.instructions()),
                result.outputVal(),
                result.usedVarsByOrder().stream()
                        .map(VariableDTO::getVarAndValueRepresentation)
                        .collect(Collectors.joining(" , ")),
                result.cycles());
    }

    public String getViewDTORepresentation(ViewResultDTO result)
    {
        return String.format("%s\n%s\n Y = %d\nUsed Input Vars: %s\nUsed Labels: %s\n",
                result.programName(),
                this.getProgramRepresentation(result.instructions()),
                result.outputVal(),
                result.usedInputsVarsByOrder().stream()
                        .map(VariableDTO::getVarRepresentation)
                        .collect(Collectors.joining(" , ")),
                String.join(" , ", result.usedLabelsByOrder()));
    }

}
