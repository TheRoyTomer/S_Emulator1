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
    private Program program;
    private  Context context;
    //Holds information on debug until process has ended and we can insert new statistics
    private  saveStatePreDebug statePreDebug = new saveStatePreDebug();
    public Executer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }

    public void setProgramAndContext(Program newProgram)
    {
        this.program = newProgram;
        this.context = this.program.getContext();
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

        return getAllVarsInRun();
    }

    public ExecuteResultDTO execute(int degree, List<Long> inputs)
    {
        context.resetMapsState();
        context.insertInputsToMap(inputs);
        expand(degree);
        context.updateIndexLabels(program.getExpandedInstructions());
        int totalCycles = runProgram(0);
        //Inserts new statistic to the history.
        List<VariableDTO> varsInList = getAllVarsInRun();
        program.getHistory().addExecutionStatistics(
                degree,
                context.getVarValue(Variable.OUTPUT),
                getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), inputs),
                varsInList,
                totalCycles);

        return new ExecuteResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(program.getExpandedInstructions(), context),
                context.getVarValue(Variable.OUTPUT),
                varsInList,
                totalCycles);
    }

    public ExecuteResultDTO resume(long PCVal)
    {
        int cycles = runProgram(PCVal) + statePreDebug.getCycle();

        List<VariableDTO> varsInList = getAllVarsInRun();
        //Inserts new statistic to the history.
        program.getHistory().addExecutionStatistics(
                statePreDebug.getDegree(),
                context.getVarValue(Variable.OUTPUT),
                getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), statePreDebug.getInputs()),
                varsInList,
                cycles);


        return new ExecuteResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(program.getExpandedInstructions(), context),
                context.getVarValue(Variable.OUTPUT),
                varsInList,
                cycles
        );

    }

    public int runProgram(long PCVal)
    {
        List<Instruction> instructions = program.getExpandedInstructions();
        program.getExpandedInstructions().forEach(System.out::println);
        int sumCycles = 0;
        for (long PC = PCVal; PC < instructions.size(); )
        {
            long prevPC = PC;
            //sumCycles += instructions.get((int) PC).getCycles();
            PC = this.SingleStepRun(PC);
            sumCycles += instructions.get((int) prevPC).getCycles();
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
            program.getHistory().addExecutionStatistics(
                    statePreDebug.getDegree(),
                    context.getVarValue(Variable.OUTPUT),
                    getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), statePreDebug.getInputs()),
                    getAllVarsInRun(),
                    statePreDebug.getCycle());
        }
        return new StepOverResult(
                currentInstruction.getCycles(),
                Convertor.varsToDTOList(temp, context),
                nextPC);
    }

    private List<VariableDTO> getAllVarsInRun()
    {
        return context.getAllVarsInList(program.getExpandedInstructions())
                .stream()
                .map(var -> Convertor.VariableToDTO(var, context))
                .toList();
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
            for (Instruction instruction : currDegreeInstructions)
            {
                nextDegreeInstructions.addAll(instruction.getOneExpand());
            }
            for (Instruction instruction : nextDegreeInstructions)
            {
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
