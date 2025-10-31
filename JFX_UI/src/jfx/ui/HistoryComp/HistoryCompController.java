package jfx.ui.HistoryComp;

import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.ShowUsersComp.ShowUsersCompController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryCompController {

    private ShowUsersCompController fatherController;

    @FXML
    private TreeView<StatisticDTO> historyTreeView;

    @FXML
    private Button reRunButton;

    @FXML
    private Button viewButton;

    @FXML
    private void initialize()
    {
        TreeItem<StatisticDTO> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);
        historyTreeView.setRoot(rootItem);
        historyTreeView.setShowRoot(false);

        historyTreeView.setCellFactory(tv -> new TreeCell<StatisticDTO>() {
            @Override
            protected void updateItem(StatisticDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (getTreeItem().getParent() == getTreeView().getRoot()) {
                        setText("History #" + item.executeID());
                    } else {
                        setText(item.getStatRepresentation());
                    }
                }
            }
        });
    }

    public void setFatherController(ShowUsersCompController fatherController)
    {
        this.fatherController = fatherController;
        BooleanProperty boolProp = this.fatherController.getDebugPropertyToBind();
        if (boolProp != null)
        {
            reRunButton.disableProperty().bind(boolProp);
        }
    }

    public void updateHistoryTree(List<StatisticDTO> statistics)
    {
        TreeItem<StatisticDTO> rootItem = historyTreeView.getRoot();

        // Save expanded state BEFORE clearing
        Set<Integer> expandedExecuteIDs = new HashSet<>();
        for (TreeItem<StatisticDTO> item : rootItem.getChildren()) {
            if (item.isExpanded() && item.getValue() != null) {
                expandedExecuteIDs.add(item.getValue().executeID());
            }
        }

        // Save selected item BEFORE clearing
        TreeItem<StatisticDTO> selectedItem = historyTreeView.getSelectionModel().getSelectedItem();
        Integer selectedExecuteID = null;
        boolean isParentSelected = false;

        if (selectedItem != null && selectedItem.getValue() != null) {
            selectedExecuteID = selectedItem.getValue().executeID();
            // Check if the selected item is a parent (History #X) or child (details)
            isParentSelected = (selectedItem.getParent() == rootItem);
        }

        rootItem.getChildren().clear();

        TreeItem<StatisticDTO> itemToSelect = null;

        for (StatisticDTO stat : statistics) {
            TreeItem<StatisticDTO> historyItem = new TreeItem<>(stat);
            TreeItem<StatisticDTO> detailsItem = new TreeItem<>(stat);
            historyItem.getChildren().add(detailsItem);

            // Restore expanded state
            if (expandedExecuteIDs.contains(stat.executeID())) {
                historyItem.setExpanded(true);
            }

            // Find the item to re-select
            if (selectedExecuteID != null && stat.executeID() == selectedExecuteID) {
                itemToSelect = isParentSelected ? historyItem : detailsItem;
            }

            rootItem.getChildren().add(historyItem);
        }

        // Restore selection
        if (itemToSelect != null) {
            historyTreeView.getSelectionModel().select(itemToSelect);
        }

        historyTreeView.refresh();
    }

    public void resetHistory()
    {
        historyTreeView.getRoot().getChildren().clear();
    }

    public StatisticDTO getSelectedStatistic()
    {
        TreeItem<StatisticDTO> selectedItem = historyTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            return null;
        }
        return selectedItem.getValue();
    }

    @FXML
    void onRerun(ActionEvent event)
    {
        StatisticDTO selected = getSelectedStatistic();

        if (selected != null) {fatherController.handleReRun(selected);}
    }

    @FXML
    void onShowPress(ActionEvent event)
    {
        StatisticDTO selected = getSelectedStatistic();
        if (selected != null) {showVariablesPopup(selected.variables(), selected.executeID());}
    }

    public void showVariablesPopup(List<VariableDTO> variables, int executeID)
    {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Variables - History #" + executeID);

        TableView<VariableDTO> table = new TableView<>();

        TableColumn<VariableDTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().name()));
        nameCol.setStyle("-fx-alignment: CENTER;");



        TableColumn<VariableDTO, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().value())));
        valueCol.setStyle("-fx-alignment: CENTER;");

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        table.getColumns().addAll(nameCol, valueCol);
        table.setItems(FXCollections.observableArrayList(variables));

        popup.setScene(new Scene(new VBox(table), 350, 250));
        popup.showAndWait();
    }

}