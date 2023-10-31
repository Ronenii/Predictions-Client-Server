package gui.app.menu.execution.result.statistics;

import gui.app.menu.execution.result.ResultTabComponentController;
import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.objects.DTOEntityInstance;
import server2client.simulation.genral.impl.properties.DTOProperty;
import gui.app.api.Controller;
import gui.app.menu.execution.result.data.HistogramData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import simulation.properties.property.api.PropertyType;

import java.util.*;

public class StatisticsComponentController implements Controller {
    private Controller mainController;

    @FXML
    private TreeView<String> entityTreeView;

    @FXML
    private TableView<HistogramData> histogramTableView;

    @FXML
    private TableColumn<HistogramData, String> valueCol;

    @FXML
    private TableColumn<HistogramData, String> quantityCol;

    @FXML
    private Label consistencyLabel;

    @FXML
    private HBox averageHbox;

    @FXML
    private Label AverageLabel;

    private DTOEntity[] entities;

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }

    public void setMainController(ResultTabComponentController controller) {
        this.mainController = controller;
    }

    /**
     * TODO: Load the result data entities and properties into the tree view.
     * Each property in the tree view will fill out the histogram table view and the statistics about it.
     */
    public void loadComponent(DTOEntity[] entities) {
        clearComponent();
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        this.entities = entities;
        updateEntitiesTreeView();
    }

    ;

    public void clearComponent() {
        entityTreeView.setRoot(null);
        AverageLabel.setText("");
        consistencyLabel.setText("");
        histogramTableView.getItems().clear();
    }

    /**
     * Add the simulation entities and their properties to the tree view.
     */
    private void updateEntitiesTreeView() {
        TreeItem<String> root = new TreeItem<>("Entities");

        entityTreeView.setRoot(root);
        for (DTOEntity entity : entities) {
            TreeItem<String> entityToAdd = new TreeItem<>(entity.getName());
            entityToAdd.getChildren().addAll(getPropertiesTreeItems(entity.getProperties()));
            root.getChildren().add(entityToAdd);
        }
    }

    private List<TreeItem<String>> getPropertiesTreeItems(DTOProperty[] properties) {
        List<TreeItem<String>> propertiesList = new ArrayList<>();

        for (DTOProperty property : properties) {
            TreeItem<String> propertyToAdd = new TreeItem<>(property.getName());
            propertiesList.add(propertyToAdd);
        }

        return propertiesList;
    }

    @FXML
    void onTreeViewItemSelected(MouseEvent event) {
//        TreeItem<String> selectedItem = entityTreeView.getSelectionModel().getSelectedItem();
//        DTOProperty chosenProperty;
//
//        if(selectedItem != null) {
//            if (selectedItem.isLeaf()) {
//                chosenProperty = getDTOPropertyByName(selectedItem.getParent().getValue(), selectedItem.getValue());
//                updateHistogramTableView(chosenProperty, selectedItem.getParent().getValue());
//                updateConsistencyLbl(selectedItem.getValue(), selectedItem.getParent().getValue(), getHasPopulation(getDTOEntityByName(selectedItem.getParent().getValue())));
//                updateAverageLbl(selectedItem.getParent().getValue(), chosenProperty.getName(), chosenProperty.getType());
//            }
//        }
    }


    private DTOEntity getDTOEntityByName(String entityName) {
        for (DTOEntity entity : entities) {
            if (entity.getName().equals(entityName)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Return true if the entity's population is not empty.
     */
    private boolean getHasPopulation(DTOEntity entity) {
        return entity.getInstances().length != 0;
    }

    private DTOProperty getDTOPropertyByName(String entityName, String propertyName) {
        for (DTOEntity entity : entities) {
            if (entity.getName().equals(entityName)) {
                for (DTOProperty property : entity.getProperties()) {
                    if (property.getName().equals(propertyName)) {
                        return property;
                    }
                }
            }
        }
        return null;
    }

    /**
     * update the histogram table view according to the given property DTO.
     */
    public void updateHistogramTableView(DTOProperty property, String entityName) {
        DTOEntity entity = getDTOEntityByName(entityName);
        Map<Object, Integer> histogram = createHistogram(property, entity);
        PropertyType type = PropertyType.valueOf(property.getType());

        histogramTableView.getItems().clear();
        switch (type) {
            case DECIMAL:
                setDataHistogramTableViewByClass(histogram, Integer.class);
                break;
            case FLOAT:
                setDataHistogramTableViewByClass(histogram, Double.class);
                break;
            case BOOLEAN:
                setDataHistogramTableViewByClass(histogram, Boolean.class);
                break;
            case STRING:
                setDataHistogramTableViewByClass(histogram, String.class);
                break;
        }
    }

    private void setDataHistogramTableViewByClass(Map<Object, Integer> histogram, Class castingClass) {
        for (Object object : histogram.keySet()) {
            HistogramData histogramData;
            if (castingClass == Double.class) {
                String value = String.format("%.2f", castingClass.cast(object));
                histogramData = new HistogramData(value, histogram.get(object));
            } else {
                histogramData = new HistogramData(castingClass.cast(object).toString(), histogram.get(object));
            }
            histogramTableView.getItems().add(histogramData);
        }
    }


    /**
     * Creates a histogram from the given property and entity's instances.
     * Sorts the histogram by key in ascending order.
     *
     * @param dtoProperty The property to build a histogram of
     * @param entity      We use this to go over all living instances of this entity and extract
     *                    the property's info.
     * @return A histogram map where the key is the value of the property, and the map's value
     * is the number of times the key appears.
     */
    private Map<Object, Integer> createHistogram(DTOProperty dtoProperty, DTOEntity entity) {
        Map<Object, Integer> unsortedMap = new HashMap<>();
        String propertyOfHistogram = dtoProperty.getName();

        for (DTOEntityInstance e : entity.getInstances()
        ) {
            Object value = e.getDTOPropertyByName(propertyOfHistogram).getValue();
            if (!unsortedMap.containsKey(value)) {
                unsortedMap.put(value, 1);
            } else {
                unsortedMap.put(value, unsortedMap.get(value) + 1);
            }
        }

        // This makes a new map with its keys sorted.
        return new TreeMap<>(unsortedMap);
    }

    /**
     * Calculate and set in 'consistencyLabel' the property's consistency.
     */
//    private void updateConsistencyLbl(String propertyName, String entityName, boolean hasPopulation) {
//        if(hasPopulation){
//            DTOEntityInstance[] entityInstances = getDTOEntityByName(entityName).getInstances();
//            int simulationTicks = mainController.getSimulationCurrentTicks();
//            float subAvg = 0, mainAvg;
//            for (DTOEntityInstance entityInstance : entityInstances) {
//                subAvg += (float)simulationTicks / entityInstance.getDTOPropertyByName(propertyName).getChangeTickAmount();
//            }
//
//            mainAvg = subAvg / entityInstances.length;
//            consistencyLabel.setText(String.format("%.2f", mainAvg));
//        }else {
//            consistencyLabel.setText("-");
//        }
//
//    }

    /**
     * Calculate and set in 'AverageLabel' the property's average among the entity's population.
     */
    private void updateAverageLbl(String entityName, String propertyName, String propertyType) {
        float sum = 0, avg;
        int count = 0;
        // This valid only for numeric property.
        if (propertyType.equals("DECIMAL") || propertyType.equals("FLOAT")) {
            DTOEntityInstance[] entityInstances = getDTOEntityByName(entityName).getInstances();
            if (entityInstances.length != 0) {
                for (DTOEntityInstance entityInstance : entityInstances) {
                    sum += Float.parseFloat(entityInstance.getDTOPropertyByName(propertyName).getValue().toString());
                    count++;
                }

                avg = sum / count;
                AverageLabel.setText(String.format("%.2f", avg));
            } else {
                AverageLabel.setText("-");
            }

        } else {
            AverageLabel.setText("-");
        }

    }


}
