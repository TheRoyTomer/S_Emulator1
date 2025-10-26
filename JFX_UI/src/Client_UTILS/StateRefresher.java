package Client_UTILS;

import Out.FunctionSelectorChoiseDTO;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

public class StateRefresher  extends TimerTask
{
private EmulatorScreenController controller;

    public StateRefresher(EmulatorScreenController controller)
    {
        this.controller = controller;
    }

    @Override
    public void run()
    {
        String url = ClientConstants.SERVER_URL + "/getAllPrograms";
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = ClientConstants.HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful())
            {
                String body = response.body().string();
                List<FunctionSelectorChoiseDTO> programs =
                        ClientConstants.GSON.fromJson(body,
                                new TypeToken<List<FunctionSelectorChoiseDTO>>(){}.getType());

                Platform.runLater(() -> controller.updateProgramComboBox(programs));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
