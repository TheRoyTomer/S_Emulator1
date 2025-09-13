package jfx.ui.VarsTableView;

import EngineObject.VariableDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.*;

public class VarsTableViewController
{
    private final Map<String, VariableDTO> recentlyChangedVars = new HashMap<>();

    @FXML private TableView<VariableDTO> varsTableView;


    @FXML
    private TableColumn<VariableDTO, String> nameCol;

    @FXML
    private TableColumn<VariableDTO, Long> valueCol;

    @FXML
    private void initialize()
    {
        nameCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().name()));
        valueCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().value()));

        varsTableView.setRowFactory(tv -> new TableRow<VariableDTO>() {
            @Override
            protected void updateItem(VariableDTO item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().remove("justChangedVar");

                if (!empty && item != null) {
                    if (recentlyChangedVars.containsKey(item.name())) {
                        getStyleClass().add("justChangedVar");
                    }
                }
            }
        });


    }



    public void setRows(List<VariableDTO> list)
    {
        varsTableView.setItems(FXCollections.observableArrayList(list));
    }

    public void clearTableView()
    {
        varsTableView.getItems().clear();
    }

    public void updateWithChangedVariables(List<VariableDTO> changedVars)
    {
        recentlyChangedVars.clear();
        if(!changedVars.isEmpty())
        {
            ObservableList<VariableDTO> currentItems = varsTableView.getItems();
            recentlyChangedVars.clear();

            for (VariableDTO changedVar : changedVars)
            {
                recentlyChangedVars.put(changedVar.name(), changedVar);
            }

            for (int i = 0; i < currentItems.size(); i++)
            {
                VariableDTO currentVar = currentItems.get(i);
                VariableDTO newVar = recentlyChangedVars.get(currentVar.name());

                if (newVar != null) {
                    currentItems.set(i, newVar);
                }
            }
        }
        varsTableView.refresh();
    }

    public void refreshVarsTableView()
    {
        varsTableView.refresh();
    }

    public void clearMap()
    {
        recentlyChangedVars.clear();
    }




}
