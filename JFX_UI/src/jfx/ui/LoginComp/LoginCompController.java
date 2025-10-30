package jfx.ui.LoginComp;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.MainComp.MainCompController;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static Client_UTILS.ClientConstants.*;

public class LoginCompController
{

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    private MainCompController mainCompController;

    public void setMainCompController(MainCompController mainCompController)
    {
        this.mainCompController = mainCompController;
    }

    @FXML
    void onLogin(ActionEvent event) {
        // Read and validate input (server also validates; this is UX-only)
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        if (username.isEmpty()) {
            errorLabel.setText("Please enter a username");
            return;
        }

        RequestBody body = RequestBody.create(
                GSON.toJson(java.util.Map.of("username", username)),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Prepare request
        Request req = new Request.Builder()
                .url(SERVER_URL + "/login")
                .post(body)
                .build();

        // Disable UI while logging in (direct, no setBusy)
        loginButton.setDisable(true);
        usernameField.setDisable(true);
        errorLabel.setText("Logging in...");

        // Run HTTP call off the FX thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (Response res = HTTP_CLIENT.newCall(req).execute()) {
                    String responseBody = res.body() != null ? res.body().string() : "";

                    if (res.isSuccessful()) {
                        Platform.runLater(() -> {
                            try {
                                // Update username in MainComp
                                if (mainCompController != null) {
                                    mainCompController.setUsername(username);

                                    // Reset credits to 0 for new login
                                    mainCompController.setCredits(0);

                                    // Load Dashboard screen (will pull credits from server)
                                    mainCompController.loadDashboardScreen();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorLabel.setText("Error loading dashboard: " + e.getMessage());
                            }
                        });
                    }

                    else {
                        String errMsg = extractMessage(responseBody);
                        int code = res.code();
                        String finalMsg = errMsg != null ? errMsg : ("Login failed (" + code + ")");
                        Platform.runLater(() -> {
                            errorLabel.setText(finalMsg);
                            // Re-enable UI
                            loginButton.setDisable(false);
                            usernameField.setDisable(false);
                        });
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        errorLabel.setText("Network error: " + e.getMessage());
                        // Re-enable UI
                        loginButton.setDisable(false);
                        usernameField.setDisable(false);
                    });
                }
                return null;
            }
        };

        new Thread(task, "login-call").start();
    }

    private String extractMessage(String json)
    {
        try {
            JsonObject obj = GSON.fromJson(json, JsonObject.class);
            if (obj != null && obj.has("message")) {
                return obj.get("message").getAsString();
            }
        } catch (Exception ignore) { }
        return null;
    }


}
