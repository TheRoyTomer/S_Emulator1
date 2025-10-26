package jfx.ui.ProgramsInfoTableView;

import Out.ProgramInfoDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ProgramsInfoTableViewController {

    @FXML
    private TableView<ProgramInfoDTO> table;

    @FXML
    private TableColumn<ProgramInfoDTO, String> nameCol;

    @FXML
    private TableColumn<ProgramInfoDTO, String> uploadUserNameCol;

    @FXML
    private TableColumn<ProgramInfoDTO, Integer> instructionCountCol;

    @FXML
    private TableColumn<ProgramInfoDTO, Integer> maxDegreeCol;

    @FXML
    private TableColumn<ProgramInfoDTO, Integer> executionsCol;

    @FXML
    private TableColumn<ProgramInfoDTO, Integer> avgCreditCol;

    @FXML
    public void initialize() {
        // Set up row factory for styling and selection
        table.setRowFactory(tv -> {
            TableRow<ProgramInfoDTO> row = new TableRow<ProgramInfoDTO>() {
                @Override
                protected void updateItem(ProgramInfoDTO item, boolean empty) {
                    super.updateItem(item, empty);

                    // Clear previous styles
                    getStyleClass().removeAll("selected-program"); // או כל CSS class שתרצה

                    if (!empty && item != null) {
                        // TODO: Add CSS classes based on conditions
                        // For example: if this program is selected
                    }
                }
            };

            // Handle row click for selection
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && row.getItem() != null) {
                    ProgramInfoDTO selectedProgram = row.getItem();
                    // TODO: Handle program selection
                    handleProgramSelection(selectedProgram);
                }
            });

            return row;
        });

        // Set up columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        uploadUserNameCol.setCellValueFactory(new PropertyValueFactory<>("uploadUserName"));
        instructionCountCol.setCellValueFactory(new PropertyValueFactory<>("numberOfInstructions"));
        maxDegreeCol.setCellValueFactory(new PropertyValueFactory<>("maxDegree"));
        executionsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfExecutions"));
        avgCreditCol.setCellValueFactory(new PropertyValueFactory<>("avgCreditCost"));
    }
    public void setRows(List<ProgramInfoDTO> list) {
        table.setItems(FXCollections.observableArrayList(list));
    }

    private void handleProgramSelection(ProgramInfoDTO program) {
        // TODO: Implement what happens when a program is selected
        System.out.println("Selected program: " + program.getName());
    }
}
