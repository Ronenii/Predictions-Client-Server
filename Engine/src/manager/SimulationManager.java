package manager;

import client2server.simulation.request.DTORequest;
import manager.requests.RequestsManager;
import manager.requests.data.RequestData;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.result.DTOLoadResult;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.queue.AddedSimulationsData;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import server2client.simulation.runtime.SimulationRunData;
import server2client.simulation.runtime.generator.IdGenerator;
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
import java.io.*;
import java.util.*;


public class SimulationManager {
    // Holds the loaded simulations definitions.
    private Map<String, SimulationDefinition> simulationDefinitions;
    private List<String> addedSimulations;
    private ExecutionManager executionManager = null;
    private final RequestsManager requestsManager;
    private int simulationBreakdownVersion;
    private int simulationsAdded;

    public SimulationManager() {
        simulationDefinitions = new HashMap<>();
        addedSimulations = new LinkedList<>();
        requestsManager = new RequestsManager(simulationDefinitions);
        simulationBreakdownVersion = 0;
        simulationsAdded = 0;
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

    public AddedSimulationsData getAddedSimulationsDTO(){
        return new AddedSimulationsData(addedSimulations);
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

    public DTOLoadResult loadSimulationFromFile(File file) {
        SimulationDefinition newDefinition;
        DTOLoadResult dtoLoadResult = null;
        try {
            newDefinition = Reader.readWorldFromXML(file, simulationDefinitions);
            if (newDefinition.getSimulationAbstractInstance() != null) {
                simulationDefinitions.put(newDefinition.getSimulationName(), newDefinition);
                dtoLoadResult = new DTOLoadResult(true, "");

                simulationBreakdownVersion++;
            }
        }catch (IllegalArgumentException e){
            dtoLoadResult = new DTOLoadResult(false, e.getMessage());
        }

        return dtoLoadResult;
    }

    public StartResponse startSimulation(int reqId) {
        SimulationInstance reqSimulationDefinition = requestsManager.getApprovedRequest(reqId).getDefinitionInstance();

        if(reqSimulationDefinition.isStartable()) {
            simulationsAdded++;
            DTOCreator dtoCreator = new DTOCreator();
            String id = IdGenerator.generateID();
            SimulationRunData simulationRunData = new SimulationRunData(IdGenerator.generateID(),0, 0, dtoCreator.getDTOEntityPopulationArray(reqSimulationDefinition.getEntities()), SimulationStatus.WAITING.name(), false, getEnvVarsValuesMap(reqSimulationDefinition), false, reqSimulationDefinition.getThreadSleepDuration());

            addedSimulations.add(addSimulationToQueue(simulationRunData, reqSimulationDefinition));
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

    public SetResponse setEntityPopulation(EntityPopulationUserInput input) {
        return requestsManager.setEntityPopulation(input);
    }

    public SetResponse setEnvironmentVariable(EnvPropertyUserInput input) {
        return requestsManager.setEnvironmentVariable(input);
    }

    public SimulationRunData getRunDataById(String simId) {
        return executionManager.getRunDataById(simId);
    }

    /**
     *
     * Adds a new simulation instance to the simulations queue. Returns the ID iof the added simulation.
     */
    private String addSimulationToQueue(SimulationRunData simulationRunData, SimulationInstance reqSimulationDefinition) {
        SimulationInstance simulationInstance = new SimulationInstance(reqSimulationDefinition);
        simulationInstance.setSimulationId(simulationRunData.getSimId());
        executionManager.addSimulationToQueue(simulationInstance, simulationRunData);

        return simulationInstance.getSimulationId();
    }

    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        executionManager.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
    }

    public int getSimulationBreakdownVersion() {
        return simulationBreakdownVersion;
    }

    public int getSimulationsAdded(){
        return simulationsAdded;
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

    public void shutdownThreadPool() {
        executionManager.shutdownThreadPool();
    }
}
