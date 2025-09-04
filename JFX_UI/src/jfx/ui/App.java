package jfx.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args); // מפעיל את מחזור החיים של JavaFX
    }

    @Override
    public void start(Stage stage) {
        Label root = new Label("Hello JavaFX!");
        Scene scene = new Scene(root, 360, 220);
        stage.setTitle("JFX_UI Demo");
        stage.setScene(scene);
        stage.show();
    }
}
