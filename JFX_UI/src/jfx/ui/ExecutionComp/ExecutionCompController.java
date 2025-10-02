package jfx.ui.ExecutionComp;

import EngineObject.VariableDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import jfx.ui.UTILS;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfx.ui.MainFX.MainFXController;
import jfx.ui.VarsTableView.VarsTableViewController;
import jfx.ui.VariableInputsTableView.VariableInputsTableViewController;

import java.util.ArrayList;
import java.util.List;


public class ExecutionCompController {

    private MainFXController mainController;

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

    private final BooleanProperty stepOverStartedProperty = new SimpleBooleanProperty(false);




    @FXML
    private void initialize()
    {

    }

    public void setMainController(MainFXController mainController)
    {
        this.mainController = mainController;
        newRunButton.disableProperty().bind(mainController.getFileLoadedProperty().not()
                .or(mainController.getDebugModeProperty()));

        executeButton.disableProperty().bind(mainController.getNewRunStartedProperty().not()
                .or(mainController.getDebugModeProperty()));

        debugButton.disableProperty().bind(mainController.getNewRunStartedProperty().not()
                .or(mainController.getDebugModeProperty()));

        breakpointButton.disableProperty().bind(mainController.getDebugModeProperty().not()
                .or(getStepOverStartedProperty()));

        stepOverButton.disableProperty().bind(mainController.getDebugModeProperty().not());
        resumeButton.disableProperty().bind(mainController.getDebugModeProperty().not());
        stopButton.disableProperty().bind(mainController.getDebugModeProperty().not());
    }

    public BooleanProperty getStepOverStartedProperty() {
        return stepOverStartedProperty;
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
        inputVarsCompController.setRows(mainController.getInputVariables());
        inputVarsComp.setEditable(true);
        inputVarsCompController.setValueColEditable(true);
        setStepOverStarted(false);

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
        mainController.handleBreakPoint();

    }

    public List<Long> handleInputs()
    {
        List<Long> res = new ArrayList<>();
        int counter = 1;
        int newListSize = mainController.getInputVariables().getLast().getSerial();
        ObservableList<VariableDTO> lst = inputVarsComp.getItems();

        while (counter <= newListSize)
        {
            res.add(inputVarsCompController.getFromInputVarsMap(counter));
            counter++;
        }
        return res;

    }

    @FXML
    private void onDebugPress(ActionEvent e)
    {
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
        try {
            mainController.onExecution(handleInputs());
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
        mainController.handleResume();
        setStepOverStarted(false);
    }

    @FXML
    private void onStop(ActionEvent e)
    {
        mainController.handleStop();
        setStepOverStarted(false);
        inputVarsCompController.clearInputVarMap();
        inputVarsCompController.clearTableView();
        varTableCompController.clearTableView();
        varTableCompController.clearMap();


    }

    @FXML
    private void onStepOver(ActionEvent e)
    {
        mainController.handleStepOver();
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

    public void onStatisticsNewRun(List<Long> inputs)
    {
        onNewRun(null);
        inputVarsCompController.loadInputValues(inputs);
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



}
