package manager.requests;

import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import manager.DTO.creator.DTOCreator;
import manager.requests.data.ApprovedRequest;
import manager.requests.data.RequestData;
import manager.requests.data.RequestStatus;
import manager.validator.exceptions.IllegalBooleanValueException;
import manager.validator.exceptions.IllegalStringValueException;
import manager.validator.exceptions.OutOfRangeException;
import manager.validator.validator.InputValidator;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import simulation.objects.world.SimulationInstance;
import simulation.objects.world.definition.SimulationDefinition;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.property.api.Property;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;

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

        approvedRequestMap.get(requestData.requestId).setDefinitionInstanceEndingConditions(requestData.getEndingConditions());
    }

    public SetResponse setEntityPopulation(EntityPopulationUserInput input) {
        return approvedRequestMap.get(input.getReqId()).getDefinitionInstance().setEntityPopulation(input);
    }

    public SetResponse setEnvironmentVariable(EnvPropertyUserInput input) {
        SetResponse response;
        Map<String, Property> environmentProperties = approvedRequestMap.get(input.getReqId()).getDefinitionInstance().getEnvironmentProperties();
        Property envProperty = environmentProperties.get(input.getName());

        if (input.isRandomInit()) {
            envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty), input.isRandomInit());
            response = new SetResponse(true, String.format("%s's value was set to %s", input.getName(), input.getValue()), null);
        } else {
            response = validateEnvVarInitValue(envProperty, input.getValue());
            if (response.isSuccess()) {
                envProperty.updateValueAndIsRandomInit(response.getParsedValue(), input.isRandomInit());
            }
        }

        return response;
    }

    /**
     * This method generate a random object match to the environment variable's type.
     *
     * @param envProperty the environment variable
     * @return a random object
     */
    private Object getRandomValueByType(Property envProperty) {
        Object ret = null;


        switch (envProperty.getType()) {
            case DECIMAL:
                IntProperty intProperty = (IntProperty) envProperty;
                RandomValueGenerator<Integer> randomIntValueGenerator = new IntRndValueGen(intProperty.getFrom(), intProperty.getTo());
                ret = randomIntValueGenerator.generateRandomValue();
                break;
            case FLOAT:
                DoubleProperty doubleProperty = (DoubleProperty) envProperty;
                RandomValueGenerator<Double> randomDoubleValueGenerator = new DoubleRndValueGen(doubleProperty.getFrom(), doubleProperty.getTo());
                ret = randomDoubleValueGenerator.generateRandomValue();
                break;
            case BOOLEAN:
                RandomValueGenerator<Boolean> randomBooleanValueGenerator = new BoolRndValueGen();
                ret = randomBooleanValueGenerator.generateRandomValue();
                break;
            case STRING:
                RandomValueGenerator<String> randomStringValueGenerator = new StringRndValueGen();
                ret = randomStringValueGenerator.generateRandomValue();
                break;
        }

        return ret;
    }

    /**
     * Validates the given user input for the environment variable and returns a set response accordingly.
     * Validates range\regex matching\value compatibility based on the type of the property.
     *
     * @param property   The property we are comparing the value to
     * @param inputValue The value we want to compare
     * @return A Set response indicating if the set action has succeeded or failed with a custom message
     */
    private SetResponse validateEnvVarInitValue(Property property, Object inputValue) {
        String successMessage = String.format("%s's value was successfully set to %s", property.getName(), inputValue.toString());
        InputValidator inputValidator = new InputValidator();
        try {
            switch (property.getType()) {
                case DECIMAL: {

                    int value = Integer.parseInt(inputValue.toString());
                    IntProperty intProperty = (IntProperty) property;
                    inputValidator.isIntegerInRange(value, intProperty.getFrom(), intProperty.getTo());
                    return new SetResponse(true, successMessage, value);
                }
                case FLOAT: {
                    double value = Double.parseDouble(inputValue.toString());
                    DoubleProperty doubleProperty = (DoubleProperty) property;
                    inputValidator.isDoubleInRange(value, doubleProperty.getFrom(), doubleProperty.getTo());
                    return new SetResponse(true, successMessage, value);
                }
                case BOOLEAN: {
                    inputValidator.validateBoolean(inputValue.toString());
                    if((inputValue.toString().equalsIgnoreCase("true"))) {
                        return new SetResponse(true, successMessage, true);
                    } else {
                        return new SetResponse(true, successMessage, false);
                    }
                }
                case STRING:
                    inputValidator.validateStringValue(inputValue.toString());
                    return new SetResponse(true, successMessage, inputValue);
            }
        } catch (NumberFormatException e) {
            return new SetResponse(false, String.format("ERROR: Value provided is does not match %s's type (%s)", property.getName(), property.getType().toString()), null);
        } catch (OutOfRangeException | IllegalBooleanValueException e) {
            return new SetResponse(false, String.format("ERROR: Value given is not in %s's range.", property.getName()), null);
        } catch (IllegalStringValueException e) {
            return new SetResponse(false, "ERROR: String given does not meet the string guidelines.", null);
        }
        return null;
    }
}
