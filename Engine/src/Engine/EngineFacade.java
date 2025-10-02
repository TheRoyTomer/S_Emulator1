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
import java.util.Objects;

public class EngineFacade
{
    private final Program originalProgram;
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;
    private Program selectedProgram;


    public EngineFacade()
    {
        this.originalProgram = new Program();
        this.executer = new Executer(originalProgram);
        this.loader = new Loader(originalProgram);
        this.viewer = new Viewer(originalProgram);
        //this.selectedProgram = Program.getSelectedProgram();
        this.selectedProgram = this.originalProgram;

    }
    public int getMaxDegree() { return selectedProgram.calcMaxDegree();}

    public List<VariableDTO> getInputVariablesPreExecute()
    {
        return selectedProgram.getContext().getAll_X_InList(selectedProgram.getInstructions())
                .stream()
                .map(var ->Convertor.VariableToDTO(var, selectedProgram.getContext()))
                .toList();
    }

    public LoadResultDTO loadFromXML(File file)
    {
        try
        {
            loader.load(file);
            this.selectedProgram = this.originalProgram;
            this.viewer.setProgramAndContext(this.selectedProgram);
            this.executer.setProgramAndContext(this.selectedProgram);
        } catch (RuntimeException e) {
            return new LoadResultDTO(false, List.of(), e.getMessage());
        }
        return new LoadResultDTO(true, this.selectedProgram.getFunctionsUserStringAndNames(), "File is loaded!");
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
        return selectedProgram.getHistory().getListAsDTOs();
    }

    public StepOverResult stepOver(long PC) {return executer.stepOver(PC);}
    public StepOverResult breakPoint(long PC) {return executer.breakPoint(PC);}

    public List<VariableDTO> preDebug(int degree, List<Long> inputsVal) {return executer.preDebug(degree, inputsVal);}

    public ExecuteResultDTO resumeDebug(int PC) {return executer.resume(PC);}

    public ViewResultDTO changeSelectedProgram(String name)
    {
        if (name.equals(originalProgram.getName()))
        {
            this.selectedProgram = this.originalProgram;
            /*Program.setSelectedProgram(this.originalProgram);
            this.selectedProgram = Program.getSelectedProgram();*/
        }
        else
        {
            this.selectedProgram = this.originalProgram.getFunctionByName(name);
            /*this.selectedProgram = this.originalProgram.getFunctionByName(name);
            Program.setSelectedProgram( this.selectedProgram);*/
        }
        viewer.setProgramAndContext(this.selectedProgram);
        executer.setProgramAndContext(this.selectedProgram);
        //this.selectedProgram.initProgram();
        return this.viewOriginalProgram();
    }


}
