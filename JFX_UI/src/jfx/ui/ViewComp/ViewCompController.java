package jfx.ui.ViewComp;

import EngineObject.InstructionDTO;
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
import java.util.Objects;

import javafx.collections.ListChangeListener;


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

        highlightSelectorCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("No selection")) {instructionsTableController.setRowHighlighter("");}
            else {instructionsTableController.setRowHighlighter(newVal);}
        });


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
        String v = highlightSelectorCombo.getValue();
    }

    public void updateComboBox(ObservableList<String> lst)
    {
        lst.addFirst("No selection");
        highlightSelectorCombo.setItems(lst);

        highlightSelectorCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.equals("No selection")) {
                    setText("Highlight selection");
                } else {
                    setText(item);
                }
            }
        });

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

    public ObservableList<InstructionDTO> findChainList(InstructionDTO inst)
    {
        ObservableList<InstructionDTO> list = FXCollections.observableArrayList();
        InstructionDTO holder = inst.holder();
        System.out.println(holder);
        while (holder != null) {
            list.add(holder);
            holder = holder.holder();
        }
        return list;
    }
}
