package jfx.ui.HeaderComp;

import Engine.EngineFacade;
import Out.LoadResultDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfx.ui.MainFX.MainFXController;
import jfx.ui.UTILS;

import java.io.File;

public class HeaderCompController {

    private MainFXController mainController;
    private EngineFacade facade;
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

    public void setMainController(MainFXController mainController)
    {
        this.mainController = mainController;
        loadButton.disableProperty().bind(mainController.getDebugModeProperty());
    }

    @FXML
    void handleLoadButton(ActionEvent event) {
        if (facade == null) {
            UTILS.showError("Engine is not initialized.");
            return;
        }

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
                LoadResultDTO result = facade.loadFromXML(file);
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

    private void showProgress() {
        javafx.application.Platform.runLater(() -> {
            progressBar.setVisible(true);
            progressLabel.setVisible(true);
            //Todo: Chat said to remove, does it works without?
            //loadButton.setDisable(true);
        });
    }

    private void hideProgress() {
        javafx.application.Platform.runLater(() -> {
            progressBar.setVisible(false);
            progressLabel.setVisible(false);
            progressBar.progressProperty().unbind();
            progressLabel.setText("");
            //Todo: Chat said to remove, does it works without?
            //loadButton.setDisable(false);
        });
    }
    public void setFacade(EngineFacade facade)
    {
        this.facade = facade;
    }
}
