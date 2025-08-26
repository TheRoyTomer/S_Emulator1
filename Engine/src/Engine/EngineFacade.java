package Engine;

import Engine.Programs.Executer;
import Engine.Programs.Loader;
import Engine.Programs.Program;
import Engine.Programs.Viewer;
import Engine.Statistics.StatisticsList;
import Out.LoadResultDTO;
import Out.ViewResultDTO;

import javax.swing.text.View;

public class EngineFacade
{
    private final Program program;
    private final Executer executer;
    private final Loader loader;
    private final Viewer viewer;
    private final StatisticsList statsList;

    public EngineFacade()
    {
        this.program = new Program();
        this.executer = new Executer(program);
        this.loader = new Loader(program);
        this.viewer = new Viewer(program);
        this.statsList = new StatisticsList();

    }

    public LoadResultDTO loadFromXML(String filePath)
    {
        //LoadResultDTO loadResultDTO = new LoadResultDTO();
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

    }
}
