package jfx.ui;

import Engine.EngineFacade;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfx.ui.MainFX.MainFXController;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the main FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jfx/ui/MainFX/MainFX.fxml"));
            Parent root = loader.load();

            // Inject the engine facade into the main controller
          MainFXController mainController = loader.getController();
            mainController.setFacade(new EngineFacade()); // replace with your real implementation if needed

            // Show stage
            Scene scene = new Scene(root);
            stage.setTitle("S-Emulator");
            stage.setScene(scene);
            stage.setMinWidth(429);
            stage.setMinHeight(387);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


