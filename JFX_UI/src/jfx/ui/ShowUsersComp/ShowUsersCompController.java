package jfx.ui.ShowUsersComp;

import Out.UserInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.HistoryComp.HistoryCompController;
import jfx.ui.UserInfoTableView.UserInfoTableViewController;

import java.util.List;

public class ShowUsersCompController {

    // FXML Data Members - Injected Controllers
    @FXML
    private VBox userInfoTableView;

    @FXML
    private VBox historyComp;

    @FXML
    private UserInfoTableViewController userInfoTableViewController;

    @FXML
    private HistoryCompController historyCompController;

    private DashboardScreenCompController mainController;

    public void setMainController(DashboardScreenCompController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    public void initialize()
    {
        // TODO: Initialize the component
        // TODO: Set up bindings between user selection and history display
    }

    // Getters for sub-controllers
    public UserInfoTableViewController getUserInfoTableViewController() {
        // TODO: Return user info table view controller
        return null;
    }

    public HistoryCompController getHistoryCompController() {
        // TODO: Return history component controller
        return null;
    }

    // Update table method
    public void UpdateUsersTable(List<UserInfoDTO> userDTOs)
    {
        System.out.println("UpdateUsersTable called with " + userDTOs.size() + " users");
        if (userInfoTableViewController != null) {
            userInfoTableViewController.setRows(userDTOs);
        } else {
            System.out.println("ERROR: userInfoTableViewController is NULL!");
        }
    }

}