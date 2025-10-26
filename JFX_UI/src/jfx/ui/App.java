package jfx.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfx.ui.MainComp.MainCompController;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the MainComp as the root container
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jfx/ui/MainComp/MainComp.fxml"));
            Parent root = loader.load();

            // Get the MainCompController
            MainCompController mainController = loader.getController();

            // Load LoginComp inside MainComp
            mainController.loadLoginScreen();

            Scene scene = new Scene(root);
            stage.setTitle("S-Emulator");
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setWidth(900);
            stage.setHeight(600);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}