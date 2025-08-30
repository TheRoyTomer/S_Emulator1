package UI;

import Engine.EngineFacade;
import EngineObject.VariableDTO;

import java.util.*;
import java.util.stream.Collectors;

public class MenuControl

{
    private final EngineFacade facade;
    Scanner scanner = new Scanner(System.in);
    boolean isLoaded = false;

    public MenuControl(EngineFacade facade)
    {
        this.facade = facade;
    }

    public EngineFacade getFacade() {return facade;}

    public int getIntFromUser()
    {
        try {
            return Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e) {
            throw new InputMismatchException("Invalid input! this is not a number!");
        }
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


    public int getAndValidateDegree()
    {
        int maxDegree = facade.getMaxDegree();
        System.out.print("Enter expansion level (max degree for this program is " + maxDegree + "): ");
        int degree = getIntFromUser();

        if(maxDegree < degree) throw new RuntimeException("Invalid Degree! must be not greater than " + maxDegree);
        else if (degree < 0) throw new RuntimeException("Invalid Degree! must be greater than 0");
        return degree;
    }

    public List<Long> getAndValidateInputs()
    {

        System.out.println("The program requires the following input variables:");
        System.out.println(facade.getInputVariablesPreExecute().stream()
                .map(VariableDTO::getVarRepresentation)
                .collect(Collectors.joining(" , ")));
        System.out.println("Please enter non-negative values for these variables,");
        System.out.println("all in one line, separated by commas.");
        List<Long> res =  new ArrayList<>();
        String input = scanner.nextLine();
        if (input.isEmpty()) {return Collections.emptyList();}
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

    public void runProject()
    {
        ActionTypes currAction = null;
        do
        {
            try {
                Action_Impl action = new Action_Impl(this);
                PromptsDisplay.showMenu(this.isLoaded);
                System.out.println("Enter your choice: ");
                currAction = getActionTypeToExecute(this.getIntFromUser());
                switch (currAction) {
                    case LOAD -> this.isLoaded |= action.loadProgram();
                    case VIEW_ORIGINAL_PROGRAM -> action.viewOriginalProgram();
                    case EXPAND_VIEW -> action.Expand();
                    case EXECUTE -> action.execution();
                    case HISTORY -> action.displayHistory();
                    case EXIT -> System.out.println("Goodbye! We are sure we deserve 100 on this excellent S_EMULATOR");
                }
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
                }

        }while (currAction!=ActionTypes.EXIT);
    }
}
