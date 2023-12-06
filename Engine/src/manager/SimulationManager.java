package manager;

import client2server.simulation.request.DTORequest;
import manager.requests.RequestsManager;
import manager.requests.data.RequestData;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.result.DTOLoadResult;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import server2client.simulation.runtime.ResultData;
import server2client.simulation.runtime.SimulationRunData;
import server2client.simulation.runtime.generator.IdGenerator;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import jaxb.unmarshal.Reader;
import manager.DTO.creator.DTOCreator;
import manager.execution.ExecutionManager;
import simulation.objects.world.SimulationInstance;
import simulation.objects.world.definition.SimulationDefinition;
import simulation.objects.world.status.SimulationStatus;
import client2server.simulation.control.bar.DTOSimulationControlBar;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class SimulationManager {
    // Holds the loaded simulations definitions.
    private Map<String, SimulationDefinition> simulationDefinitions;
    private Map<String, ResultData> pastSimulations;
    private Set<String> keysToSerialize;
    private boolean isSimulationLoaded;
    private boolean isFirstSimulationLoaded;
    private ExecutionManager executionManager = null;
    private final RequestsManager requestsManager;
    private int simulationBreakdownVersion;

    public SimulationManager() {
        simulationDefinitions = new HashMap<>();
        pastSimulations = new HashMap<>();
        isSimulationLoaded = false;
        keysToSerialize = new HashSet<>();
        isFirstSimulationLoaded = true;
        requestsManager = new RequestsManager(simulationDefinitions);
        simulationBreakdownVersion = 0;
    }

    public void loadValuesFromDeserialization(SimulationManager instance) {
        pastSimulations = instance.pastSimulations;
        keysToSerialize = instance.keysToSerialize;
        isSimulationLoaded = instance.isSimulationLoaded;
    }


    public boolean getIsSimulationLoaded() {
        return isSimulationLoaded;
    }

    public SimulationsPreviewData getCurrentSimulationsDetails() {
        PreviewData[] previewDataArray = new PreviewData[simulationDefinitions.size()];
        int index = 0;
        for(SimulationDefinition simulationDefinition : simulationDefinitions.values()) {
            previewDataArray[index] = getDefinitionPreviewData(simulationDefinition.getSimulationAbstractInstance());
            index++;
        }

        return new SimulationsPreviewData(previewDataArray);
    }

    public void updateThreadCount(int threadCount) {
        if(executionManager == null) {
            executionManager = new ExecutionManager(threadCount);
        } else {
            executionManager.shutdownPreviousThreadPoolAndSetNewThreadPool(threadCount);
        }
    }

    public PreviewData getDefinitionPreviewDataByName(String defName) {
        return getDefinitionPreviewData(simulationDefinitions.get(defName).getSimulationAbstractInstance());
    }

    private PreviewData getDefinitionPreviewData(SimulationInstance simulationDefinition) {
        DTOCreator dtoCreator = new DTOCreator();
        return dtoCreator.createSimulationPreviewDataObject(simulationDefinition.getSimulationName(), simulationDefinition.getEnvironmentProperties(), simulationDefinition.getEntities(), simulationDefinition.getRules(), simulationDefinition.getEndingConditions(), simulationDefinition.getGrid());
    }

    public String getSimulationDetailsById(int simId) {
        return null;
    }


    public ResultData[] getPastSimulationResultData() {
        return pastSimulations.values().toArray(new ResultData[0]);
    }

//    private void addResultData(ResultData resultData) {
//        pastSimulations.put(resultData.getId(), resultData);
//    }

    private ResultData getResultDataById(String id) {
        return pastSimulations.get(id);
    }


    public DTOLoadResult loadSimulationFromFile(File file) {
        SimulationDefinition newDefinition;
        DTOLoadResult dtoLoadResult = null;
        try {
            newDefinition = Reader.readWorldFromXML(file, simulationDefinitions);
            if (newDefinition.getSimulationAbstractInstance() != null) {
                simulationDefinitions.put(newDefinition.getSimulationName(), newDefinition);
                dtoLoadResult = new DTOLoadResult(true, "");

                isFirstSimulationLoaded = false;
                if (executionManager != null){
                    executionManager.shutdownThreadPool();
                }

                //executionManager = new ExecutionManager(simulationDefinition.getThreadCount());

                simulationBreakdownVersion++;
                isSimulationLoaded = true;
            }
        }catch (IllegalArgumentException e){
            dtoLoadResult = new DTOLoadResult(false, e.getMessage());
        }


        return dtoLoadResult;
    }


    public StartResponse startSimulation(int reqId) {
        SimulationInstance reqSimulationDefinition = requestsManager.getApprovedRequest(reqId).getDefinitionInstance();

        if(reqSimulationDefinition.isStartable()) {
            DTOCreator dtoCreator = new DTOCreator();
            String id = IdGenerator.generateID();
            SimulationRunData simulationRunData = new SimulationRunData(IdGenerator.generateID(),0, 0, dtoCreator.getDTOEntityPopulationArray(reqSimulationDefinition.getEntities()), SimulationStatus.WAITING.name(), false, getEnvVarsValuesMap(reqSimulationDefinition), false);

            addSimulationToQueue(simulationRunData, reqSimulationDefinition);
            return new StartResponse(true, String.format("Simulation %s was added to the queue successfully.", id), simulationRunData);
        } else {
            return new StartResponse(false, "ERROR: Could not start simulation. You need to have at least one entity with a population larger than 0.");
        }

    }

    private Map<String, Object> getEnvVarsValuesMap(SimulationInstance reqSimulationDefinition) {
        Map<String, Object> envVarsValuesMap = new HashMap<>();
        Map<String, Property> environmentProperties = reqSimulationDefinition.getEnvironmentProperties();

        for (Property property : environmentProperties.values()) {
            envVarsValuesMap.put(property.getName(), property.getValue());
        }

        return envVarsValuesMap;
    }


    /**
     * Clears all data of past simulations and all generated IDs.
     */
    public void resetEngine() {
        pastSimulations.clear();
        IdGenerator.clearIds();
    }


    /**
     * Create and return the DTO start data which contains information about the simulation's environment variables.
     *
     * @return a StartData DTO
     */

//    public StartData getSimulationStartData() {
//        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
//        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
//        Map<String, Property> environmentProperties = this.simulationDefinition.getEnvironmentProperties();
//        Property valueFromTheMap;
//
//        for (Map.Entry<String, Property> entry : environmentProperties.entrySet()) {
//            valueFromTheMap = entry.getValue();
//            environmentVariables.add(getDTOEnvironmentVariable(valueFromTheMap));
//        }
//
//        return new StartData(environmentVariables);
//    }

    /**
     * Create a 'DTOEnvironmentVariable' which contain the given environment variable's data and return it.
     */
    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap) {
        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
        Double from = null, to = null;

        if (valueFromTheMap.getType() == PropertyType.FLOAT) {
            DoubleProperty doubleProperty = (DoubleProperty) valueFromTheMap;
            from = doubleProperty.getFrom();
            to = doubleProperty.getTo();
        } else if (valueFromTheMap.getType() == PropertyType.DECIMAL) {
            IntProperty intProperty = (IntProperty) valueFromTheMap;
            from = (double) intProperty.getFrom();
            to = (double) intProperty.getTo();
        }

        return new DTOEnvironmentVariable(name, type, from, to);
    }

    public void saveState(String path) {
        File toSerialize = new File(path);
        keysToSerialize.addAll(IdGenerator.getGeneratedIds());

        try {
            toSerialize.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(this);
            out.close();
            fileOut.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadState(String path) {
        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            loadValuesFromDeserialization((SimulationManager) in.readObject());
            IdGenerator.setGeneratedIds(keysToSerialize);
            in.close();
            file.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public SetResponse setEntityPopulation(EntityPopulationUserInput input) {
        return requestsManager.setEntityPopulation(input);
    }


    public SetResponse setEnvironmentVariable(EnvPropertyUserInput input) {
        return requestsManager.setEnvironmentVariable(input);
    }



    public SimulationRunData getRunDataById(String simId) {
        return executionManager.getRunDataById(simId);
    }


    private void addSimulationToQueue(SimulationRunData simulationRunData, SimulationInstance reqSimulationDefinition) {
        SimulationInstance simulationInstance = new SimulationInstance(reqSimulationDefinition);
        simulationInstance.setSimulationId(simulationRunData.getSimId());
        executionManager.addSimulationToQueue(simulationInstance, simulationRunData);
    }


    public void shutdownThreadPool() {
        if(executionManager != null){
            executionManager.shutdownThreadPool();
        }
    }


    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        executionManager.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
    }

    public int getSimulationBreakdownVersion() {
        return simulationBreakdownVersion;
    }

    public int addNewRequest(DTORequest dtoRequest, String username) {
        return requestsManager.addNewRequest(new RequestData(username, dtoRequest.getSimulationName(), dtoRequest.getSimulationTokens(), requestsManager.convertDTOEndingConditions(dtoRequest.getEndingConditions())));

    }

    public DTORequests getDTORequests() {
        return requestsManager.getDTORequests();
    }

    public void changeRequestStatus(int reqId, String reqStatus) {
        requestsManager.changeRequestStatus(reqId, reqStatus);
    }

    public DTORequestStatusUpdate getDtoRequestStatusUpdate(String username) {
        return requestsManager.getDtoRequestStatusUpdate(username);
    }
}
