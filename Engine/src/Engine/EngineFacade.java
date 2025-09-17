package Engine;

import Engine.Programs.*;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class EngineFacade
{
    private final Program program;
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;

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
        } catch (RuntimeException e) {
            return new LoadResultDTO(false, List.of(), e.getMessage());
        }
        return new LoadResultDTO(true, this.program.getFunctionsUserString(), "File is loaded!");
    }

    public ViewResultDTO viewOriginalProgram()
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

    public List<VariableDTO> preDebug(int degree, List<Long> inputsVal) {return executer.preDebug(degree, inputsVal);}

    public ExecuteResultDTO resumeDebug(int PC) {return executer.resume(PC);}

    public List<String> getFunctionsUserInput()
    {
        return program.getFunctionsUserString();
    }


}
