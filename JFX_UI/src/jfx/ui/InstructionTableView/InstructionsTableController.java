package jfx.ui.InstructionTableView;

import EngineObject.InstructionDTO;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.ViewComp.ViewCompController;

import java.util.ArrayList;
import java.util.List;


public class InstructionsTableController {

    private ViewCompController viewController;

    @FXML private TableView<InstructionDTO> instructionsTable;

    @FXML private TableColumn<InstructionDTO, Integer> indexCol;

    @FXML private TableColumn<InstructionDTO, String> bsCol;

    @FXML private TableColumn<InstructionDTO, String> labelCol;

    @FXML private TableColumn<InstructionDTO, String> commandCol;

    @FXML private TableColumn<InstructionDTO, String> cyclesCol;


    @FXML
    private void initialize()
    {
        instructionsTable.setRowFactory(tv -> {
            TableRow<InstructionDTO> row = new TableRow<InstructionDTO>() {
                @Override
                protected void updateItem(InstructionDTO item, boolean empty) {
                    super.updateItem(item, empty);

                    getStyleClass().removeAll("highlight", "highlightRowDebug", "breakpoint");

                    if (!empty && item != null) {
                        getStyleClass().addAll(updateHighlights(item, getIndex()));
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    if (event.getClickCount() == 2 ||
                            (event.getButton() == MouseButton.PRIMARY && event.isControlDown())) {

                        InstructionDTO instruction = row.getItem();
                        if (instruction != null && viewController != null &&
                                viewController.getMainController() != null) {
                            viewController.getMainController().toggleBreakpointAtLine(instruction.lineIndex());
                        }
                    }
                }
            });

            return row;
        });

        indexCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().lineIndex()));
        labelCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().label()));
        cyclesCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().toStringCyclesByFuncName()));
        bsCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().computeSynthetic()));
        commandCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().commandBody()));
    }

    private List<String> updateHighlights(InstructionDTO item, int rowIndex)
    {
        List<String> res = new ArrayList<>();
        if (viewController != null && viewController.getMainController() != null)
        {
            EmulatorScreenController mainController = viewController.getMainController();

            if (mainController.hasBreakpoint(item.lineIndex())) {
                res.add("breakpoint");
            }

            String selection = viewController.getHighlightSelection();
            if (selection != null && !selection.equals("No selection") &&
                    !selection.isEmpty() && item.isInInstruction(selection)) {
                res.add("highlight");
            }

            if (rowIndex == mainController.getCurrPC_Property().getValue()
                    && mainController.getDebugModeProperty().get()) {
                res.add("highlightRowDebug");
            }
        }
        return res;
    }


    /*private List<String> updateHighlights(InstructionDTO item, int rowIndex)
    {
        List<String> res = new ArrayList<>();
        if (viewController != null && viewController.getMainController() != null)
        {
            EmulatorScreenController mainController = viewController.getMainController();

            if (mainController.hasBreakpoint(item.lineIndex())) {
                res.add("breakpoint");
            }

            String selection = viewController.getHighlightSelection();
            if (selection != null && !selection.equals("No selection") &&
                    !selection.isEmpty() && item.isInInstruction(selection)) {
                res.add("highlight");
            }

            if (rowIndex == mainController.getCurrPC_Property().getValue()
                    && mainController.getDebugModeProperty().get()) {
                res.add("highlightRowDebug");
            }
        }
        return res;
    }
*/
    public void setViewController(ViewCompController viewController)
    {
        this.viewController = viewController;
    }

    public ReadOnlyObjectProperty<InstructionDTO> selectedItemProperty() {
        return instructionsTable.getSelectionModel().selectedItemProperty();
    }

    public void setRows(List<InstructionDTO> list)
    {
        instructionsTable.setItems(FXCollections.observableArrayList(list));
    }


    public void refreshTable()
    {
        instructionsTable.refresh();
    }

    public int getInstructionTableSize()
    {
        return instructionsTable.getItems().size();
    }

    public void disableSelection()
    {
        instructionsTable.setSelectionModel(null);
    }

    public InstructionDTO getSelectedInstruction()
    {
        return instructionsTable.getSelectionModel().getSelectedItem();
    }

}
