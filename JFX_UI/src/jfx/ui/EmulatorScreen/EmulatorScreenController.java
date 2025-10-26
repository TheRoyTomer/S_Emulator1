package jfx.ui.EmulatorScreen;

import Client_UTILS.StateRefresher;
import EngineObject.StatisticDTO;
import EngineObject.VariableDTO;
import Out.ExecuteResultDTO;
import Out.FunctionSelectorChoiseDTO;
import Out.StepOverResult;
import Out.ViewResultDTO;
import javafx.application.Platform;
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

    @FXML
    private VBox headerComp;
    @FXML
    private SplitPane viewComp;
    @FXML
    private VBox executionComp;
    @FXML
    private VBox historyComp;

    @FXML
    private HeaderCompController headerCompController;
    @FXML
    private ViewCompController viewCompController;
    @FXML
    private ExecutionCompController executionCompController;
    @FXML
    private HistoryCompController historyCompController;


/*    private EngineFacade facade;*/

    private final httpRequests requests = new httpRequests(this);

    private final BooleanProperty fileLoadedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty newRunStartedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty debugModeProperty = new SimpleBooleanProperty(false);

    private final IntegerProperty currentDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty cyclesProperty = new SimpleIntegerProperty(0);
    private final LongProperty currPC_Property = new SimpleLongProperty(-1);
    private long nextPC = 0;
    private Integer currentBreakpoint = null;

    private Timer timer;
    private StateRefresher refresher;


    @FXML
    private void initialize()
    {
        if (this.headerCompController != null && this.viewCompController != null
                && this.executionCompController != null) {
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
        startPolling();
    }

    public void startPolling()
    {
        this.timer = new Timer();
        this.refresher = new StateRefresher(this);
        timer.schedule(this.refresher, 0, REFRESH_RATE);
    }

    public void stopPolling()
    {
        if (timer != null) timer.cancel();
        if (refresher != null) refresher.cancel();
    }

    public void handleLoad()
    {
        currentBreakpoint = null;
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

  /*  public void setFacade(EngineFacade facade)
    {
        this.facade = facade;
        if (headerCompController != null) {
           // headerCompController.setFacade(facade);
        }
    }*/

    public void resetCurrAndNextPC()
    {
        currPC_Property.set(-1);
        this.nextPC = 0;
    }

/*
    public EngineFacade getFacade()
    {
        return facade;
    }
*/

    public httpRequests getRequests()
    {
        return requests;
    }

    public void onProgramLoaded(List<FunctionSelectorChoiseDTO> funcInputStringsAndNames)
    {
        fileLoadedProperty.set(true);
        currentDegreeProperty.set(0);

        final String viewUrl = SERVER_URL + "/viewProgram";
        final String degreeUrl = SERVER_URL + "/GetMaxDegree";


        Request viewRequest = new Request.Builder()
                .url(viewUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(viewRequest).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String json;
                try {
                    int code = response.code();
                    json = (response.body() != null) ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        final String msg = "viewProgram failed: HTTP " + code;
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    ViewResultDTO res;
                    try {
                        res = GSON.fromJson(json, ViewResultDTO.class);
                    } catch (Exception parseEx) {
                        final String msg = "viewProgram JSON parse error: " + parseEx.getMessage();
                        Platform.runLater(() -> UTILS.showError(msg));
                        return;
                    }

                    Request maxDegreeRequest = new Request.Builder()
                            .url(degreeUrl)
                            .get()
                            .addHeader("Accept", "application/json")
                            .build();

                    HTTP_CLIENT.newCall(maxDegreeRequest).enqueue(new Callback()
                    {
                        @Override
                        public void onResponse(@NotNull Call call2, @NotNull Response response2) throws IOException
                        {
                            String json2 = null;
                            try {
                                int code2 = response2.code();
                                json2 = (response2.body() != null) ? response2.body().string() : "";

                                if (!response2.isSuccessful()) {
                                    final String msg = "GetMaxDegree failed: HTTP " + code2;
                                    Platform.runLater(() -> UTILS.showError(msg));
                                    return;
                                }

                                Map<String, Object> result;
                                try {
                                    result = GSON.fromJson(json2, Map.class);
                                } catch (Exception parseEx2) {
                                    final String msg = "GetMaxDegree JSON parse error: " + parseEx2.getMessage();
                                    Platform.runLater(() -> UTILS.showError(msg));
                                    return;
                                }

                                Object md = result.get("maxDegree");
                                if (md == null) {
                                    Platform.runLater(() -> UTILS.showError("GetMaxDegree: missing 'maxDegree' in response"));
                                    return;
                                }
                                int maxDegree = (md instanceof Number) ? ((Number) md).intValue() : Integer.parseInt(md.toString());

                                Platform.runLater(() -> {
                                    InstructionsUpdate(res);
                                    maxDegreeProperty.set(maxDegree);
                                    historyCompController.resetHistory();
                                    executionCompController.resetInputFieldsState();
                                    requests.loadAvailablePrograms();
                                });

                            } finally {
                                response2.close();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call2, @NotNull IOException e)
                        {
                            System.out.println("[CLIENT] GetMaxDegree network error: " + e.getMessage());
                            Platform.runLater(() -> UTILS.showError("Failed to get max degree: " + e.getMessage()));
                        }
                    });

                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Platform.runLater(() -> UTILS.showError("Failed to view original program: " + e.getMessage()));
            }
        });
    }


    public void changeViewedProgram(String viewedProgramName)
    {
        fileLoadedProperty.set(true);
        currentDegreeProperty.set(0);
        requests.httpChangeSelectedProgram(viewedProgramName);
    }

    public void postChangeSelectedProgram(ViewResultDTO res)
    {
        maxDegreeProperty.set(requests.httpGetMaxDegree());
        InstructionsUpdate(res);
        requests.httpGetHistory(history ->
        {
            historyCompController.updateHistoryTree(history);
            executionCompController.resetInputFieldsState();
            currentBreakpoint = null;
        });

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

    public void onExecution(List<Long> inputs)
    {
        requests.httpExecuteProgram(currentDegreeProperty.getValue(), inputs);
    }

    public void handleExecuteRes(ExecuteResultDTO res)
    {
        cyclesProperty.setValue(res.cycles());
        executionCompController.updateVarTable(res.usedVarsByOrder());
        requests.httpGetHistory(history -> {
            historyCompController.updateHistoryTree(history);
            newRunStartedProperty.set(false);
            debugModeProperty.set(false);
            requests.httpViewExpandedProgram(currentDegreeProperty.get());
        });

    }

    public void handleResume()
    {
        requests.httpResume((int) nextPC);
    }

    public void postResume(ExecuteResultDTO res)
    {
        handleExecuteRes(res);
        resetCurrAndNextPC();
        viewCompController.refreshInstructionsTable();
        executionCompController.refreshAndClear();
    }

    public void handleStepOver()
    {
        currPC_Property.set(this.nextPC);
        if (currPC_Property.get() < viewCompController.getInstructionTableSize()) {
            requests.httpStepOver(currPC_Property.get());

        } else {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            requests.httpGetHistory(history -> {
                historyCompController.updateHistoryTree(history);
                executionCompController.refreshAndClear();
                viewCompController.refreshInstructionsTable();
            });
        }
        
        requests.httpViewExpandedProgram(currentDegreeProperty.getValue());
    }

    public void postStepOverAction(StepOverResult res)
    {
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
    }

    public void toggleBreakpointAtLine(int lineIndex)
    {
        if (currentBreakpoint != null && currentBreakpoint == lineIndex) {
            currentBreakpoint = null;
        } else {
            currentBreakpoint = lineIndex;
        }

        viewCompController.refreshInstructionsTable();
    }

    public void addBreakpointFromSelectedRow()
    {
        if (currentBreakpoint == null) {
            UTILS.showError("Please select an instruction row first!");
        }
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

    public void handleBreakPoint()
    {
        long startFrom = this.nextPC;
        requests.httpBreakPoint(startFrom, (long) currentBreakpoint);
    }

    public void postBreakPointAction(StepOverResult res)
    {
        if (res.nextPC() < viewCompController.getInstructionTableSize()) {
            currPC_Property.set(res.nextPC() - 1);
            this.nextPC = res.nextPC();
        } else {
            resetCurrAndNextPC();
            debugModeProperty.set(false);
            newRunStartedProperty.set(false);
            requests.httpGetHistory(history -> {
                historyCompController.updateHistoryTree(history);
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


    public void updateProgramComboBox(List<FunctionSelectorChoiseDTO> programs)
    {
        viewCompController.updateProgramSelectorCombo(programs);
    }
}

