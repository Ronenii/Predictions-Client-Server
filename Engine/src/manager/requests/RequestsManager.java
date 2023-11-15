package manager.requests;

import manager.DTO.creator.DTOCreator;
import manager.requests.data.RequestData;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.request.DTORequests;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.ending.conditions.EndingConditionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsManager {
    private int idCounter;
    private Map<Integer, RequestData> requestDataMap;

    public RequestsManager() {
        this.idCounter = 0;
        this.requestDataMap = new HashMap<>();
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

    public RequestData getRequest(int id) {
        return requestDataMap.get(id);
    }

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
}