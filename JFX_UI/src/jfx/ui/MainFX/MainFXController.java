package jfx.ui.MainFX;

import Engine.EngineFacade;
import EngineObject.VariableDTO;
import Out.ViewResultDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;
import jfx.ui.HeaderComp.HeaderCompController;
import jfx.ui.ViewComp.ViewCompController;

import java.util.stream.Stream;

public class MainFXController {

    @FXML private VBox headerComp;
    @FXML private SplitPane viewComp;

    @FXML private HeaderCompController headerCompController;
    @FXML private ViewCompController viewCompController;

    private EngineFacade facade;


    private final IntegerProperty currentDegreeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxDegreeProperty = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        if (this.headerCompController != null && this.viewCompController != null)
        {
            headerCompController.setMainController(this);
            viewCompController.setMainController(this);

            viewCompController.bindDegrees(currentDegreeProperty, maxDegreeProperty);
        }
    }

    public void setFacade(EngineFacade facade) {
        this.facade = facade;
        if (headerCompController != null) {
            headerCompController.setFacade(facade); // propagate engine to header
        }
        if (viewCompController != null) {
            // viewCompController.setFacade(facade); // enable when needed
        }
    }

    public EngineFacade getFacade()
    {
        return facade;
    }

    public void onProgramLoaded()
    {
        currentDegreeProperty.set(0);
        maxDegreeProperty.set(facade.getMaxDegree());
        ViewResultDTO res = facade.viewOriginalProgram();
        InstructionsUpdate(res);
    }

    public IntegerProperty getCurrentDegreeProperty()
    {
        return currentDegreeProperty;
    }

    public IntegerProperty getMaxDegreeProperty()
    {
        return maxDegreeProperty;
    }

    public void incrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() + 1);
    }

    public void decrementDegree()
    {
        currentDegreeProperty.set(currentDegreeProperty.get() - 1);
    }

    public void onDegreeChange()
    {
        ViewResultDTO res = facade.viewExpandedProgram(currentDegreeProperty.get());
        InstructionsUpdate(res);
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
}
