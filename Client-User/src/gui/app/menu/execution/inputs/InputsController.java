package gui.app.menu.execution.inputs;
import gui.api.Controller;
import gui.app.menu.execution.inputs.entity.EntityPopulationComponentController;
import gui.app.menu.execution.inputs.env.var.EnvironmentVariableComponentController;
import gui.app.menu.execution.models.EntitiesStartData;
import gui.app.menu.execution.models.EnvironmentVarsStartData;
import gui.app.menu.execution.models.StartDetails;
import gui.app.menu.execution.NewExecutionComponentController;
import gui.app.menu.request.data.RequestData;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.UserServerAgent;
import server2client.simulation.prview.PreviewData;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public class InputsController implements Controller {
    private NewExecutionComponentController mainController;
    @FXML private GridPane entityPopulationComponent;
    @FXML private EntityPopulationComponentController entityPopulationComponentController;
    @FXML private GridPane environmentVariablesComponent;
    @FXML private EnvironmentVariableComponentController environmentVariableComponentController;


    private int currentReqId;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){
        if(environmentVariableComponentController != null && entityPopulationComponentController != null){
            entityPopulationComponentController.setMainController(this);
            environmentVariableComponentController.setMainController(this);
        }
    }

    public void clearInputs(){
        entityPopulationComponentController.clearInputs();
        environmentVariableComponentController.clearInputs();
    }

    public StartDetails getStartDetails(Map<String, Object> envVarsValuesMap) {
        EntitiesStartData entitiesStartData = entityPopulationComponentController.getEntitiesStartData();
        EnvironmentVarsStartData environmentVarsStartData = new EnvironmentVarsStartData(envVarsValuesMap);
        return new StartDetails(entitiesStartData, environmentVarsStartData);
    }

    public void setUpExecutionWindowWithPreviewData(PreviewData previewData, RequestData requestData) {
        currentReqId = requestData.getRequestId();
        environmentVariableComponentController.loadEnvVarsDetails(previewData);
        entityPopulationComponentController.loadEntitiesDet(previewData);
    }

    public int getCurrentReqId() {
        return currentReqId;
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
