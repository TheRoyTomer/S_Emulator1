package jfx.ui.EmulatorScreen;

import Client_UTILS.StateRefresher;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.FunctionSelectorChoiseDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;
import jfx.ui.ExecutionComp.ExecutionCompController;
import jfx.ui.HeaderComp.HeaderCompController;
import jfx.ui.HistoryComp.HistoryCompController;
import jfx.ui.MainComp.MainCompController;
import jfx.ui.UTILS;
import jfx.ui.ViewComp.ViewCompController;
import jfx.ui.httpRequests;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Stream;

import static Client_UTILS.ClientConstants.*;

public class EmulatorScreenController
{

  /*  @FXML
    private HBox headerComp;*/
    @FXML
    private SplitPane viewComp;
    @FXML
    private VBox executionComp;


   /* @FXML
    private HeaderCompController headerCompController;*/
    @FXML
    private ViewCompController viewCompController;
    @FXML
    private ExecutionCompController executionCompController;


    private final httpRequests requests = new httpRequests(this);

    private final BooleanProperty fileLoadedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty newRunStartedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty debugModeProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isArchFitForExecute = new SimpleBooleanProperty(false);

    private final IntegerProperty currentDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty cyclesProperty = new SimpleIntegerProperty(0);
    private final LongProperty currPC_Property = new SimpleLongProperty(-1);
    private long nextPC = 0;
    private Integer currentBreakpoint = null;

    private BooleanProperty isFirstStepInDebugProperty = new SimpleBooleanProperty(false);

    private MainCompController mainCompController;


    public MainCompController getMainCompController()
    {
        return mainCompController;
    }


    @FXML
    private void initialize()
    {
        if (this.viewCompController != null && this.executionCompController != null)
        {
            viewCompController.setMainController(this);
            executionCompController.setMainController(this);

            viewCompController.bindDegrees(currentDegreeProperty, maxDegreeProperty);
            executionCompController.bindCycles(cyclesProperty);

            // Connect architecture selection to summary label
            executionCompController.getSelectedArchitectureProperty().addListener((obs, oldVal, newVal) -> {
                viewCompController.updateSummaryLabel(newVal.intValue());
            });

            currentDegreeProperty.addListener((obs, oldVal, newVal) -> {
                onDegreeChange();
            });

            newRunStartedProperty.addListener((obs, oldVal, newVal) ->
            {
                if (newVal) {
                    cyclesProperty.set(0);
                }
            });
            setupIsArchFitForExecuteBinding();

        }

    }

    public void setMainCompController(MainCompController mainCompController)
    {
        this.mainCompController = mainCompController;
    }

    public boolean getIsIsFirstStepInDebugPropertyValue()
    {
        return isFirstStepInDebugProperty.getValue();
    }

    public void setIsIsFirstStepInDebugPropertyValue(boolean value)
    {
        isFirstStepInDebugProperty.setValue(value);
    }


    public BooleanProperty isFirstStepInDebugPropertyProperty()
    {
        return isFirstStepInDebugProperty;
    }

    public BooleanProperty getIsArchFitForExecute()
    {
        return isArchFitForExecute;
    }

    public void preDisplaySelectedProgram()
    {
        fileLoadedProperty.set(true);
        currentDegreeProperty.set(0);
    }

    public void postDisplaySelectedProgram(ViewResultDTO res, int maxDegree)
    {
        InstructionsUpdate(res);
        maxDegreeProperty.set(maxDegree);
        executionCompController.resetInputFieldsState();
        //requests.loadAvailablePrograms();
    }

//Todo: what to do with that?
/*
    public void handleLoad()
    {
        currentBreakpoint = null;
    }
*/

    private void setupIsArchFitForExecuteBinding()
    {
        isArchFitForExecute.bind(Bindings.createBooleanBinding(() -> {
            return viewCompController.getMaxArchitectureValue() <= executionCompController.getSelectedArchitecture();
        }, viewCompController.getMaxInstArchProperty(), executionCompController.getSelectedArchitectureProperty()));
    }


    public BooleanProperty getFileLoadedProperty()
    {
        return fileLoadedProperty;
    }

    public BooleanProperty getNewRunStartedProperty()
    {
        return newRunStartedProperty;
    }

    public BooleanProperty getDebugModeProperty()
    {
        return debugModeProperty;
    }

    public void setNewRunStarted(boolean value)
    {
        newRunStartedProperty.set(value);
    }

    public void setDebugMode(boolean value)
    {
        debugModeProperty.set(value);
    }

    public void resetCurrAndNextPC()
    {
        currPC_Property.set(-1);
        this.nextPC = 0;
    }

    public httpRequests getRequests()
    {
        return requests;
    }


    public IntegerProperty getCurrentDegreeProperty()
    {
        return currentDegreeProperty;
    }

    public void resetCyclesProperty()
    {
        this.cyclesProperty.set(0);
    }

    public IntegerProperty getMaxDegreeProperty()
    {
        return maxDegreeProperty;
    }

    public LongProperty getCurrPC_Property()
    {
        return currPC_Property;
    }

    public void incrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() + 1);
    }

    public void decrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() - 1);
    }

    public void changeDegree(int newDegree)
    {
        currentDegreeProperty.set(newDegree);
    }

    public void setCurrentDegree(int degree)
    {
        if (degree >= 0 && degree <= maxDegreeProperty.get()) {
            currentDegreeProperty.set(degree);
        }
    }

    public void onDegreeChange()
    {
        requests.httpViewExpandedProgram(currentDegreeProperty.get());
        currentBreakpoint = null;
    }

    public void handleReRun(StatisticDTO dto)
    {
        executionCompController.onStatisticsNewRun(dto.inputs());
        currentDegreeProperty.set(dto.degree());
    }

    public void InstructionsUpdate(ViewResultDTO resDTO)
    {

        viewCompController.showTableInfo(resDTO.instructions());
        ObservableList<String> combinedList = FXCollections.observableArrayList(
                Stream.concat(
                                resDTO.usedVarsByOrder().stream()
                                        .map(VariableDTO::getVarRepresentation),
                                resDTO.usedLabelsByOrder().stream())
                        .toList()
        );
        viewCompController.updateComboBox(combinedList);

    }

    public void onDebug(List<Long> inputs)
    {

        requests.httpPreDebug(currentDegreeProperty.getValue(), inputs);

    }

    public void onExecution(List<Long> inputs, int architecture)
    {
        requests.httpExecuteProgram(currentDegreeProperty.getValue(), inputs, architecture);
    }

    public void handleExecuteRes(ExecuteResultDTO res)
    {
        if (res.isFailed())
        {
            UTILS.showError("Execution stopped: Insufficient credits. Please charge your account to continue.");
            handleStop();
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);

        }
        cyclesProperty.setValue(res.cycles());
        executionCompController.updateVarTable(res.usedVarsByOrder());
        requests.httpGetHistory(history -> {
            //historyCompController.updateHistoryTree(history);
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
            requests.httpViewExpandedProgram(currentDegreeProperty.get());
        });

    }

    public void handleResume(int architecture)
    {
        requests.httpResume((int) nextPC, architecture);
    }

    public void postResume(ExecuteResultDTO res)
    {
        if (res.isFailed())
        {
            UTILS.showError("Resume stopped: Insufficient credits.\nPlease charge your account to continue.");
            handleStop();
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
        }
        this.setIsIsFirstStepInDebugPropertyValue(false);
        resetCurrAndNextPC();
        viewCompController.refreshInstructionsTable();
        executionCompController.refreshAndClear();
        cyclesProperty.setValue(res.cycles());
        executionCompController.updateVarTable(res.usedVarsByOrder());
        requests.httpGetHistory(history -> {
            //historyCompController.updateHistoryTree(history);
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
            requests.httpViewExpandedProgram(currentDegreeProperty.get());
        });
    }

    public void handleStepOver(int architecture)
    {
        currPC_Property.set(this.nextPC);
        if (currPC_Property.get() < viewCompController.getInstructionTableSize()) {
            requests.httpStepOver(currPC_Property.get(), architecture);

        } else {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            requests.httpGetHistory(history -> {
                //historyCompController.updateHistoryTree(history);
                executionCompController.refreshAndClear();
                viewCompController.refreshInstructionsTable();
            });
        }
        
        requests.httpViewExpandedProgram(currentDegreeProperty.getValue());
    }

    public void postStepOverAction(StepOverResult res)
    {
        if (res.isFailed())
        {
            UTILS.showError("Step over stopped: Insufficient credits.\nPlease charge your account to continue.");
            handleStop();
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
            cyclesProperty.setValue(0);
            this.nextPC = 0;
            return;
        }
        cyclesProperty.setValue(cyclesProperty.get() + res.cycles());
        this.nextPC = res.nextPC();
        executionCompController.updateChangedVariables(res.changedVars());
        viewCompController.refreshInstructionsTable();
    }

    public void handleStop()
    {
        resetCurrAndNextPC();
        setDebugMode(false);
        setNewRunStarted(false);
        resetCyclesProperty();
        viewCompController.refreshInstructionsTable();
        executionCompController.postHandleStop();
    }

    public void toggleBreakpointAtLine(int lineIndex)
    {
        if (currentBreakpoint != null && currentBreakpoint == lineIndex)
        {
            currentBreakpoint = null;
        }
        else
        {
            currentBreakpoint = lineIndex;
        }

        viewCompController.refreshInstructionsTable();
    }

    public void addBreakpointFromSelectedRow()
    {
        if (currentBreakpoint == null) {UTILS.showError("Please select an instruction row first!");}
    }

    public boolean hasBreakpoint(int lineIndex)
    {
        return currentBreakpoint != null && currentBreakpoint == lineIndex;
    }

    public void clearBreakpoint()
    {
        currentBreakpoint = null;
        viewCompController.refreshInstructionsTable();
    }

    public void handleBreakPoint(int architecture)
    {
        long startFrom = this.nextPC;
        requests.httpBreakPoint(startFrom, (long) currentBreakpoint, architecture);
    }

    public void postBreakPointAction(StepOverResult res)
    {
        if (res.isFailed())
        {
            UTILS.showError("Breakpoint stopped: Insufficient credits.\nPlease charge your account to continue.");
            handleStop();
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
            cyclesProperty.setValue(0);
            this.nextPC = 0;
            return;
        }

        if (res.nextPC() < viewCompController.getInstructionTableSize()) {
            currPC_Property.set(res.nextPC() - 1);
            this.nextPC = res.nextPC();
        } else {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            requests.httpGetHistory(history -> {
               // historyCompController.updateHistoryTree(history);
                executionCompController.refreshAndClear();
            });
        }

        cyclesProperty.setValue(cyclesProperty.get() + res.cycles());
        executionCompController.updateChangedVariables(res.changedVars());

        viewCompController.refreshInstructionsTable();
        requests.httpViewExpandedProgram(currentDegreeProperty.getValue());
    }

    public void postGetInputVariablesRequest(List<VariableDTO> variablesRes)
    {
        ObservableList<VariableDTO> observableListInput = FXCollections.observableArrayList(variablesRes);
        Platform.runLater(() -> executionCompController.setinputVarsCompControllerRows(observableListInput));
    }
    

    public void postPreDebug(List<VariableDTO> variablesRes)
    {
        executionCompController.updateVarTable(variablesRes);
    }

    public void resetBreakPoint()
    {
        this.currentBreakpoint = null;
    }


    public ExecutionCompController getExecutionCompController()
    {
        return executionCompController;
    }
}

