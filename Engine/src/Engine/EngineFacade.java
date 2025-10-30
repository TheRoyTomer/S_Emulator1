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

public class EngineFacade
{
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;
    private Program program;


    public EngineFacade()
    {
        this.program = new Program();
        this.executer = new Executer(program);
        this.loader = new Loader(program);
        this.viewer = new Viewer(program);

    }
    public int getMaxDegree() { return program.calcMaxDegree();}

    public List<VariableDTO> getInputVariablesPreExecute()
    {
        return program.getContext().getAll_X_InList(program.getInstructions())
                .stream()
                .map(var ->Convertor.VariableToDTO(var, program.getContext()))
                .toList();
    }

    public LoadResultDTO loadFromXML(File file)
    {
        try
        {
            loader.load(file);
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

    public ExecuteResultDTO executeProgram(int degree, List<Long> inputsVal, int credits)
    {
         return executer.execute(degree, inputsVal, credits);
    }

    public List<StatisticDTO> getHistory()
    {
        return program.getHistory().getListAsDTOs();
    }

    public StepOverResult stepOver(long PC, int creditsLeft) {return executer.stepOver(PC, creditsLeft);}
    public StepOverResult breakPoint(long startPC, long destPC, int creditsLeft) {return executer.breakPoint(startPC, destPC, creditsLeft);}
    public List<VariableDTO> preDebug(int degree, List<Long> inputsVal) {return executer.preDebug(degree, inputsVal);}

    public ExecuteResultDTO resumeDebug(int PC, int creditsLeft) {return executer.resume(PC, creditsLeft);}


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
        return switch (architecture)
        {
            case 1 -> ArchitectureTypeEnum.ONE.getCostArchitecture();
            case 2 -> ArchitectureTypeEnum.TWO.getCostArchitecture();
            case 3 -> ArchitectureTypeEnum.THREE.getCostArchitecture();
            case 4 -> ArchitectureTypeEnum.FOUR.getCostArchitecture();
            default -> 0;
        };
    }
}
