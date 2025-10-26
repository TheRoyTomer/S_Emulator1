package jfx.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfx.ui.EmulatorScreen.EmulatorScreenController;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the Login screen instead of MainFX
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jfx/ui/LoginComp/LoginComp.fxml"));
            Parent root = loader.load();




            Scene scene = new Scene(root);
            stage.setTitle("Login Test");
            stage.setScene(scene);
            stage.setMinWidth(400);
            stage.setMinHeight(300);


            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}


