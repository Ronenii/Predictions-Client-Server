package manager;

import client2server.simulation.request.DTORequest;
import manager.requests.RequestsManager;
import manager.requests.data.RequestData;
import server2client.simulation.admin.load.details.AdminLoadDetails;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.result.DTOLoadResult;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.queue.SimulationData;
import server2client.simulation.queue.NewSimulationsData;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import server2client.simulation.runtime.SimulationRunData;
import server2client.simulation.runtime.generator.IdGenerator;
import jaxb.unmarshal.Reader;
import manager.DTO.creator.DTOCreator;
import manager.execution.ExecutionManager;
import server2client.simulation.thread.data.ThreadData;
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
    private List<SimulationData> newSimulations;
    private ExecutionManager executionManager = null;
    private final RequestsManager requestsManager;
    private int simulationBreakdownVersion;
    private int addedSimulationsCount;

    public SimulationManager() {
        simulationDefinitions = new HashMap<>();
        newSimulations = new LinkedList<>();
        requestsManager = new RequestsManager(simulationDefinitions);
        simulationBreakdownVersion = 0;
        addedSimulationsCount = 0;
    }

    public PreviewData getPreviewDataByName(String simName){
        return getDefinitionPreviewData(simulationDefinitions.get(simName).getSimulationAbstractInstance());
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

    /**
     * @return A DTO containing an array of all newly added simulations.
     */
    public NewSimulationsData getNewSimulationsDTO(){
        return new NewSimulationsData(newSimulations);
    }


    /**
     * Clears the List of newly added simulations so that the server won't send to the client twice the same simulation.
     */
    public void clearNewSimulations() {newSimulations.clear();}

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

    /**
     *
     * Runs a new instance of the simulation definition if possible.
     * @return The ID of the simulation that was started.
     */
    public StartResponse startSimulation(int reqId, String user) {
        SimulationInstance reqSimulationDefinition = requestsManager.getApprovedRequest(reqId).getDefinitionInstance();

        if(reqSimulationDefinition.isStartable()) {
            addedSimulationsCount++; // Update the number of simulations that were started.
            DTOCreator dtoCreator = new DTOCreator();
            String id = IdGenerator.generateID();
            SimulationRunData simulationRunData = new SimulationRunData(reqSimulationDefinition.getSimulationName(),id,0, 0, dtoCreator.getDTOEntityPopulationArray(reqSimulationDefinition.getEntities()), SimulationStatus.WAITING.name(), false, getEnvVarsValuesMap(reqSimulationDefinition), false, reqSimulationDefinition.getThreadSleepDuration());
            reqSimulationDefinition.setRequestedBy(user);
            addSimulationToQueue(simulationRunData,reqSimulationDefinition);
            newSimulations.add(new SimulationData(user,id, SimulationStatus.WAITING.name()));
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
     * Adds a new simulation instance to the simulations queue. Returns the ID iof the added simulation.
     */
    private void addSimulationToQueue(SimulationRunData simulationRunData, SimulationInstance reqSimulationDefinition) {
        SimulationInstance simulationInstance = new SimulationInstance(reqSimulationDefinition);
        simulationInstance.setSimulationId(simulationRunData.getSimId());
        executionManager.addSimulationToQueue(simulationInstance, simulationRunData);
    }

    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        executionManager.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
    }

    public int getSimulationBreakdownVersion() {
        return simulationBreakdownVersion;
    }

    public int getAddedSimulationsCount(){
        return addedSimulationsCount;
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

    public String getSimulationStatusById(String simulationId){
        return executionManager.getRunDataById(simulationId).getStatus();
    }

    public void shutdownThreadPool() {
        executionManager.shutdownThreadPool();
    }

    public int getThreadDataVersion() {
        return executionManager.getThreadDataVersion();
    }

    public ThreadData getThreadData() {
        return executionManager.getThreadData();
    }

    public AdminLoadDetails getAdminLoadDetails() {
        DTOCreator dtoCreator = new DTOCreator();
        NewSimulationsData newSimulationsData;
        boolean isThreadExecutorSet;

        if(executionManager != null) {
            newSimulationsData = executionManager.getNewSimulationData();
            isThreadExecutorSet = executionManager.isThreadExecutorSet();
        } else {
            newSimulationsData = null;
            isThreadExecutorSet = false;
        }

        return dtoCreator.createAdminLoadDetails(simulationDefinitions.keySet(), requestsManager.getDTORequests(), newSimulationsData, isThreadExecutorSet);
    }
}
