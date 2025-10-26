package Engine;

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

    public ExecuteResultDTO executeProgram(int degree, List<Long> inputsVal)
    {
         return executer.execute(degree, inputsVal);
    }

    public List<StatisticDTO> getHistory()
    {
        return program.getHistory().getListAsDTOs();
    }

    public StepOverResult stepOver(long PC) {return executer.stepOver(PC);}
    public StepOverResult breakPoint(long startPC, long destPC) {return executer.breakPoint(startPC ,destPC);}

    public List<VariableDTO> preDebug(int degree, List<Long> inputsVal) {return executer.preDebug(degree, inputsVal);}

    public ExecuteResultDTO resumeDebug(int PC) {return executer.resume(PC);}

  /*  public ViewResultDTO changeSelectedProgram(String name)
    {
        if (name.equals(originalProgram.getName()))
        {
            this.selectedProgram = this.originalProgram;
        }
        else
        {
            this.selectedProgram = this.originalProgram.getFunctionByName(name);
        }
        viewer.setProgramAndContext(this.selectedProgram);
        executer.setProgramAndContext(this.selectedProgram);
        return this.viewProgram();
    }
*/

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
}
