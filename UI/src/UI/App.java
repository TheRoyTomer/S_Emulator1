package UI;

import Engine.EngineFacade;

public class App
{
    public static void main(String[] args)
    {
        EngineFacade facade = new EngineFacade();
        MenuControl m = new MenuControl(facade);
        m.runProject();
    }
}
