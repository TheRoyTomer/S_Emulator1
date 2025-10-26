package Client_UTILS;

import Out.FunctionSelectorChoiseDTO;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

public class StateRefresher  extends TimerTask
{
private DashboardScreenCompController controller;

    public StateRefresher(DashboardScreenCompController controller)
    {
        this.controller = controller;
    }

    @Override
    public void run()
    {
        controller.pullDashboardData();
    }

}
