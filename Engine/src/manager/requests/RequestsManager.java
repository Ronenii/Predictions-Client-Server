package manager.requests;

import manager.DTO.creator.DTOCreator;
import manager.requests.data.ApprovedRequest;
import manager.requests.data.RequestData;
import manager.requests.data.RequestStatus;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import simulation.objects.world.SimulationInstance;
import simulation.objects.world.definition.SimulationDefinition;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.ending.conditions.EndingConditionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsManager {
    private int idCounter;
    // requestDataMap holds the 'Pending' requests. Accepted or denied request will be removed from this map.
    private final Map<Integer, RequestData> requestDataMap;
    // approvedRequestMap holds only the approved requests, in order to receive the user last initialize for entity count etc.
    private final Map<Integer, ApprovedRequest> approvedRequestMap;

    private Map<String, SimulationDefinition> simulationDefinitions;

    public RequestsManager(Map<String, SimulationDefinition> simulationDefinitionMap) {
        this.idCounter = 0;
        this.requestDataMap = new HashMap<>();
        this.approvedRequestMap = new HashMap<>();
        this.simulationDefinitions = simulationDefinitionMap;
    }

    /**
     * The method return the new request's id
     */
    public int addNewRequest(RequestData requestData) {
        int ret = idCounter;

        requestData.requestId = idCounter;
        requestDataMap.put(idCounter,requestData);
        idCounter++;
        return ret;
    }

    public RequestData getNonApprovedRequest(int id) {
        return requestDataMap.get(id);
    }

    public ApprovedRequest getApprovedRequest(int id) {return approvedRequestMap.get(id);}

    public List<EndingCondition> convertDTOEndingConditions(DTOEndingCondition[] endingConditions) {
        List<EndingCondition> ret = new ArrayList<>();

        for (DTOEndingCondition endingCondition : endingConditions) {
            ret.add(new EndingCondition(EndingConditionType.valueOf(endingCondition.getType().toUpperCase()), endingCondition.getCount()));
        }

        return ret;
    }

    public DTORequests getDTORequests() {
        DTOCreator dtoCreator = new DTOCreator();

       if (requestDataMap.isEmpty()) {
           return null;
       } else {
           return dtoCreator.createDTORequests(requestDataMap);
       }
    }

    public void changeRequestStatus(int reqId, String reqStatus) {
        requestDataMap.get(reqId).setStatus(RequestStatus.valueOf(reqStatus));
    }

    public DTORequestStatusUpdate getDtoRequestStatusUpdate(String username) {
        DTOCreator dtoCreator = new DTOCreator();
        List<RequestData> updatedRequests = new ArrayList<>();

        for(RequestData requestData : requestDataMap.values()) {
            if(requestData.getUsername().equals(username)) {
                if(requestData.getStatus() == RequestStatus.APPROVED || requestData.getStatus() == RequestStatus.DENIED) {
                    updatedRequests.add(requestData);
                }
            }
        }

        // Another loop to remove the approved/denied requests from requestDataMap
        for(RequestData requestData : updatedRequests) {
            addNewApprovedRequest(requestData);
            requestDataMap.remove(requestData.requestId);
        }

        return dtoCreator.createDtoRequestStatusUpdate(updatedRequests);
    }

    private void addNewApprovedRequest(RequestData requestData){
        approvedRequestMap.put(requestData.requestId, new ApprovedRequest(requestData.requestId,
                requestData.getUsername(),
                requestData.getTokens(),
                new SimulationInstance(simulationDefinitions.get(requestData.getSimulationName()).getSimulationAbstractInstance())));

    }
}
