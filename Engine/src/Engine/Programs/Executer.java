package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Out.ExecuteResultDTO;

import java.util.ArrayList;
import java.util.List;

public class Executer
{
    private Program program;
    private Context context;

    public Executer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }

    public ExecuteResultDTO execute(int degree, List<Long> inputs)
    {
        context.resetMapsState();
        context.insertInputsToMap(inputs);
        expand(degree);
        context.updateIndexLabels(program.getExpandedInstructions());
        int totalCycles = runProgram();

        program.getHistory().addExecutionStatistics(
                degree,
                context.getVarValue(Variable.OUTPUT),
                inputs,
                totalCycles);

        return new ExecuteResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(program.getExpandedInstructions()),
                context.getVarValue(Variable.OUTPUT),
                context.getAllVarsInList(program.getExpandedInstructions())
                        .stream()
                        .map(Convertor::VariableToDTO)
                        .toList(),
                totalCycles);
    }

    public int runProgram()
    {
        List<Instruction> instructions = program.getExpandedInstructions();
        int sumCycles = 0;
        LabelInterface label = null;
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
        int lineIndex;
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
