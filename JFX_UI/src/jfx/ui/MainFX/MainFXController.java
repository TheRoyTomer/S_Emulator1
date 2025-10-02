package jfx.ui.MainFX;

import Engine.EngineFacade;
import EngineObject.InstructionDTO;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.FunctionSelectorChoiseDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;
import jfx.ui.ExecutionComp.ExecutionCompController;
import jfx.ui.HeaderComp.HeaderCompController;
import jfx.ui.HistoryComp.HistoryCompController;
import jfx.ui.UTILS;
import jfx.ui.ViewComp.ViewCompController;

import java.util.List;
import java.util.stream.Stream;

public class MainFXController {

    @FXML private VBox headerComp;
    @FXML private SplitPane viewComp;
    @FXML private VBox executionComp;
    @FXML private VBox historyComp;

    @FXML private HeaderCompController headerCompController;
    @FXML private ViewCompController viewCompController;
    @FXML private ExecutionCompController executionCompController;
    @FXML private HistoryCompController historyCompController;


    private EngineFacade facade;

    private final BooleanProperty fileLoadedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty newRunStartedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty debugModeProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty currentDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty cyclesProperty = new SimpleIntegerProperty(0);
    private final LongProperty currPC_Property = new SimpleLongProperty(-1);
    private long nextPC = 0;
    private Integer currentBreakpoint = null;


    @FXML
    private void initialize() {
        if (this.headerCompController != null && this.viewCompController != null
        && this.executionCompController != null)
        {
            headerCompController.setMainController(this);
            viewCompController.setMainController(this);
            executionCompController.setMainController(this);
            historyCompController.setMainFXController(this);

            viewCompController.bindDegrees(currentDegreeProperty, maxDegreeProperty);
            executionCompController.bindCycles(cyclesProperty);

            currentDegreeProperty.addListener((obs, oldVal, newVal) -> {
                onDegreeChange();
            });

            newRunStartedProperty.addListener((obs, oldVal, newVal) ->
            {
                if (newVal) {
                    cyclesProperty.set(0);
                }
            });
        }
    }

    public void handleLoad() {currentBreakpoint = null; }


    public BooleanProperty getFileLoadedProperty() { return fileLoadedProperty; }
    public BooleanProperty getNewRunStartedProperty() { return newRunStartedProperty; }
    public BooleanProperty getDebugModeProperty() { return debugModeProperty; }

    public void setNewRunStarted(boolean value) { newRunStartedProperty.set(value); }
    public void setDebugMode(boolean value) { debugModeProperty.set(value); }

    public void setFacade(EngineFacade facade) {
        this.facade = facade;
        if (headerCompController != null)
        {
            headerCompController.setFacade(facade);
        }
    }

    public void resetCurrAndNextPC()
    {
        currPC_Property.set(-1);
        this.nextPC = 0;
    }

    public EngineFacade getFacade()
    {
        return facade;
    }

    public void onProgramLoaded(List<FunctionSelectorChoiseDTO> funcInputStringsAndNames)
    {
        fileLoadedProperty.set(true);
        currentDegreeProperty.set(0);
        ViewResultDTO res = facade.viewOriginalProgram();
        maxDegreeProperty.set(facade.getMaxDegree());
        InstructionsUpdate(res);
        historyCompController.resetHistory();
        executionCompController.resetInputFieldsState();
        viewCompController.updateProgramSelectorCombo(funcInputStringsAndNames);
    }

    public void changeViewedProgram(String viewedProgramName)
    {
        fileLoadedProperty.set(true);
        currentDegreeProperty.set(0);
        ViewResultDTO res = facade.changeSelectedProgram(viewedProgramName);
        maxDegreeProperty.set(facade.getMaxDegree());
        InstructionsUpdate(res);
        historyCompController.updateHistoryTree(facade.getHistory());
        executionCompController.resetInputFieldsState();
        currentBreakpoint = null;
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

    public LongProperty getCurrPC_Property() {return currPC_Property;}

    public void incrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() + 1);
    }

    public void decrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() - 1);
    }

    public void changeDegree(int newDegree) {currentDegreeProperty.set(newDegree);}

    public void setCurrentDegree(int degree)
    {
        if (degree >= 0 && degree <= maxDegreeProperty.get())
        {
            currentDegreeProperty.set(degree);
        }
    }

    public void onDegreeChange()
    {
        ViewResultDTO res = facade.viewExpandedProgram(currentDegreeProperty.get());
        InstructionsUpdate(res);
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

    public List<VariableDTO> getInputVariables()
    {
        return facade.getInputVariablesPreExecute();
    }

    public void onDebug(List<Long> inputs)
    {

        List<VariableDTO> usedVars = facade.preDebug(currentDegreeProperty.getValue(), inputs);
        executionCompController.updateVarTable(usedVars);

    }

    public void onExecution(List<Long> inputs)
    {
        ExecuteResultDTO res = facade.executeProgram(currentDegreeProperty.getValue(), inputs);
        handleExecuteRes(res);


    }

    private void handleExecuteRes(ExecuteResultDTO res)
    {
        cyclesProperty.setValue(res.cycles());
        executionCompController.updateVarTable(res.usedVarsByOrder());
        historyCompController.updateHistoryTree(facade.getHistory());
        newRunStartedProperty.set(false);
        debugModeProperty.set(false);

        ViewResultDTO updatedView = facade.viewExpandedProgram(currentDegreeProperty.getValue());
        InstructionsUpdate(updatedView);
    }

    public void handleResume()
    {
        ExecuteResultDTO res = facade.resumeDebug((int)nextPC);
        handleExecuteRes(res);
        resetCurrAndNextPC();
        viewCompController.refreshInstructionsTable();
        executionCompController.refreshAndClear();
    }

    public void handleStepOver()
    {
        currPC_Property.set(this.nextPC);
        if (currPC_Property.get() < viewCompController.getInstructionTableSize())
        {
            StepOverResult res = facade.stepOver(currPC_Property.get());
            cyclesProperty.setValue(cyclesProperty.get() + res.cycles());
            this.nextPC = res.nextPC();
            executionCompController.updateChangedVariables(res.changedVars());
        }
        else
        {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            historyCompController.updateHistoryTree(facade.getHistory());
            executionCompController.refreshAndClear();
        }
        viewCompController.refreshInstructionsTable();

        ViewResultDTO updatedView = facade.viewExpandedProgram(currentDegreeProperty.getValue());
        InstructionsUpdate(updatedView);


    }

    public void handleStop()
    {
        resetCurrAndNextPC();
        setDebugMode(false);
        setNewRunStarted(false);
        resetCyclesProperty();
        viewCompController.refreshInstructionsTable();
    }

    public void toggleBreakpointAtLine(int lineIndex)
    {
        if (currentBreakpoint != null && currentBreakpoint == lineIndex) {
            currentBreakpoint = null;
            UTILS.showInfo("Breakpoint removed from line " + lineIndex);
        } else {
            currentBreakpoint = lineIndex;
            UTILS.showInfo("Breakpoint set at line " + lineIndex);
        }

        viewCompController.refreshInstructionsTable();
    }

    public void addBreakpointFromSelectedRow()
    {
        if (currentBreakpoint == null) {
            UTILS.showError("Please select an instruction row first!");
        }
    }

    public boolean hasBreakpoint(int lineIndex) {
        return currentBreakpoint != null && currentBreakpoint == lineIndex;
    }

    public void clearBreakpoint() {
        currentBreakpoint = null;
        viewCompController.refreshInstructionsTable();
    }

    public void handleBreakPoint()
    {
        StepOverResult res = facade.breakPoint((long)currentBreakpoint);

        if (res.nextPC() < viewCompController.getInstructionTableSize())
        {
            currPC_Property.set(res.nextPC() - 1);
            this.nextPC = res.nextPC();
        }
        else
        {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            historyCompController.updateHistoryTree(facade.getHistory());
            executionCompController.refreshAndClear();
        }

        cyclesProperty.setValue(cyclesProperty.get() + res.cycles());
        executionCompController.updateChangedVariables(res.changedVars());

        viewCompController.refreshInstructionsTable();
        ViewResultDTO updatedView = facade.viewExpandedProgram(currentDegreeProperty.getValue());
        InstructionsUpdate(updatedView);
    }
}

