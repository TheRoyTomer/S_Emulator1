package UI;

import EngineObject.InstructionDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.ViewResultDTO;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayDTOs
{
    private static String getFullPathInstructionRepresentation(InstructionDTO inst)
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

    private static String getProgramRepresentation(List<InstructionDTO> instructions)
    {
        return instructions.stream()
                .map(DisplayDTOs::getFullPathInstructionRepresentation)
                .collect(Collectors.joining("\n"));
    }

    public static String getExecuteDTORepresentation(ExecuteResultDTO result)
    {
        return String.format("%s\n%s\nY = %d\nUsed Vars: %s\nTotal cycles: %d\n",
                result.programName(),
                DisplayDTOs.getProgramRepresentation(result.instructions()),
                result.outputVal(),
                result.usedVarsByOrder().stream()
                        .map(VariableDTO::getVarAndValueRepresentation)
                        .collect(Collectors.joining(" , ")),
                result.cycles());
    }

    public static String getViewDTORepresentation(ViewResultDTO result)
    {
        return String.format("%s\n%s\nUsed Input Vars: %s\nUsed Labels: %s\n",
                result.programName(),
                DisplayDTOs.getProgramRepresentation(result.instructions()),
                result.usedInputsVarsByOrder().stream()
                        .map(VariableDTO::getVarRepresentation)
                        .collect(Collectors.joining(" , ")),
                String.join(" , ", result.usedLabelsByOrder()));
    }

}
