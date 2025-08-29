package UI;

import Engine.EngineFacade;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.ViewResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class menuControl

{
    private final EngineFacade facade;
    Scanner scanner = new Scanner(System.in);
    DisplayDTOs  displayDTOs = new DisplayDTOs();
    boolean isLoaded = false;
    public menuControl(EngineFacade facade)
    {
        this.facade = facade;
    }

    public int getIntFromUser()
    {
        return Integer.parseInt(scanner.nextLine());
    }


    public ActionTypes getActionTypeToExecute(int choice)
    {
        return switch (choice) {
            case 1 -> ActionTypes.LOAD;
            case 2 -> this.isLoaded ? ActionTypes.VIEW_ORIGINAL_PROGRAM : ActionTypes.EXIT;

            case 3 -> {
                if (this.isLoaded) yield ActionTypes.EXPAND_VIEW;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 4 -> {
                if (this.isLoaded) yield ActionTypes.EXECUTE;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 5 -> {
                if (this.isLoaded) yield ActionTypes.HISTORY;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 6 -> {
                if (this.isLoaded) yield ActionTypes.EXIT;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }

            default -> throw new IllegalArgumentException("Invalid choice");

        };
    }

    public boolean loadProgram()
    {
        System.out.println("Enter XML file path: ");

        LoadResultDTO res = facade.loadFromXML(scanner.nextLine());
        System.out.println(res.message());
        return res.isLoaded();
    }

    public void viewOriginalProgram()
    {
        ViewResultDTO res = facade.viewOriginalProgram();
        System.out.println(displayDTOs.getViewDTORepresentation(res));
    }

    public void Expand()
    {
        int degree = getAndValidateDegree();
        ViewResultDTO res = facade.viewExpandedProgram(degree);
        System.out.println(displayDTOs.getViewDTORepresentation(res));
    }

    public void execution()
    {
        int maxDegree = facade.getMaxDegree();
        int degree = getAndValidateDegree();
        List<Long> input = getAndValidateInputs(degree);
        ExecuteResultDTO res = facade.executeProgram(degree, input);
        System.out.println(displayDTOs.getExecuteDTORepresentation(res));
    }

    public int getAndValidateDegree()
    {
        int maxDegree = facade.getMaxDegree();
        System.out.print("Enter expansion level (max degree for this program is " + maxDegree + "): ");
        int degree = getIntFromUser();

        if(maxDegree < degree) throw new RuntimeException("Invalid Degree! must be not greater than " + maxDegree);
        else if (degree < 0) throw new RuntimeException("Invalid Degree! must be greater than 0");
        return degree;
    }

    public List<Long> getAndValidateInputs(int degree)
    {

        System.out.println("The program requires the following input variables:");
        System.out.println(facade.getInputVariablesPreExecute(degree).stream()
                .map(VariableDTO::getVarRepresentation)
                .collect(Collectors.joining(" , ")));
        System.out.println("Please enter non-negative values for these variables,");
        System.out.println("all in one line, separated by commas.");
        List<Long> res =  new ArrayList<>();
        String input = scanner.nextLine();
        long value;
        String[] split = input.split(",");
        for(String s : split)
        {
            try{
                value = Long.parseLong(s.trim());
                if (value < 0) throw new IllegalArgumentException(value + "Is negative!");
                res.add(value);
            } catch (NumberFormatException e){
                throw new IllegalArgumentException("Invalid input, not a number: " + s.trim());            }
        }
        return res;
    }

    public void displayHistory()
    {
        List<StatisticDTO> res = facade.getHistory();
        res.stream()
                .map(StatisticDTO::getStatRepresentation)
                .forEach(System.out::println);
    }

    public void runProject()
    {
        ActionTypes currAction = null;
        do
        {
            try {
                PromptsDisplay.showMenu(this.isLoaded);
                System.out.println("Enter your choise: ");
                currAction = getActionTypeToExecute(this.getIntFromUser());
                switch (currAction) {
                    case LOAD -> this.isLoaded |= loadProgram();
                    case VIEW_ORIGINAL_PROGRAM -> viewOriginalProgram();
                    case EXPAND_VIEW -> Expand();
                    case EXECUTE -> execution();
                    case HISTORY -> displayHistory();
                    case EXIT -> System.out.println("Goodbye!");
                }
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
                }

        }while (currAction!=ActionTypes.EXIT);
    }

}
