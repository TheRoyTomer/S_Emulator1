package jfx.ui.UserInfoTableView;

import Out.UserInfoDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jfx.ui.ShowUsersComp.ShowUsersCompController;

import java.util.List;

public class UserInfoTableViewController
{

    // FXML Data Members
    @FXML
    private TableView<UserInfoDTO> usersTable;

    @FXML
    private TableColumn<UserInfoDTO, String> userNameColumn;

    @FXML
    private TableColumn<UserInfoDTO, Integer> uploadedProgramsColumn;

    @FXML
    private TableColumn<UserInfoDTO, Integer> uploadedFunctionsColumn;

    @FXML
    private TableColumn<UserInfoDTO, Integer> currentCreditsColumn;

    @FXML
    private TableColumn<UserInfoDTO, Integer> usedCreditsColumn;

    @FXML
    private TableColumn<UserInfoDTO, Integer> executionsColumn;

    @FXML
    private Button unselectUserBtn;

    private ShowUsersCompController fatherController;

    @FXML
    public void initialize()
    {
        // Set up row factory for styling and selection
        usersTable.setRowFactory(tv -> {
            TableRow<UserInfoDTO> row = new TableRow<UserInfoDTO>()
            {
                @Override
                protected void updateItem(UserInfoDTO item, boolean empty)
                {
                    super.updateItem(item, empty);

                    // Clear previous styles
                    getStyleClass().removeAll("selected-user");

                    if (!empty && item != null) {
                        // TODO: Add CSS classes based on conditions
                        // For example: if this user is selected
                    }
                }
            };

            // Handle row click for selection
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && row.getItem() != null) {
                    UserInfoDTO selectedUser = row.getItem();
                    handleUserSelection(selectedUser);
                }
            });

            return row;
        });

        // Set up columns using lambdas
        userNameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().userName())
        );

        uploadedProgramsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().uploadedProgramCount()).asObject()
        );

        uploadedFunctionsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().uploadedFunctionCount()).asObject()
        );

        currentCreditsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().currentCredits()).asObject()
        );

        usedCreditsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().usedCredits()).asObject()
        );

        executionsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().executionCount()).asObject()
        );

    }

    public void setFatherController(ShowUsersCompController fatherController)
    {
        this.fatherController = fatherController;
    }

    public void setRows(List<UserInfoDTO> list)
    {
        //System.out.println("setRows called with " + list.size() + " users");
        //System.out.println("usersTable is null? " + (usersTable == null));

        if (usersTable == null) {
            System.out.println("ERROR: usersTable is NULL! Cannot set items.");
            return;
        }

        usersTable.setItems(FXCollections.observableArrayList(list));
        usersTable.refresh();
    }

    private void handleUserSelection(UserInfoDTO user)
    {
        fatherController.setSelectedUser(user);
        fatherController.updateHistoryBySelection();
    }

    @FXML
    private void handleUnselectUser()
    {
        // Clear table selection
        usersTable.getSelectionModel().clearSelection();
        fatherController.setSelectedUser(null);
        fatherController.updateHistoryBySelection();
    }
}