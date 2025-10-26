package jfx.ui.HeaderComp;

import Out.LoadResultDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import Client_UTILS.ClientConstants;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.UTILS;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class HeaderCompController {

    private EmulatorScreenController mainController;
/*    private EngineFacade facade;*/
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

    public void setMainController(EmulatorScreenController mainController)
    {
        this.mainController = mainController;
        loadButton.disableProperty().bind(mainController.getDebugModeProperty());
    }

    @FXML
    void handleLoadButton(ActionEvent event) {
      /*  if (facade == null) {
            UTILS.showError("Engine is not initialized.");
            return;
        }*/

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
        mainController.handleLoad();
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
                    mainController.onProgramLoaded(result.funcNames());
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

    private void showProgress() {
        javafx.application.Platform.runLater(() -> {
            progressBar.setVisible(true);
            progressLabel.setVisible(true);
        });
    }
   /* public void setFacade(EngineFacade facade)
    {
        this.facade = facade;
    }*/
}
