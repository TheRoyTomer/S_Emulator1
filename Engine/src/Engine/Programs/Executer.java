package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Executer
{
    private final Program program;
    private final Context context;

    public Executer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }

    public List<Long> getInputListForStatistics(TreeSet<Variable> inputList, List<Long> valuesList)
    {
        return inputList.stream()
                .filter(v -> v.getSerial() > 0 && v.getSerial() <= valuesList.size())
                .map(v -> valuesList.get(v.getSerial() - 1))
                .collect(Collectors.toList());
    }

    public ExecuteResultDTO execute(int degree, List<Long> inputs)
    {
        context.resetMapsState();
        context.insertInputsToMap(inputs);
        expand(degree);
        context.updateIndexLabels(program.getExpandedInstructions());
        int totalCycles = runProgram();

        List<VariableDTO> allVarsInRun =
                context.getAllVarsInList(program.getExpandedInstructions())
                .stream()
                .map(Convertor::VariableToDTO)
                .toList();

        //Inserts new statistic to the history.
        program.getHistory().addExecutionStatistics(
                degree,
                context.getVarValue(Variable.OUTPUT),
                getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), inputs),
                allVarsInRun,
                totalCycles);

        return new ExecuteResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(program.getExpandedInstructions()),
                context.getVarValue(Variable.OUTPUT),
                allVarsInRun,
                totalCycles);
    }

    public int runProgram()
    {
        List<Instruction> instructions = program.getExpandedInstructions();
        int sumCycles = 0;
        LabelInterface label;
        Instruction currentInstruction;
        for (long PC = 0; PC < instructions.size(); )
        {
            currentInstruction = instructions.get((int) PC);
            label = currentInstruction.execute();
            sumCycles += currentInstruction.getCycles();
            if (label == FixedLabels.EMPTY)
            {
                PC++;
            } else if (label == FixedLabels.EXIT)
            {
                break;
            } else {
                PC = context.getFromMapL((Label_Implement) label);
            }
        }
        return sumCycles;
    }

    public void expand(int degree)
    {
        List<Instruction> currDegreeInstructions = new ArrayList<>(program.getInstructions());
        List<Instruction> nextDegreeInstructions = new ArrayList<>();
        int lineIndex = 1;
        for (Instruction instruction : currDegreeInstructions)
        {
            instruction.setLineIndex(lineIndex);
            lineIndex++;
        }

        while (degree != 0) {
            lineIndex = 1;
            for (Instruction instruction : currDegreeInstructions) {
                nextDegreeInstructions.addAll(instruction.getOneExpand());
            }
            for (Instruction instruction : nextDegreeInstructions) {
                instruction.setLineIndex(lineIndex);
                lineIndex++;
            }
            currDegreeInstructions.clear();
            currDegreeInstructions.addAll(nextDegreeInstructions);
            nextDegreeInstructions.clear();
            degree--;
        }
        program.setExpandedInstructions(currDegreeInstructions);
    }

}
