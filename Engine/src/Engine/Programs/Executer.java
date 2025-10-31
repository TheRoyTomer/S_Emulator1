package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Type.Quote;
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

    public ExecuteResultDTO execute(int degree, List<Long> inputs, int credits)
    {
        context.resetMapsState();
        context.insertInputsToMap(inputs);
        expand(degree);
        context.updateIndexLabels(program.getExpandedInstructions());
        int totalCycles = runProgram(0, credits);
        //-1 means no credits left mid run
        if (totalCycles == -1)
        {
            return ExecuteResultDTO.FAILED;
        }
        List<VariableDTO> varsInList = getAllVarsInRun();
        program.setRecentExecutionStatistics(
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

    public saveStatePreDebug getStatePreDebug()
    {
        return statePreDebug;
    }

    public ExecuteResultDTO resume(long PCVal, int creditsLeft)
    {

        int cyclesSoFar = runProgram(PCVal, creditsLeft);

        if (cyclesSoFar == -1) {return ExecuteResultDTO.FAILED;}

        int cycles = cyclesSoFar += statePreDebug.getCycle();

        List<VariableDTO> varsInList = getAllVarsInRun();

        program.setRecentExecutionStatistics(
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

    public int runProgram(long PCVal, int credits)
    {
        List<Instruction> instructions = program.getExpandedInstructions();
        int sumCycles = 0;
        for (long PC = PCVal; PC < instructions.size(); )
        {
            long prevPC = PC;
            PC = this.SingleStepRun(PC, credits);
            if (PC < 0)
            {
                return -1;
            }
            int instCycles = instructions.get((int) prevPC).getCycles();
            sumCycles += instCycles;
            credits -= instCycles;
        }
        return sumCycles;
    }

    public long SingleStepRun(long currPC, int creditsLeft)
    {
        Instruction currentInstruction = program.getExpandedInstructions().get((int) currPC);

        LabelInterface label = currentInstruction.execute();

        if (currentInstruction.getCycles() > creditsLeft) {return -1;}
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

    public StepOverResult breakPoint(long startPC, long destPC, int creditsLeft)
    {
        long nextPC = startPC;
        StepOverResult res = null;
        boolean debugFinished = false;
        int sumCycles = 0;

        if (startPC == destPC)
        {
            res = stepOver(nextPC, creditsLeft);

            if (res.isFailed()) {return StepOverResult.FAILED;}

            nextPC = res.nextPC();
            sumCycles += res.cycles();
            creditsLeft -= res.cycles();
        }

        while ((nextPC != destPC) && (nextPC != program.getExpandedInstructions().size()))
        {
            res = stepOver(nextPC, creditsLeft);

            if (res.isFailed()) {return StepOverResult.FAILED;}

            nextPC = res.nextPC();
            sumCycles += res.cycles();
            creditsLeft -= res.cycles();
        }

        if (nextPC >= program.getExpandedInstructions().size())
        {
            debugFinished = true;


            program.setRecentExecutionStatistics(
                    statePreDebug.getDegree(),
                    context.getVarValue(Variable.OUTPUT),
                    getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), statePreDebug.getInputs()),
                    getAllVarsInRun(),
                    statePreDebug.getCycle());
        }

        return new StepOverResult(
                sumCycles,
                res != null ? res.changedVars() : List.of(),
                nextPC,
                true,
                debugFinished);
    }

    public StepOverResult stepOver(long PC, int creditsLeft)
    {
        boolean debugFinished = false;

        Instruction currentInstruction = program.getExpandedInstructions().get((int) PC);

        List<Variable> temp = currentInstruction.getChangedVariables();
        long nextPC = this.SingleStepRun(PC, creditsLeft);

        if (nextPC < 0) {return StepOverResult.FAILED;}

        statePreDebug.increaseCyclesByNumber(currentInstruction.getCycles());
        if (nextPC >= program.getExpandedInstructions().size())
        {
            debugFinished = true;


            program.setRecentExecutionStatistics(
                    statePreDebug.getDegree(),
                    context.getVarValue(Variable.OUTPUT),
                    getInputListForStatistics(context.getAll_X_InList(program.getInstructions()), statePreDebug.getInputs()),
                    getAllVarsInRun(),
                    statePreDebug.getCycle());

        }
        return new StepOverResult(
                currentInstruction.getCycles(),
                Convertor.varsToDTOList(temp, context),
                nextPC,
                true,
                debugFinished);
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
