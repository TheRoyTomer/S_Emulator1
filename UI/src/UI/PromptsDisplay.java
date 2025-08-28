package UI;

import java.util.ArrayList;
import java.util.List;

public class PromptsDisplay
{
    public static void showMenu(boolean isLoaded)
    {
        System.out.println("=================================================");
        System.out.println("*******  Welcome to the best S-emulator   *******");
        System.out.println("*******   IN THE WORLD by Roy & Yuval     *******");
        System.out.println("=================================================");
        System.out.println("*         1. Load program from file             *");
        if (isLoaded)
        {
            System.out.println("*         2. Show program                       *");
            System.out.println("*         3. Show expanded program              *");
            System.out.println("*         4. Run program                        *");
            System.out.println("*         5. Show statistics                    *");
            System.out.println("*         6. Exit                               *");
        }
        else
        {
            System.out.println("*         2. Exit                               *");
        }
        System.out.println("=================================================");
    }

    public static void PrintSeperator()
    {
        System.out.println("=================================================");
    }

    public static void getPathFromUser(boolean isFirstTry)
    {
        if (!isFirstTry){System.out.println("Invalid Path!");}
        System.out.println("Enter the path of XML file:");
        PrintSeperator();
    }

    public static void getDegreeFromUser(boolean isFirstTry, int maxDegree)
    {
        if (!isFirstTry){System.out.println("Invalid Degree!");}
        System.out.print("Enter expansion level (0 to " + maxDegree + "): ");
    }

    public static void getInputsFromUser(boolean isFirstTry, List<String> inputVars)
    {
        if (!isFirstTry) {System.out.println("Invalid Input!");}
        System.out.println("The program requires the following input variables:");
        System.out.println(String.join(", ", inputVars));
        System.out.println("Please enter non-negative values for these variables,");
        System.out.println("all in one line, separated by commas.");
        System.out.print("Your input: ");
    }



    //ToDo: delete
    public static void main(String[] args)
    {
        showMenu(false);
        showMenu(true);
        getPathFromUser(false);
        getDegreeFromUser(false, 7);
        getInputsFromUser(false, new ArrayList<String>(List.of("X1", "X8", "X9")));
    }
}
