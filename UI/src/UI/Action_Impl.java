package UI;

import Engine.EngineFacade;
import EngineObject.StatisticDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.ViewResultDTO;

import java.util.List;

public class Action_Impl
{
    private final MenuControl menuControl;
    private final EngineFacade facade;

    public Action_Impl(MenuControl menuControl)
    {
        this.menuControl = menuControl;
        this.facade = menuControl.getFacade();
    }

    public void displayHistory()
    {
        List<StatisticDTO> res = facade.getHistory();
        if (res.isEmpty()) System.out.println("History is empty!");
        res.stream()
                .map(StatisticDTO::getStatRepresentation)
                .forEach(System.out::println);
    }

    public void execution()
    {
        int degree = menuControl.getAndValidateDegree();
        List<Long> input = menuControl.getAndValidateInputs();
        ExecuteResultDTO res = facade.executeProgram(degree, input);
        //System.out.println(DisplayDTOs.getExecuteDTORepresentation(res));
    }

    public void viewOriginalProgram()
    {
        ViewResultDTO res = facade.viewOriginalProgram();
        //System.out.println(DisplayDTOs.getViewDTORepresentation(res));
    }

    public void Expand()
    {
        int degree = menuControl.getAndValidateDegree();
        ViewResultDTO res = facade.viewExpandedProgram(degree);
        //System.out.println(DisplayDTOs.getViewDTORepresentation(res));
    }

    public boolean loadProgram()
    {
        //System.out.println("Enter XML file path: ");

        LoadResultDTO res = facade.loadFromXML(menuControl.scanner.nextLine());
        //System.out.println(res.message());
        return res.isLoaded();
    }

}
