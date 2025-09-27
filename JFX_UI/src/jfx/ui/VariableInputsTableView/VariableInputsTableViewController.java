package jfx.ui.VariableInputsTableView;

import EngineObject.InstructionDTO;
import EngineObject.VariableDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableInputsTableViewController {

    @FXML private TableView<VariableDTO> variableInputsTableView;

    @FXML
    private TableColumn<VariableDTO, String> inputVarCol;

    @FXML
    private TableColumn<VariableDTO, String> valueCol;

    private final Map<Integer, String> userInputs = new HashMap<>();


    @FXML
    private void initialize()
    {

        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        inputVarCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().name()));
        inputVarCol.setCellFactory(column -> {

            return new TableCell<VariableDTO, String>() {
                {
                    setOnMousePressed(Event::consume);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
        });
        valueCol.setCellValueFactory(cd -> {
            int serial = cd.getValue().getSerial();
            String shownValue = userInputs.get(serial);
            return new ReadOnlyStringWrapper(shownValue);
        });

        valueCol.setOnEditCommit(event -> {
            VariableDTO row = event.getRowValue();
            int serial = row.getSerial();
            String newVal = event.getNewValue();
            userInputs.put(serial, newVal);
        });


    }

    public void clearTableView()
    {
        variableInputsTableView.getItems().clear();
    }

    public void clearInputVarMap()
    {
        userInputs.clear();
    }

    public Long getFromInputVarsMap(int key)
    {
            if (userInputs.containsKey(key))
            {
                String value = userInputs.get(key);
                long res =  Long.parseLong(value);
                if(res < 0 )
                {
                    throw new IllegalArgumentException(res + "is a negative number");
                }
                return Long.parseLong(value);
            }
            return 0L;
    }

    public void loadInputValues(List<Long> inputs)
    {
        for (VariableDTO var : variableInputsTableView.getItems())
        {
            int serial = var.getSerial();
            if (serial > 0 && serial <= inputs.size())
            {
                userInputs.put(serial, String.valueOf(inputs.get(serial - 1)));
            }
        }

        variableInputsTableView.refresh();
    }


    public void setRows(List<VariableDTO> list)
    {
        variableInputsTableView.setItems(FXCollections.observableArrayList(list));
    }

    public void setValueColEditable(boolean editable)
    {
        valueCol.setEditable(editable);
    }

}
