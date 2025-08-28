package Engine;

import Engine.Programs.*;
import Engine.Statistics.HistoryList;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.ViewResultDTO;

import java.util.List;

public class EngineFacade
{
    private final Program program;
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;
    private final HistoryList statsList;

    public EngineFacade()
    {
        this.program = new Program();
        this.executer = new Executer(program);
        this.loader = new Loader(program);
        this.viewer = new Viewer(program);
        this.statsList = new HistoryList();

    }
    public int getMaxDegree() { return program.calcMaxDegree();}

    public List<VariableDTO> getInputVariablesPreExecute(int degree)
    {
        return program.getContext().getAll_X_InList(program.getInstructions())
                .stream()
                .map(Convertor::VariableToDTO)
                .toList();
    }

    public LoadResultDTO loadFromXML(String filePath)
    {
        try
        {
           loader.load(filePath);
        } catch (RuntimeException e) {
            return new LoadResultDTO(false, e.getMessage());
        }
        return LoadResultDTO.SUCCESS;
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
        return statsList.getHistory();
    }



}
