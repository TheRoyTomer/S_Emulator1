package jfx.ui.VarsTableView;

import EngineObject.InstructionDTO;
import EngineObject.VariableDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class VarsTableViewController
{

    @FXML private TableView<VariableDTO> varsTableView;


    @FXML
    private TableColumn<VariableDTO, String> nameCol;

    @FXML
    private TableColumn<VariableDTO, Long> valueCol;

    private void initialize()
    {
        nameCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().name()));
        valueCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().value()));
    }



    public void setRows(List<VariableDTO> list)
    {
        varsTableView.setItems(FXCollections.observableArrayList(list));
    }

}
