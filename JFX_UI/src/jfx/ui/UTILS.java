package jfx.ui;

import javafx.scene.control.Alert;

public class UTILS
{
    public static void showError(String msg)
    {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Load Error");
        a.setContentText(msg);
        a.showAndWait();
    }
    public static void showInfo(String msg)
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

}
