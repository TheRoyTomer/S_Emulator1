package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.LabelInterface;
import Engine.Statistics.ExecutionStatistics;
import Engine.Vars.Variable;
import EngineObject.InstructionDTO;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;

import java.util.List;

//Convert from engine Objects to their related DTOs.
public class Convertor
{
    private static Context context;

    public Convertor(Context context)
    {
        Convertor.context = context;
    }


    public static InstructionDTO InstructionToDTO(Instruction inst)
    {
        if (inst == null) {return null;}

        return new InstructionDTO(
                inst.getLineIndex(),
                inst instanceof S_Instruction,
                inst.getLabel().getLabelRepresentation(),
                VariableToDTO(inst.getVar()),
                inst.getName(),
                InstructionToDTO(inst.getHolder()),
                inst.getCycles(),
                inst.getArgIfExist().map(Convertor::VariableToDTO),
                inst.getConstantIfExist(),
                inst.getLabelToJumpIfExist().map(LabelInterface::getLabelRepresentation));
    }

    public static VariableDTO VariableToDTO(Variable v)
    {
        return new VariableDTO(
                v.getVariableRepresentation(),
                context.getVarValue(v));
    }

    public static List<InstructionDTO> convertInstructionsListToDTO(List<Instruction> instructions)
    {
        return instructions
                .stream()
                .map(Convertor::InstructionToDTO)
                .toList();
    }

    public static StatisticDTO convertStatisticToDTO(ExecutionStatistics stat)
    {
        return new StatisticDTO(
                stat.getExecuteId(),
                stat.getExecuteDegree(),
                stat.getInputsVals(),
                stat.getVariablesVals(),
                stat.getFinalYValue(),
                stat.getTotalCycles());

    }

}
