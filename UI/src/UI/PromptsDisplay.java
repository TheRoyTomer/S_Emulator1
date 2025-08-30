package UI;

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
}
