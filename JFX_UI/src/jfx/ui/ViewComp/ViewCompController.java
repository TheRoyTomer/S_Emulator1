package jfx.ui.ViewComp;

import EngineObject.InstructionDTO;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import jfx.ui.InstructionTableView.InstructionsTableController;
import jfx.ui.MainFX.MainFXController;

import java.util.List;

public class ViewCompController {

    private MainFXController mainController;


    @FXML private Button programSelectorButton;
    @FXML private Button collapseButton;
    @FXML private Label currMaxDegreeLabel;
    @FXML private Button expandButton;
    @FXML private ComboBox<String> highlightSelectorCombo;
    @FXML private TableView<InstructionDTO> instructionsTable;
    @FXML private TableView<InstructionDTO> instructionHistoryChainTable;
    @FXML private InstructionsTableController instructionsTableController;
    @FXML private InstructionsTableController instructionHistoryChainTableController;
    @FXML private Label summeryLineLabel;

    @FXML
    private void initialize()
    {
        IntegerBinding countS = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(InstructionDTO::isSynthetic)
                                .count(),
                instructionsTable.itemsProperty()
        );

        IntegerBinding countB = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(i -> !i.isSynthetic())
                                .count(),
                instructionsTable.itemsProperty()
        );

        summeryLineLabel.textProperty().bind(
                Bindings.format(
                        "Basic commands: %d | Synthetic commands: %d | Total commands: %d",
                        countB,
                        countS,
                        Bindings.add(countB, countS)
                ));
    }

    public void setMainController(MainFXController mainController)
    {
        this.mainController = mainController;

        collapseButton.disableProperty().bind(
                mainController.getCurrentDegreeProperty().isEqualTo(0)
        );

        expandButton.disableProperty().bind(
                mainController.getCurrentDegreeProperty()
                        .isEqualTo(mainController.getMaxDegreeProperty())
        );

        instructionsTableController
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if (newValue == null) {instructionHistoryChainTableController.setRows(List.of());}
                    else {instructionHistoryChainTableController.setRows(findChainList(newValue));}
                });
    }

    public void bindDegrees(IntegerProperty current, IntegerProperty max)
    {
        currMaxDegreeLabel.textProperty().bind(
                Bindings.format("Degree %d / %d", current, max)
        );
    }


    public void showTableInfo(List<InstructionDTO> list)
    {
        instructionsTableController.setRows(list);
    }

    public void updateComboBox(ObservableList<String> lst)
    {
        highlightSelectorCombo.setItems(lst);
    }

        @FXML
    private void handleProgramSelector(ActionEvent event)
    {

    }

    @FXML
    private void handleCollapse(ActionEvent event)
    {
        mainController.decrementDegree();
        mainController.onDegreeChange();
    }

    @FXML
    private void handleExpand(ActionEvent event)
    {
        mainController.incrementDegree();
        mainController.onDegreeChange();
    }

   /* @FXML
    private void handleHighlightSelection(ActionEvent event)
    {
        String selected = highlightSelectorCombo.getValue();
        //ToDo: Do with CSS!!!
        instructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override
            protected void updateItem(InstructionDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.isInInstruction(selected))
                {
                    setStyle("-fx-background-color: lightblue;");
                } else {
                    setStyle("");
                }
                instructionsTable.refresh();
            }
        });
    }*/


    //ToDO: delete and use CSS!!!
    @FXML
    private void handleHighlightSelection(ActionEvent event)
    {
        String selected = highlightSelectorCombo.getValue();
        if (selected == null || selected.isEmpty()) {
            instructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
                @Override protected void updateItem(InstructionDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setStyle("");
                }
            });
            instructionsTable.refresh();
            return;
        }

        final String s = selected;

        instructionsTable.setRowFactory(tv -> new TableRow<InstructionDTO>() {
            @Override
            protected void updateItem(InstructionDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                    return;
                }

                boolean highlight = item.isInInstruction(s);

                if (highlight) {
                    setStyle("-fx-background-color: lightblue;");
                } else {
                    setStyle("");
                }
            }
        });

        instructionsTable.refresh();
    }


    public ObservableList<InstructionDTO> findChainList(InstructionDTO inst)
    {
        ObservableList<InstructionDTO> list = FXCollections.observableArrayList();
        InstructionDTO holder = inst.holder();
        System.out.println(holder);
        while (holder != null)
        {
            list.add(holder);
            holder = holder.holder();
        }
        return list;
    }
}
