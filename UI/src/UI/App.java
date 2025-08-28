package UI;

import Engine.EngineFacade;

public class App
{
    public static void main(String[] args)
    {
        EngineFacade facade = new EngineFacade();
        menuControl m = new menuControl(facade);
        m.runProject();
    }
}
