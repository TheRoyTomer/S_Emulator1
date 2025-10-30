package jfx.ui.FunctionsInfoTableView;

import Out.FunctionInfoDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class FunctionsInfoTableViewController {

    @FXML
    private TableView<FunctionInfoDTO> table;

    @FXML
    private TableColumn<FunctionInfoDTO, String> nameCol;

    @FXML
    private TableColumn<FunctionInfoDTO, String> uploadUserNameCol;

    @FXML
    private TableColumn<FunctionInfoDTO, Integer> instructionCountCol;

    @FXML
    private TableColumn<FunctionInfoDTO, Integer> maxDegreeCol;

    @FXML
    private TableColumn<FunctionInfoDTO, String> relatedProgramCol;

    private FunctionInfoDTO selectedFunction;

    @FXML
    public void initialize() {
        // Set up row factory for styling and selection
        table.setRowFactory(tv -> {
            TableRow<FunctionInfoDTO> row = new TableRow<FunctionInfoDTO>() {
                @Override
                protected void updateItem(FunctionInfoDTO item, boolean empty) {
                    super.updateItem(item, empty);

                    // Clear previous styles
                    getStyleClass().removeAll("selected-function");

                    if (!empty && item != null) {
                        // TODO: Add CSS classes based on conditions
                        // For example: if this function is selected
                    }
                }
            };

            // Handle row click for selection
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && row.getItem() != null) {selectedFunction = row.getItem();}
            });

            return row;
        });

        // Set up columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        uploadUserNameCol.setCellValueFactory(new PropertyValueFactory<>("uploadUserName"));
        instructionCountCol.setCellValueFactory(new PropertyValueFactory<>("numberOfInstructions"));
        maxDegreeCol.setCellValueFactory(new PropertyValueFactory<>("maxDegree"));
        relatedProgramCol.setCellValueFactory(new PropertyValueFactory<>("relatedProgramName"));
    }

    public void setRows(List<FunctionInfoDTO> list) {
        // Save the currently selected item BEFORE updating
        FunctionInfoDTO selectedItem = table.getSelectionModel().getSelectedItem();
        String selectedFunctionName = selectedItem != null ? selectedItem.getName() : null;

        // Update the table items
        table.setItems(FXCollections.observableArrayList(list));

        table.refresh();
        // Restore selection if the same function still exists in the new list
        if (selectedFunctionName != null) {
            for (int i = 0; i < table.getItems().size(); i++) {
                if (table.getItems().get(i).getName().equals(selectedFunctionName)) {
                    table.getSelectionModel().select(i);
                    break;
                }
            }
        }
    }

}