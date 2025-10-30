package jfx.ui.DashboardScreenComp;

import Client_UTILS.ClientConstants;
import Client_UTILS.StateRefresher;
import Out.UpdateDataDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfx.ui.AvailableProgsAndFuncsComp.AvailableProgsAndFuncsCompController;
import jfx.ui.HeaderComp.HeaderCompController;
import jfx.ui.MainComp.MainCompController;
import jfx.ui.ShowUsersComp.ShowUsersCompController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;

import static Client_UTILS.ClientConstants.REFRESH_RATE;

public class DashboardScreenCompController
{
    @FXML
    private HBox headerComp;

    @FXML
    private VBox ShowUsersComp;
    @FXML
    private VBox AvailableProgsAndFuncsComp;

    // FXML Data Members - Injected Controllers
    @FXML
    private HeaderCompController headerCompController;

    @FXML
    private ShowUsersCompController showUsersCompController;

    @FXML
    private AvailableProgsAndFuncsCompController availableProgsAndFuncsCompController;

    private Timer timer;
    private StateRefresher refresher;

    private MainCompController mainCompController;



    @FXML
    public void initialize()
    {
        if (this.headerCompController != null && this.availableProgsAndFuncsCompController != null
                && this.showUsersCompController != null)
        {
            this.headerCompController.setMainController(this);
            this.showUsersCompController.setMainController(this);
            this.availableProgsAndFuncsCompController.setMainController(this);
        }

        startPolling();

    }

    public void startPolling()
    {
        this.timer = new Timer();
        this.refresher = new StateRefresher(this);
        timer.schedule(this.refresher, 0, REFRESH_RATE);
    }

    public void stopPolling()
    {
        if (timer != null) timer.cancel();
        if (refresher != null) refresher.cancel();
    }

    public void setMainCompController(MainCompController mainCompController)
    {
        this.mainCompController = mainCompController;
    }

    public MainCompController getMainCompController()
    {
        return this.mainCompController;
    }

    public void pullDashboardData()
    {
        String url = ClientConstants.SERVER_URL + "/getDataPull";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        ClientConstants.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                if (!response.isSuccessful()) {
                    System.err.println("Failed to pull dashboard data: HTTP " + response.code());
                    return;
                }

                String json = response.body() != null ? response.body().string() : "";

                UpdateDataDTO data = ClientConstants.GSON.fromJson(json, UpdateDataDTO.class);
                Platform.runLater(() -> {updateDashboardUI(data);});
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.err.println("Failed to pull dashboard data: " + e.getMessage());
            }
        });

    }

    private void updateDashboardUI(UpdateDataDTO data)
    {
        if (availableProgsAndFuncsCompController != null)
        {
            availableProgsAndFuncsCompController.UpdateTables(
                    data.getAllPrograms(), data.getAllFunctions());
        }
        if (showUsersCompController != null)
        {
            showUsersCompController.UpdateUsersTable(data.getAllUsers());
        }

        // Update credits in MainComp
        if (mainCompController != null)
        {
            mainCompController.setCredits(data.getUserCredits());
        }
    }
}