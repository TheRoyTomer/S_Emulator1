package jfx.ui.AvailableProgsAndFuncsComp;

import Out.FunctionInfoDTO;
import Out.ProgramInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import jfx.ui.DashboardScreenComp.DashboardScreenCompController;
import jfx.ui.FunctionsInfoTableView.FunctionsInfoTableViewController;
import jfx.ui.ProgramsInfoTableView.ProgramsInfoTableViewController;

import java.util.List;

public class AvailableProgsAndFuncsCompController {

    @FXML
    private Button executeProgramBtn;

    @FXML
    private Button executeFunctionBtn;

    @FXML
    private TableView<ProgramInfoDTO> programsTable;

    @FXML
    private ProgramsInfoTableViewController programsTableController;

    @FXML
    private TableView<FunctionInfoDTO> functionsTable;

    @FXML
    private FunctionsInfoTableViewController functionsTableController;
    private DashboardScreenCompController mainController;

    public void setMainController(DashboardScreenCompController mainController)
    {
        this.mainController = mainController;
    }

    @FXML
    private void initialize()
    {
        // Todo Optional: init logic
    }

    @FXML
    private void onExecuteProgram()
    {
        ProgramInfoDTO selectedProgram = programsTable.getSelectionModel().getSelectedItem();

        if (selectedProgram == null) {
            // TODO: Show error message to user - no program selected
            return;
        }
        mainController.stopPolling();
        mainController.getMainCompController().displaySelectedProgram(selectedProgram.getName());

    }

    @FXML
    private void onExecuteFunction()
    {
        FunctionInfoDTO SelectedFunction = functionsTable.getSelectionModel().getSelectedItem();

        if (SelectedFunction == null) {
            // TODO: Show error message to user - no program selected
            return;
        }
        mainController.stopPolling();
        mainController.getMainCompController().displaySelectedFunction(SelectedFunction.getName());

    }


    public void UpdateTables(List<ProgramInfoDTO> programDTOs, List<FunctionInfoDTO> functionDTOs)
    {
        System.out.println("=== AvailableProgsAndFuncsCompController: UpdateTables called ===");
        System.out.println("Received programs: " + (programDTOs != null ? programDTOs.size() : 0));
        if (programDTOs != null) {
            programDTOs.forEach(p -> System.out.println("  Received program: " + p.getName()));
        }

        if (this.programsTableController != null )
        {
            System.out.println("Calling programsTableController.setRows()");
            this.programsTableController.setRows(programDTOs);
        }
        else
        {
            System.out.println("WARNING: programsTableController is null!");
        }

        if (this.functionsTableController != null )
        {
            this.functionsTableController.setRows(functionDTOs);
        }
    }
}