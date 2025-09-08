package jfx.ui.HeaderComp;

import Engine.EngineFacade;
import Out.LoadResultDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfx.ui.MainFX.MainFXController;

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

    public void setMainController(MainFXController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    void handleLoadButton(ActionEvent event) {
        if (facade == null) {
            showError("Engine is not initialized.");
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

        try {

            LoadResultDTO res = facade.loadFromXML(selected);
            if (res.isLoaded())
            {
                filePathLabel.setText(path);
                mainController.onProgramLoaded();
            }
            showInfo(res.message());

        } catch (Exception ex) {
            showError("Failed to load file:\n" + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Load Error");
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public void setFacade(EngineFacade facade)
    {
        this.facade = facade;
    }
}
