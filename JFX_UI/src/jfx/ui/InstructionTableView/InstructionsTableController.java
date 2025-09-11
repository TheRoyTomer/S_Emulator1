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

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


public class InstructionsTableController {

    @FXML private TableView<InstructionDTO> instructionsTable;

    @FXML private TableColumn<InstructionDTO, Integer> indexCol;

    @FXML private TableColumn<InstructionDTO, String> bsCol;

    @FXML private TableColumn<InstructionDTO, String> labelCol;

    @FXML private TableColumn<InstructionDTO, String> commandCol;

    @FXML private TableColumn<InstructionDTO, Integer> cyclesCol;


    @FXML
    private void initialize()
    {
        indexCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().lineIndex()));
        labelCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().label()));
        cyclesCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().cycles()));
        bsCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().computeSynthetic()));
        commandCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().commandBody()));

    }

    public ReadOnlyObjectProperty<InstructionDTO> selectedItemProperty() {
        return instructionsTable.getSelectionModel().selectedItemProperty();
    }

    public void setRows(List<InstructionDTO> list)
    {
        instructionsTable.setItems(FXCollections.observableArrayList(list));
    }

    public void setRowHighlighter(String ComboBoxChoise) {
        instructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override protected void updateItem(InstructionDTO it, boolean empty)
            {
                super.updateItem(it, empty);
                if(ComboBoxChoise.isEmpty()) {return;}
                if (!empty && it != null && it.isInInstruction(ComboBoxChoise))
                {
                    if(!getStyleClass().contains("highlight"))
                    {
                        getStyleClass().add("highlight");
                    }
                }
                else
                {
                    getStyleClass().remove("highlight");
                }
            }
        });
        instructionsTable.refresh();
    }

}
