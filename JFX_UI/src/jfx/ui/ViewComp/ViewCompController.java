package jfx.ui.ViewComp;

import EngineObject.InstructionDTO;
import Out.FunctionSelectorChoiseDTO;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import jfx.ui.InstructionTableView.InstructionsTableController;
import jfx.ui.EmulatorScreen.EmulatorScreenController;

import java.util.List;


public class ViewCompController {

    private EmulatorScreenController mainController;

    private boolean isUpdatingDegreeSelector = false;

    @FXML private Button collapseButton;
    @FXML private Label currMaxDegreeLabel;
    @FXML private Button expandButton;
    @FXML private ComboBox<String> highlightSelectorCombo;
    @FXML private TableView<InstructionDTO> instructionsTable;
    @FXML private TableView<InstructionDTO> instructionHistoryChainTable;
    @FXML private InstructionsTableController instructionsTableController;
    @FXML private InstructionsTableController instructionHistoryChainTableController;
    @FXML private HBox summeryLineLabel;

    private IntegerBinding countI;
    private IntegerBinding countII;
    private IntegerBinding countIII;
    private IntegerBinding countIV;
    private IntegerBinding totalCount;

    @FXML private ComboBox<Integer> DegreeSelectorComboBox;

    private final IntegerProperty MaxInstArch = new SimpleIntegerProperty(1);


    @FXML
    private void initialize()
    {
        countI = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(i -> i.archType() == 1)
                                .count(),
                instructionsTable.itemsProperty()
        );

        countII = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(i -> i.archType() == 2)
                                .count(),
                instructionsTable.itemsProperty()
        );

        countIII = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(i -> i.archType() == 3)
                                .count(),
                instructionsTable.itemsProperty()
        );

        countIV = Bindings.createIntegerBinding(() ->
                        (int) instructionsTable.getItems().stream()
                                .filter(i -> i.archType() == 4)
                                .count(),
                instructionsTable.itemsProperty()
        );

        totalCount = Bindings.createIntegerBinding(() ->
                        instructionsTable.getItems().size(),
                instructionsTable.itemsProperty()
        );

        // Add listeners to update the TextFlow when counts change
        countI.addListener((obs, oldVal, newVal) -> updateSummaryLabelFromCurrentArchitecture());
        countII.addListener((obs, oldVal, newVal) -> updateSummaryLabelFromCurrentArchitecture());
        countIII.addListener((obs, oldVal, newVal) -> updateSummaryLabelFromCurrentArchitecture());
        countIV.addListener((obs, oldVal, newVal) -> updateSummaryLabelFromCurrentArchitecture());
        totalCount.addListener((obs, oldVal, newVal) -> updateSummaryLabelFromCurrentArchitecture());
        setupMaxArchitectureBinding();

        if (instructionsTableController != null) {instructionsTableController.setViewController(this);}

        highlightSelectorCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            instructionsTableController.refreshTable();
        });

        // Set custom button cell for Degree Selector to always show "Degree Selector"
        DegreeSelectorComboBox.setButtonCell(new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText("Degree Selector");
            }
        });

        DegreeSelectorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!isUpdatingDegreeSelector && newVal != null && mainController != null) {
                mainController.setCurrentDegree(newVal);
            }
        });
    }

    public void setMainController(EmulatorScreenController mainController)
    {
        this.mainController = mainController;

        collapseButton.disableProperty().bind(
                mainController.getCurrentDegreeProperty().isEqualTo(0)
                        .or(mainController.getFileLoadedProperty().not())
                        .or(mainController.getDebugModeProperty())
        );

        expandButton.disableProperty().bind(
                mainController.getCurrentDegreeProperty()
                        .isEqualTo(mainController.getMaxDegreeProperty())
                        .or(mainController.getFileLoadedProperty().not())
                        .or(mainController.getDebugModeProperty())
        );


        highlightSelectorCombo.disableProperty().bind(mainController.getFileLoadedProperty().not()
                .or(mainController.getDebugModeProperty()));

        DegreeSelectorComboBox.disableProperty().bind(
                mainController.getFileLoadedProperty().not()
                        .or(mainController.getDebugModeProperty()));

        mainController.getMaxDegreeProperty().addListener((obs, oldVal, newVal) -> {
            updateDegreeComboBoxItems(newVal.intValue());});

        mainController.getCurrentDegreeProperty().addListener((obs, oldVal, newVal) -> {
            isUpdatingDegreeSelector = true;
            DegreeSelectorComboBox.setValue(newVal.intValue());
            isUpdatingDegreeSelector = false;});


        instructionsTableController
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if (newValue == null) {instructionHistoryChainTableController.setRows(List.of());}
                    else {instructionHistoryChainTableController.setRows(findChainList(newValue));}
                });

        if (instructionHistoryChainTableController != null)
        {
            instructionHistoryChainTableController.setViewController(this);
            instructionHistoryChainTableController.disableSelection();

        }


    }

    public EmulatorScreenController getMainController()
    {
        return mainController;
    }

    public int getMaxArchitectureValue()
    {
        return MaxInstArch.getValue();
    }

    private void setupMaxArchitectureBinding()
    {
        MaxInstArch.bind(Bindings.createIntegerBinding(() -> {
            if (countIV.get() != 0) {return 4;}
            else if (countIII.get() != 0) {return 3;}
            else if (countII.get() != 0) {return 2;}
            else if (countI.get() != 0) {return 1;}
            else {return 0;}
        }, countI, countII, countIII, countIV));
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
    private void handleCollapse(ActionEvent event)
    {
        mainController.decrementDegree();
    }

    @FXML
    private void handleExpand(ActionEvent event)
    {
        mainController.incrementDegree();
    }
    @FXML
    private void OnDegreeSelection(ActionEvent event){/*mainController.changeDegree(this.DegreeSelectorComboBox.getValue());*/}

    public ObservableList<InstructionDTO> findChainList(InstructionDTO inst)
    {
        ObservableList<InstructionDTO> list = FXCollections.observableArrayList();
        InstructionDTO holder = inst.holder();
        while (holder != null) {
            list.add(holder);
            holder = holder.holder();
        }
        return list;
    }

    public String getHighlightSelection()
    {
        return highlightSelectorCombo.getValue();
    }

    public void refreshInstructionsTable()
    {
        instructionsTableController.refreshTable();
    }

    public InstructionDTO getSelectedInstruction()
    {
        return instructionsTableController.selectedItemProperty().get();
    }

    public int getInstructionTableSize()
    {
        return instructionsTableController.getInstructionTableSize();
    }


    private void updateDegreeComboBoxItems(int maxDegree)
    {
        ObservableList<Integer> degrees = FXCollections.observableArrayList();
        for (int i = 0; i <= maxDegree; i++) {degrees.add(i);}

        isUpdatingDegreeSelector = true;
        DegreeSelectorComboBox.setItems(degrees);
        if (mainController != null)
        {
            int currentDegree = mainController.getCurrentDegreeProperty().get();
            if (currentDegree <= maxDegree) {
                DegreeSelectorComboBox.setValue(currentDegree);
            }
        }
        isUpdatingDegreeSelector = false;
    }

    public void updateSummaryLabel(int selectedArchitecture)
    {
        summeryLineLabel.getChildren().clear();

        Text iText = new Text("I: " + countI.get());
        Text sep1 = new Text(" | ");
        Text iiText = new Text("II: " + countII.get());
        Text sep2 = new Text(" | ");
        Text iiiText = new Text("III: " + countIII.get());
        Text sep3 = new Text(" | ");
        Text ivText = new Text("IV: " + countIV.get());
        Text sep4 = new Text(" | ");
        Text totalText = new Text("Total Commands: " + totalCount.get());

        // Color texts based on selected architecture (0 means no selection)
        // Red only if:
        // 1) instructions exist
        // AND
        // 2) selected architecture is insufficient
        if (selectedArchitecture > 0)
        {
            if (countII.get() > 0 && selectedArchitecture < 2) {iiText.setFill(Color.RED);}
            if (countIII.get() > 0 && selectedArchitecture < 3) {iiiText.setFill(Color.RED);}
            if (countIV.get() > 0 && selectedArchitecture < 4) {ivText.setFill(Color.RED);}
        }

        summeryLineLabel.getChildren().addAll(iText, sep1, iiText, sep2, iiiText, sep3, ivText, sep4, totalText);
    }

    public IntegerProperty getMaxInstArchProperty()
    {
        return MaxInstArch;
    }

    private void updateSummaryLabelFromCurrentArchitecture()
    {
        if (mainController != null) {
            int arch = mainController.getExecutionCompController().getSelectedArchitecture();
            updateSummaryLabel(arch);
        } else {
            updateSummaryLabel(0);
        }
    }

    // בתוך ViewCompController

    public void selectAndScrollInstruction(int rowIndex) {
        // ריצה בטוחה על JavaFX Application Thread
        if (!javafx.application.Platform.isFxApplicationThread()) {
            javafx.application.Platform.runLater(() -> selectAndScrollInstruction(rowIndex));
            return;
        }

        if (instructionsTable == null || instructionsTable.getItems() == null || instructionsTable.getItems().isEmpty()) {
            return;
        }

        // מחוץ לטווח -> מנקה היילייט
        if (rowIndex < 0 || rowIndex >= instructionsTable.getItems().size()) {
            instructionsTable.getSelectionModel().clearSelection();
            return;
        }

        // בוחרים ומגלגלים קצת כדי לראות את השורה
        instructionsTable.getSelectionModel().clearSelection();
        instructionsTable.getSelectionModel().select(rowIndex);
        instructionsTable.scrollTo(Math.max(0, rowIndex - 3)); // אפשר גם scrollTo(rowIndex) אם מעדיפים
    }

    public void clearInstructionHighlight() {
        if (!javafx.application.Platform.isFxApplicationThread()) {
            javafx.application.Platform.runLater(this::clearInstructionHighlight);
            return;
        }
        if (instructionsTable != null) {
            instructionsTable.getSelectionModel().clearSelection();
        }
    }

}
