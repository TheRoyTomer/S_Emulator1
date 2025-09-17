package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.LabelInterface;
import Engine.Statistics.ExecutionStatistics;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;
import EngineObject.InstructionDTO;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;

import java.util.List;

//Convert from engine Objects to their related DTOs.
public class Convertor
{


    public Convertor()
    {
    }


    public static InstructionDTO InstructionToDTO(Instruction inst, Context myContext)
    {
        if (inst == null) {return null;}

        return new InstructionDTO(
                inst.getLineIndex(),
                inst instanceof S_Instruction,
                inst.getLabel().getLabelRepresentation(),
                VariableToDTO(inst.getVar(), myContext),
                inst.getName(),
                InstructionToDTO(inst.getHolder(), myContext),
                inst.getCycles(),
                inst.getArgIfExist().map(var -> VariableToDTO(var, myContext)),
                inst.getConstantIfExist(),
                inst.getLabelToJumpIfExist().map(LabelInterface::getLabelRepresentation),
                inst.getFuncUserInputIfExist(),
                inst.getFuncArgsIfExist());
    }

    public static List<VariableDTO> varsToDTOList(List<Variable> vars, Context myContext)
    {
        return vars.stream().map(var->VariableToDTO(var, myContext)).toList();
    }


    public static VariableDTO VariableToDTO(Variable v, Context specificContext)
    {
        return new VariableDTO(
                v.getVariableRepresentation(),
                specificContext.getVarValue(v));
    }

    public static List<InstructionDTO> convertInstructionsListToDTO(List<Instruction> instructions, Context myContext)
    {
        return instructions
                .stream()
                .map(inst -> InstructionToDTO(inst , myContext))
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

    public static VariableImplement convertStringToVar(String str)
    {

        VariableType type = switch (Character.toUpperCase(str.charAt(0))){
            case 'X' -> VariableType.INPUT;
            case 'Z' -> VariableType.WORK;
            default -> VariableType.OUTPUT;
        };
        if (type == VariableType.OUTPUT) return Variable.OUTPUT;
        return new VariableImplement(type, Integer.parseInt(str.substring(1)));

    }

}
