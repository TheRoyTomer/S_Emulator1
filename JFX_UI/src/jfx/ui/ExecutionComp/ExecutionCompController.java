package jfx.ui.ExecutionComp;

import EngineObject.VariableDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import jfx.ui.MainFX.MainFXController;
import jfx.ui.VarsTableView.VarsTableViewController;
import jfx.ui.VariableInputsTableView.VariableInputsTableViewController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionCompController {

    private MainFXController mainController;


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



    @FXML
    private void initialize()
    {

    }

    public void setMainController(MainFXController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void onNewRun(ActionEvent e)
    {
        inputVarsCompController.clearInputVarsMap();
        inputVarsCompController.setRows(mainController.getInputVariables());
        inputVarsComp.setEditable(true);
        inputVarsCompController.setValueColEditable(true);
    }

    //Todo: Move to Utils
    private void showError(String msg)
    {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Load Error");
        a.setContentText(msg);
        a.showAndWait();
    }

    public List<Long> handleInputs()
    {
        List<Long> res = new ArrayList<>();
        int counter = 1;
        int newListSize = mainController.getInputVariables().getLast().getSerial();
        ObservableList<VariableDTO> lst = inputVarsComp.getItems();
        try {
            while (counter <= newListSize)
            {
                res.add(inputVarsCompController.getFromInputVarsMap(counter));
                counter++;
            }
            return res;
        } catch (NumberFormatException e) {
            showError(e.getMessage() + " Is not a number");
            return List.of();
        }
        catch (IllegalArgumentException e)
        {
            showError(e.getMessage());
            return List.of();
        }
    }

    @FXML
    private void onDebugPress(ActionEvent e) { }

    @FXML
    private void onExecutePress(ActionEvent e)
    {
        System.out.println(handleInputs());
    }

    @FXML
    private void onResume(ActionEvent e) { }

    @FXML
    private void onStop(ActionEvent e) { }

    @FXML
    private void onStepOver(ActionEvent e) { }
}
