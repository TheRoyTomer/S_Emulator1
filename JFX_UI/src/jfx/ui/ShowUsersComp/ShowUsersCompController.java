package jfx.ui.ShowUsersComp;

import EngineObject.StatisticDTO;
import Out.UserInfoDTO;
import javafx.beans.property.BooleanProperty;
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

    public HistoryCompController getHistoryCompController()
    {
        return historyCompController;
    }

    public UserInfoDTO selectedUser;

    @FXML
    public void initialize()
    {
        if (userInfoTableViewController != null)
        {
            userInfoTableViewController.setFatherController(this);
        }

        if (historyCompController != null)
        {
            historyCompController.setFatherController(this);
        }

    }

    public UserInfoDTO getSelectedUser()
    {
        return selectedUser;
    }

    public void setSelectedUser(UserInfoDTO selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    public BooleanProperty getDebugPropertyToBind()
    {
        try
        {
            return mainController.getMainCompController().getEmulatorScreenController().getDebugModeProperty();
        }
        catch (Exception e)
        {return null;}
    }

    // Update table method
    public void UpdateUsersTable(List<UserInfoDTO> userDTOs)
    {
        if (userInfoTableViewController != null) {userInfoTableViewController.setRows(userDTOs);}
    }

    public void updateHistoryBySelection()
    {
        if (historyCompController != null)
        {
            if (this.selectedUser != null)
            {
                historyCompController.updateHistoryTree(selectedUser.userHistory());
            }
             else
            {
                historyCompController.updateHistoryTree(mainController.getUserHistory());
            }
        }

    }

    public void handleReRun(StatisticDTO selected)
    {
        mainController.getMainCompController().reRunStatistic(selected);
    }

}