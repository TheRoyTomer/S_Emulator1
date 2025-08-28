package UI;

import Engine.EngineFacade;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.LoadResultDTO;
import Out.ViewResultDTO;
import jakarta.xml.bind.SchemaOutputResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class menuControl

{
    private EngineFacade facade;
    Scanner scanner = new Scanner(System.in);
    DisplayDTOs  displayDTOs = new DisplayDTOs();

    public menuControl(EngineFacade facade)
    {
        this.facade = facade;
    }


    public ActionTypes getActionTypeToExecute(int choice, boolean isLoaded)
    {
        return switch (choice) {
            case 1 -> ActionTypes.LOAD;
            case 2 -> isLoaded ? ActionTypes.VIEW_ORIGINAL_PROGRAM : ActionTypes.EXIT;

            case 3 -> {
                if (isLoaded) yield ActionTypes.EXPAND_VIEW;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 4 -> {
                if (isLoaded) yield ActionTypes.EXECUTE;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 5 -> {
                if (isLoaded) yield ActionTypes.HISTORY;
                throw new IllegalArgumentException("Option not available: no program is loaded in the system");
            }
            case 6 -> {
                if (isLoaded) yield ActionTypes.EXIT;
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
        System.out.println(displayDTOs.getViewResultDTO(res));
    }

    public void Expand()
    {

        ViewResultDTO res = facade.viewExpandedProgram(scanner.nextInt());
        System.out.println(displayDTOs.getViewResultDTO(res));
    }

    public void execution()
    {
        int maxDegree = facade.getMaxDegree();
        int degree = getAndValidateDegree();
        List<Long> input = getAndValidateInputs(degree);
        ExecuteResultDTO res = facade.executeProgram(degree, input);
        System.out.println(displayDTOs.getExecuteResultDTO(res));
    }

    public int getAndValidateDegree()
    {
        int maxDegree = facade.getMaxDegree();
        System.out.print("Enter expansion level (0 to " + maxDegree + "): ");
        int degree = Integer.parseInt(scanner.nextLine());

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
        List<String> split = Arrays.asList(input.split(","));
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

}
