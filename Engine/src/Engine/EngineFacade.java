package Engine;

import Engine.ArchitectureTypes.ArchitectureTypeEnum;
import Engine.Programs.*;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class EngineFacade
{
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;
    private Program program;

    private StatisticDTO recentExecutionStatistics;

    private int executionID;


    public EngineFacade()
    {
        this.program = new Program();
        this.executer = new Executer(program);
        this.loader = new Loader(program);
        this.viewer = new Viewer(program);
        this.executionID = 1;
    }

    public int getMaxDegree()
    {
        return program.calcMaxDegree();
    }

    public StatisticDTO getRecentExecutionStatistics()
    {
        return recentExecutionStatistics;
    }

    public List<VariableDTO> getInputVariablesPreExecute()
    {
        return program.getContext().getAll_X_InList(program.getInstructions())
                .stream()
                .map(var -> Convertor.VariableToDTO(var, program.getContext()))
                .toList();
    }

    public LoadResultDTO loadFromXML(File file, ConcurrentMap<String, Function> functions, Set<String> programKeyName)
    {
        try {
            loader.load(file , functions, programKeyName);
            this.viewer.setProgramAndContext(this.program);
            this.executer.setProgramAndContext(this.program);
        } catch (RuntimeException e) {
            return new LoadResultDTO(false, List.of(), e.getMessage());
        }
        return new LoadResultDTO(true, this.program.getFunctionsUserStringAndNames(), "File is loaded!");
    }


    public ViewResultDTO viewProgram()
    {
        return viewer.viewProgram(0);
    }

    public ViewResultDTO viewExpandedProgram(int degree)
    {
        executer.expand(degree);
        return viewer.viewProgram(degree);
    }

    public ExecuteResultDTO executeProgram(int degree, List<Long> inputsVal, int credits, int archTypeSerial)
    {
        ExecuteResultDTO res = executer.execute(degree, inputsVal, credits);
        if (!res.isFailed())
        {
            this.recentExecutionStatistics = Convertor.convertStatisticToDTO(
                    program.getRecentExecutionStatistics(),
                    this.executionID,
                    program instanceof Function ? true : false,
                    archTypeSerial,
                    program.getName());
            this.executionID++;
        }
        return res;
    }

    public StepOverResult stepOver(long PC, int creditsLeft, int archTypeSerial)
    {
        StepOverResult res = executer.stepOver(PC, creditsLeft);
        if (!res.isFailed() && res.isDebugCompleted())
        {
            this.recentExecutionStatistics = Convertor.convertStatisticToDTO(
                    program.getRecentExecutionStatistics(),
                    this.executionID,
                    program instanceof Function ? true : false,
                    archTypeSerial,
                    program.getName());
            this.executionID++;
        }
        return res;
    }

    public StepOverResult breakPoint(long startPC, long destPC, int creditsLeft, int archTypeSerial)
    {
        StepOverResult res =  executer.breakPoint(startPC, destPC, creditsLeft);
        if (!res.isFailed() && res.isDebugCompleted())
        {
            this.recentExecutionStatistics = Convertor.convertStatisticToDTO(
                    program.getRecentExecutionStatistics(),
                    this.executionID,
                    program instanceof Function ? true : false,
                    archTypeSerial,
                    program.getName());
            this.executionID++;
        }
        return res;

    }

    public List<VariableDTO> preDebug(int degree, List<Long> inputsVal)
    {
        return executer.preDebug(degree, inputsVal);
    }

    public ExecuteResultDTO resumeDebug(int PC, int creditsLeft, int archTypeSerial)
    {
        ExecuteResultDTO res = executer.resume(PC, creditsLeft);
        if (!res.isFailed())
        {
            this.recentExecutionStatistics = Convertor.convertStatisticToDTO(
                    program.getRecentExecutionStatistics(),
                    this.executionID,
                    program instanceof Function ? true : false,
                    archTypeSerial,
                    program.getName());
            this.executionID++;
        }
        return res;
    }


    public Program getProgram()
    {
        return this.program;
    }

    public ViewResultDTO loadExistingProgram(Program selected)
    {
        this.program = selected;
        viewer.setProgramAndContext(selected);
        executer.setProgramAndContext(selected);
        return this.viewProgram();
    }

    public int getCreditCostByArchitecture(int architecture)
    {
        return switch (architecture) {
            case 1 -> ArchitectureTypeEnum.ONE.getCostArchitecture();
            case 2 -> ArchitectureTypeEnum.TWO.getCostArchitecture();
            case 3 -> ArchitectureTypeEnum.THREE.getCostArchitecture();
            case 4 -> ArchitectureTypeEnum.FOUR.getCostArchitecture();
            default -> 0;
        };
    }
}
