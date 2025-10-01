package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Instructions_Types.S_Type.Quote;
import Engine.Labels.LabelInterface;
import Engine.Statistics.ExecutionStatistics;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;
import EngineObject.InstructionDTO;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                inst.getFuncArgsToDisplayIfExist());
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


    public static List<String> argsToStringList(String arg)
    {
        String temp = String.valueOf(arg);
        List<String> res = new ArrayList<>();
        while (!temp.isEmpty())
        {
            temp = temp.replaceFirst("^[(),]+", "");
            if (!temp.isEmpty())
            {
                String str = findOneArg(temp);
                res.add(str);
                temp = temp.replace(str, "");

            }
        }
        return res;
    }

    private static String findOneArg(String subArg)
    {
        int ind = subArg.indexOf(",");
        if (ind == -1) {return subArg.replaceAll("\\)", "");}
        String check = subArg.substring(0, ind);
        if (isArgVar(check)) { return check; }

        int parenthesesCounter = 1;
        int index = 0;
        while (parenthesesCounter != 0)
        {
            if (subArg.charAt(index) == '(') parenthesesCounter++;
            else if (subArg.charAt(index) == ')')
            {parenthesesCounter--;}
            index++;
        }
        return subArg.substring(0, index - 1);

    }

    public static boolean isArgVar(String subArg)
    {
        char c = Character.toUpperCase(subArg.charAt(0));
        if (c != 'X' && c != 'Y' && c != 'Z')
        {
            return false;
        }
        if (subArg.length() == 1 && c == 'Y')
        {
            return true;
        }

        try
        {
            String temp = subArg.substring(1);
            int serial = Integer.parseInt(temp);
            return true;
        }
        catch (NumberFormatException | StringIndexOutOfBoundsException  e)
        {
            return false;
        }
    }

    public static List<String> extractVariables(String text)
    {
        return Arrays.stream(text.split("[^a-zA-Z0-9]+"))
                .filter(part -> part.matches("[xXyYzZ]\\d+"))
                .distinct()
                .collect(Collectors.toList());
    }




}
