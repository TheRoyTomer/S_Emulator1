package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.StepOverResult;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Executer
{
    private final Program program;
    private final Context context;
    //Holds information on debug until process has ended and we can insert new statistics
    private  saveStatePreDebug statePreDebug = new saveStatePreDebug();
    public Executer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }

    public List<Long> getInputListForStatistics(TreeSet<Variable> inputSet, List<Long> valuesList)
    {
        return inputSet.stream()
                .filter(v -> v.getSerial() > 0 && v.getSerial() <= valuesList.size())
                .map(v -> valuesList.get(v.getSerial() - 1))
                .collect(Collectors.toList());
    }

    public List<VariableDTO> preDebug(int degree, List<Long> inputs)
    {
        statePreDebug = new saveStatePreDebug(degree, inputs);
        context.resetMapsState();
        context.insertInputsToMap(inputs);
        expand(degree);
        context.updateIndexLabels(program.getExpandedInstructions());

        return context.getAllVarsInList(program.getExpandedInstructions())
             .stream()
             .map(Convertor::VariableToDTO)
             .toList();
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
        for (long PC = 0; PC < instructions.size(); )
        {
            sumCycles += instructions.get((int) PC).getCycles();
            PC = this.SingleStepRun(PC);
        }
        return sumCycles;
    }

    public long SingleStepRun(long currPC)
    {
        Instruction currentInstruction = program.getExpandedInstructions().get((int) currPC);
        LabelInterface label = currentInstruction.execute();
        if (label == FixedLabels.EMPTY)
        {
            return currPC + 1;
        } else if (label == FixedLabels.EXIT)
        {
            return program.getExpandedInstructions().size();
        } else {
            return context.getFromMapL((Label_Implement) label);
        }
    }


    public StepOverResult stepOver(long PC)
    {
        Instruction currentInstruction = program.getExpandedInstructions().get((int) PC);

        List<Variable> temp = currentInstruction.getChangedVariables();
        long nextPC = this.SingleStepRun(PC);
        statePreDebug.increaseCyclesByNumber(currentInstruction.getCycles());
        if (nextPC >= program.getExpandedInstructions().size())
        {
            List<VariableDTO> allVarsInRun =
                    context.getAllVarsInList(program.getExpandedInstructions())
                            .stream()
                            .map(Convertor::VariableToDTO)
                            .toList();

            //Inserts new statistic to the history.
            program.getHistory().addExecutionStatistics(
                    statePreDebug.getDegree(),
                    context.getVarValue(Variable.OUTPUT),
                    getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), statePreDebug.getInputs()),
                    allVarsInRun,
                    statePreDebug.getCycle());
        }
        return new StepOverResult(
                currentInstruction.getCycles(),
                Convertor.varsToDTOList(temp),
                nextPC);
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
