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
import javafx.scene.control.cell.PropertyValueFactory;
import jfx.ui.MainFX.MainFXController;
import jfx.ui.ViewComp.ViewCompController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.sun.javafx.css.StyleClassSet.getStyleClass;


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
        instructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>()
                {
                    @Override
                    protected void updateItem(InstructionDTO item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        getStyleClass().removeAll("highlight", "highlightRowDebug");

                        if (!empty && item != null) {
                            getStyleClass().addAll(updateHighlights(item, getIndex()));
                        }
                    }
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
        if (viewController != null)
        {
            String selection = viewController.getHighlightSelection();
            if (selection != null && !selection.equals("No selection") &&
                    !selection.isEmpty() && item.isInInstruction(selection)) {
                res.add("highlight");
            }

            if (viewController.getMainController() != null &&
                    rowIndex == viewController.getMainController().getCurrPC_Property().getValue()
                    && viewController.getMainController().getDebugModeProperty().get())
            {
                res.add("highlightRowDebug");
            }
        }
        return res;
    }

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

}
