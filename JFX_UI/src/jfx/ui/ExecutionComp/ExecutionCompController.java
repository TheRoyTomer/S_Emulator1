package jfx.ui.ExecutionComp;

import EngineObject.VariableDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import jfx.ui.MainComp.MainCompController;
import jfx.ui.UTILS;
import jfx.ui.EmulatorScreen.EmulatorScreenController;
import jfx.ui.VarsTableView.VarsTableViewController;
import jfx.ui.VariableInputsTableView.VariableInputsTableViewController;

import java.util.ArrayList;
import java.util.List;


public class ExecutionCompController {

    private EmulatorScreenController mainController;

    @FXML private Button breakpointButton;
    @FXML private Button newRunButton;
    @FXML private Button executeButton;
    @FXML private Button debugButton;

    @FXML private Button resumeButton;
    @FXML private Button stopButton;
    @FXML private Button stepOverButton;

    @FXML private Label cyclesLabel;

    @FXML private TableView<VariableDTO> varTableComp;
    @FXML private TableView<VariableDTO> inputVarsComp;

    @FXML private VarsTableViewController varTableCompController;
    @FXML private VariableInputsTableViewController inputVarsCompController;

    @FXML private Button backToDashboardButton;

    @FXML private ComboBox<String> architectureSelectionComboBox;
    private final BooleanProperty stepOverStartedProperty = new SimpleBooleanProperty(false);
    private final IntegerProperty selectedArchitectureProperty = new SimpleIntegerProperty(0);

    // Flag to prevent infinite loop in bidirectional binding
    private boolean isUpdatingArchitecture = false;




    @FXML
    private void initialize()
    {
        // Initialize architecture ComboBox with Roman numerals
        architectureSelectionComboBox.setItems(FXCollections.observableArrayList("I", "II", "III", "IV"));

        // Set prompt text instead of default value
        architectureSelectionComboBox.setPromptText("Architecture");

        // Set custom button cell to show prompt text when null
        architectureSelectionComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Architecture");
                } else {
                    setText(item);
                }
            }
        });

        // Bidirectional binding between ComboBox (String) and Property (Integer)
        StringConverter<Integer> converter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer archType) {
                // Integer → String (Property → ComboBox)
                if (archType == null || archType == 0) return null;
                return switch (archType) {
                    case 1 -> "I";
                    case 2 -> "II";
                    case 3 -> "III";
                    case 4 -> "IV";
                    default -> null;
                };
            }

            @Override
            public Integer fromString(String archString) {
                // String → Integer (ComboBox → Property)
                if (archString == null) return 0;
                return switch (archString) {
                    case "I" -> 1;
                    case "II" -> 2;
                    case "III" -> 3;
                    case "IV" -> 4;
                    default -> 0;
                };
            }
        };

        // Manual bidirectional binding with loop prevention
        architectureSelectionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!isUpdatingArchitecture) {
                isUpdatingArchitecture = true;
                selectedArchitectureProperty.set(converter.fromString(newVal));
                isUpdatingArchitecture = false;
            }
        });

        selectedArchitectureProperty.addListener((obs, oldVal, newVal) -> {
            if (!isUpdatingArchitecture) {
                isUpdatingArchitecture = true;
                architectureSelectionComboBox.setValue(converter.toString(newVal.intValue()));
                isUpdatingArchitecture = false;
            }
        });
    }

    public void setMainController(EmulatorScreenController mainController)
    {
        this.mainController = mainController;
        newRunButton.disableProperty().bind(mainController.getFileLoadedProperty().not()
                .or(mainController.getDebugModeProperty()));

        executeButton.disableProperty().bind(mainController.getNewRunStartedProperty().not()
                .or(mainController.getDebugModeProperty()).or(mainController.getIsArchFitForExecute().not()));

        debugButton.disableProperty().bind(mainController.getNewRunStartedProperty().not()
                .or(mainController.getDebugModeProperty()).or(mainController.getIsArchFitForExecute().not()));

        breakpointButton.disableProperty().bind(mainController.getDebugModeProperty().not()
                /* .or(getStepOverStartedProperty())*/);

        stepOverButton.disableProperty().bind(mainController.getDebugModeProperty().not());
        resumeButton.disableProperty().bind(mainController.getDebugModeProperty().not());
        stopButton.disableProperty().bind(mainController.getDebugModeProperty().not());
        backToDashboardButton.disableProperty().bind(mainController.getDebugModeProperty());
        architectureSelectionComboBox.disableProperty().bind(mainController.getNewRunStartedProperty().not().or(mainController.getDebugModeProperty()));

    }

    public BooleanProperty getStepOverStartedProperty()
    {
        return stepOverStartedProperty;
    }

    public IntegerProperty getSelectedArchitectureProperty()
    {
        return selectedArchitectureProperty;
    }

    public int getSelectedArchitecturePropertyValue()
    {
        return selectedArchitectureProperty.getValue();
    }

    public int getSelectedArchitecture()
    {
        return selectedArchitectureProperty.get();
    }

    public void setStepOverStarted(boolean value) {
        stepOverStartedProperty.set(value);
    }

    @FXML
    private void onNewRun(ActionEvent e)
    {
        mainController.setNewRunStarted(true);
        inputVarsCompController.clearInputVarMap();
        inputVarsCompController.clearTableView();
        varTableCompController.clearTableView();
        mainController.resetCyclesProperty();
        inputVarsComp.setEditable(true);
        inputVarsCompController.setValueColEditable(true);
        setStepOverStarted(false);
        mainController.setIsIsFirstStepInDebugPropertyValue(true);
        mainController.getRequests().httpGetInputVariables();
    }

    public void setinputVarsCompControllerRows(ObservableList<VariableDTO> inputVarsList)
    {
        System.out.println(inputVarsList.toString());
        inputVarsCompController.setRows(inputVarsList);
    }

    public void resetInputFieldsState()
    {
        inputVarsComp.setEditable(false);
        inputVarsCompController.setValueColEditable(false);
        inputVarsCompController.clearInputVarMap();
        inputVarsCompController.clearTableView();
        varTableCompController.clearTableView();
        mainController.resetCyclesProperty();
    }

    @FXML
    private void onBreakpointPress(ActionEvent e)
    {
        mainController.addBreakpointFromSelectedRow();
        mainController.handleBreakPoint(getSelectedArchitecture());
    }

    public List<Long> handleInputs()
    {
        List<Long> res = new ArrayList<>();
        int counter = 1;
        //int newListSize = mainController.httpGetInputVariables().getLast().getSerial();
        ObservableList<VariableDTO> items = inputVarsComp.getItems();
        int maxSerial = items.stream()
                .mapToInt(VariableDTO::getSerial)
                .max()
                .orElse(0);
        ObservableList<VariableDTO> lst = inputVarsComp.getItems();

        while (counter <= maxSerial)
        {
            res.add(inputVarsCompController.getFromInputVarsMap(counter));
            counter++;
        }
        return res;

    }

    @FXML
    private void onDebugPress(ActionEvent e)
    {
        if (!this.preRunCreditsValidation()
                && mainController.getIsIsFirstStepInDebugPropertyValue()) {return;}

        try {
            mainController.onDebug(handleInputs());
            inputVarsComp.setEditable(false);
            mainController.setDebugMode(true);

        } catch (NumberFormatException ex) {
            UTILS.showError(ex.getMessage() + " Is not a number");
        }
        catch (IllegalArgumentException ex)
        {
            UTILS.showError(ex.getMessage());
        }

    }

    @FXML
    private void onExecutePress(ActionEvent e)
    {
        if (!this.preRunCreditsValidation()) {return;}

        try {
            mainController.onExecution(handleInputs(), getSelectedArchitecture());
            inputVarsComp.setEditable(false);

        } catch (NumberFormatException ex) {
            UTILS.showError(ex.getMessage() + " Is not a number");
        }
        catch (IllegalArgumentException ex)
        {
            UTILS.showError(ex.getMessage());
        }

    }

    @FXML
    private void onResume(ActionEvent e)
    {
        mainController.handleResume(getSelectedArchitecture());
        setStepOverStarted(false);
    }

    @FXML
    private void onStop(ActionEvent e)
    {
        mainController.handleStop();
    }

    public void postHandleStop()
    {
        setStepOverStarted(false);
        inputVarsCompController.clearInputVarMap();
        inputVarsCompController.clearTableView();
        varTableCompController.clearTableView();
        varTableCompController.clearMap();
    }

    @FXML
    private void onStepOver(ActionEvent e)
    {
        mainController.handleStepOver(getSelectedArchitecture());
        setStepOverStarted(true);
    }

    public void updateVarTable(List<VariableDTO> usedVars)
    {
        varTableCompController.setRows(usedVars);
    }

    public void bindCycles(IntegerProperty cyclesPro)
    {
        cyclesLabel.textProperty().bind(
                Bindings.format("Cycles: %d", cyclesPro)
        );
    }

   /* public void onStatisticsNewRun(List<Long> inputs, int archType, List<VariableDTO> inputVarsList)
    {
        onNewRun(null);
        selectedArchitectureProperty.set(archType); // Bidirectional binding will update ComboBox
        inputVarsCompController.loadInputValues(inputs, inputVarsList);
    }*/

    public void onStatisticsNewRun(List<Long> inputs, int archType, List<VariableDTO> inputVarsList)
    {
        // Initialize state like onNewRun but without HTTP call
        mainController.setNewRunStarted(true);
        inputVarsCompController.clearInputVarMap();
        inputVarsCompController.clearTableView();
        varTableCompController.clearTableView();
        mainController.resetCyclesProperty();
        inputVarsComp.setEditable(true);
        inputVarsCompController.setValueColEditable(true);
        setStepOverStarted(false);
        mainController.setIsIsFirstStepInDebugPropertyValue(true);

        // Set architecture
        selectedArchitectureProperty.set(archType); // Bidirectional binding will update ComboBox

        // Directly set the input variables (no HTTP needed - we already have them!)
        inputVarsCompController.setRows(inputVarsList);

        // Load the values into the table
        inputVarsCompController.loadInputValues(inputs, inputVarsList);
    }

    public void updateChangedVariables(List<VariableDTO> changedVars)
    {
        varTableCompController.updateWithChangedVariables(changedVars);
    }

    public void refreshVarsTable() {varTableComp.refresh();}

    public void refreshAndClear()
    {
        varTableComp.refresh();
        varTableCompController.clearMap();
    }

    @FXML
    private void onBackToDashboard(ActionEvent e)
    {
        resetArchitectureSelection();
        mainController.getMainCompController().loadDashboardScreen();

    }

    public void resetArchitectureSelection()
    {
        architectureSelectionComboBox.setValue(null);
        selectedArchitectureProperty.set(0);
        mainController.resetBreakPoint();

    }

    public boolean preRunCreditsValidation()
    {
        MainCompController mainCompController = mainController.getMainCompController();
        if (mainCompController != null && mainCompController.getCurrentProgramInfo() != null
                && mainCompController.getCredits() < mainCompController.getCurrentProgramInfo().getAvgCreditCost())
        {

            UTILS.showInfo("Your credit balance is lower than the average cost of running this program.\n" +
                    "Please load more credits to run it.");
            return false;
        }
        return true;
    }

}