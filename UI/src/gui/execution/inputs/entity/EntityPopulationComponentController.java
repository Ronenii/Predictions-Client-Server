package gui.execution.inputs.entity;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.prview.PreviewData;
import gui.api.event.handler.BarNotifier;
import gui.execution.inputs.InputsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jaxb.event.FileLoadedEvent;

public class EntityPopulationComponentController implements FileLoadedEvent, BarNotifier {

    private static final int POPULATION_ERROR = -1;
    private BarNotifier notificationBar;
    private InputsController mainController;
    @FXML
    private TextField populationTF;

    @FXML
    private Button setBTN;

    @FXML
    private ListView<DTOEntity> entitiesLV;

    @FXML
    private Label entityLabel;

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    public void initialize(){
        notificationBar = getNotificationBarParent();
    }

    @Override
    public BarNotifier getNotificationBarParent() {
        return mainController.getNotificationBarParent();
    }

    @FXML
    void populationTextFieldActionListener(ActionEvent event) {

    }

    @FXML
    void setButtonActionListener(ActionEvent event) {
        int population = getTextFieldInput();



    }

    private int getTextFieldInput(){
        try{
            int result = Integer.parseInt(populationTF.getText());
            if(result < 0){
                throw new Exception();
            }
            return result;
        }catch (Exception e){
            notificationBar.showNotification("The population value may only be a non negative Integer.");
            return POPULATION_ERROR;
        }
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        entitiesLV.getItems().addAll(previewData.getEntities());

        entitiesLV.setCellFactory(new Callback<ListView<DTOEntity>, ListCell<DTOEntity>>() {
            @Override
            public ListCell<DTOEntity> call(ListView<DTOEntity> listView) {
                return new ListCell<DTOEntity>() {
                    @Override
                    protected void updateItem(DTOEntity item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };

            }
        });
    }
}