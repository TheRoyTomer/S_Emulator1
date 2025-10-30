package jfx.ui.HeaderComp;

import Out.LoadResultDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import Client_UTILS.ClientConstants;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.UTILS;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class HeaderCompController {

    private DashboardScreenCompController mainController;
    @FXML
    private Label filePathLabel;

    @FXML
    private Button loadButton;

    @FXML
    private Label titleLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Button chargeCreditsBtn;

    @FXML
    private TextField creditsField;


    public void setMainController(DashboardScreenCompController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    void handleLoadButton(ActionEvent event)
    {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select XML file");
        chooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        Window owner = loadButton.getScene() != null ? loadButton.getScene().getWindow() : null;
        File selected = chooser.showOpenDialog(owner);
        if (selected == null) return; // user cancelled

        String path = selected.getAbsolutePath();
        loadFileWithProgress(selected, path);
        if (mainController != null)
        {
            mainController.getMainCompController().resetBreakPoint();
        }
    }



    private void loadFileWithProgress(File file, String path)
    {
        Task<LoadResultDTO> loadTask = new Task<LoadResultDTO>()
        {
            @Override
            protected LoadResultDTO call() throws Exception
            {

                for (int i = 10; i <= 30; i += 5) {
                    updateProgress(i, 100);
                    Thread.sleep(150);
                }

                updateProgress(40, 100);
                //LoadResultDTO result = facade.loadFromXML(file);
                Out.LoadResultDTO result = httpLoad(file);
                updateProgress(70, 100);

                for (int i = 75; i <= 95; i += 5) {
                    updateProgress(i, 100);
                    Thread.sleep(100);
                }

                updateProgress(100, 100);
                Thread.sleep(200);

                return result;
            }
            @Override
            protected void succeeded() {
                LoadResultDTO result = getValue();
                hideProgress();
                System.out.println(result.isLoaded() ? "Success" : "Failed");

                if (result.isLoaded())
                {
                    filePathLabel.setText(path);
                   // mainController.onProgramLoaded(result.funcNames());
                }
                UTILS.showInfo(result.message());
            }

            @Override
            protected void failed() {
                hideProgress();
                UTILS.showError("Failed to load file:\n" + getException().getMessage());
            }
        };

        progressBar.progressProperty().bind(loadTask.progressProperty());

        loadTask.progressProperty().addListener((obs, oldVal, newVal) -> {
            int percent = (int)(newVal.doubleValue() * 100);
            progressLabel.setText(percent + "%");
        });

        showProgress();

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private Out.LoadResultDTO httpLoad(File file) throws IOException {
        MediaType XML = MediaType.parse("application/xml");
        RequestBody fileBody = RequestBody.create(file, XML);

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("programFile", file.getName(), fileBody)
                .build();

        String url = ClientConstants.SERVER_URL + "/api/load";


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        try (Response response = ClientConstants.HTTP_CLIENT.newCall(request).execute())
        {
            if (!response.isSuccessful()) {
                String respBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    throw new IOException("HTTP " + response.code() + " - " + respBody);
                }
                throw new IOException("HTTP " + response.code());
            }
            String body = response.body() != null ? response.body().string() : "";
            return ClientConstants.GSON.fromJson(body, LoadResultDTO.class);
        }

    }



    private void hideProgress() {
        javafx.application.Platform.runLater(() -> {
            progressBar.setVisible(false);
            progressLabel.setVisible(false);
            progressBar.progressProperty().unbind();
            progressLabel.setText("");
        });
    }

    private void showProgress()
    {
        javafx.application.Platform.runLater(() -> {
            progressBar.setVisible(true);
            progressLabel.setVisible(true);
        });
    }
    @FXML
    private void onChargeCreditsPress()
    {
        String text = creditsField.getText();
        // Clear field immediately
        creditsField.clear();

        // Validate input
        if (text == null || text.trim().isEmpty())
        {
            UTILS.showError("Credits amount is required");
            return;
        }

        int creditsToAdd;
        try {
            creditsToAdd = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            UTILS.showError("Credits amount must be a valid integer number");
            return;
        }

        if (creditsToAdd <= 0)
        {
            UTILS.showError("Credits amount must be a positive integer number");
            return;
        }

        // Send request to server
        chargeCredits(creditsToAdd);
    }

    private void chargeCredits(int amount)
    {
        String url = ClientConstants.SERVER_URL + "/chargeCredits";

        RequestBody body = RequestBody.create(
                "{\"amount\":" + amount + "}",
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        ClientConstants.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful())
                {
                    javafx.application.Platform.runLater(() ->
                            UTILS.showError("Failed to charge credits: HTTP " + response.code())
                    );
                    return;
                }

                try {
                    com.google.gson.JsonObject result = ClientConstants.GSON.fromJson(json, com.google.gson.JsonObject.class);
                    int newTotal = result.get("totalCredits").getAsInt();

                    javafx.application.Platform.runLater(() -> {
                        // Update MainComp credits
                        mainController.getMainCompController().setCredits(newTotal);

                        // Clear field and show success
                        creditsField.clear();
                        UTILS.showInfo("Successfully charged " + amount + " credits!\nNew total: " + newTotal);
                    });

                } catch (Exception e) {
                    javafx.application.Platform.runLater(() ->
                            UTILS.showError("Failed to parse response: " + e.getMessage())
                    );
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                javafx.application.Platform.runLater(() ->
                        UTILS.showError("Network error: " + e.getMessage())
                );
            }
        });
    }
}
