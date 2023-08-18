package jaxb.unmarshal.converter.api;

/**
 * A parent class for all validators. Ensures that each validator has an error list and the functions to
 * handle it.
 */
abstract public class Validator {
    private final StringBuilder errorsList;

    public Validator() {
        this.errorsList = new StringBuilder();
    }


    /**
     * Receives the given parameters, constructs and add the error message to the errorList string.
     *
     * @param operatingClass the class the error occurred in.
     * @param objectName     the name of the object the error occurred in
     * @param error          the error that occurred
     */
    public void addErrorToList(Object operatingClass, String objectName, String error) {
        errorsList.append(String.format("In %s: %s, ", operatingClass.getClass().getSimpleName(), objectName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    public void addActionErrorToList(String ruleName, String actionName, int actionNumber, String error) {
        errorsList.append(String.format("In rule: %s, action number %d, type: %s, ", ruleName, actionNumber, actionName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    public void addEntitiesAndEnvPropCreationErrorMessage() {
        errorsList.append("Due to the errors provided, the simulation's environment variables and/or entities were not created and the conversion process stopped.\n")
                .append("Please fix the provided XML file where the errors occurred and reload the file to ensure proper creation of the simulation.\n");
    }

    public void addRulesAndEndingConditionsCreationErrorMessage() {
        errorsList.append("Due to the errors provided, the simulation's rules and/or ending conditions were not created and the conversion process stopped.\n")
                .append("Please fix the provided XML file where the errors occurred and reload the file to ensure proper creation of the simulation.\n");
    }

    /**
     * @return returns true if the validator found any errors.
     */
    public boolean containsErrors() {
        return errorsList.length() > 0;
    }

    public String getErrorList() {
        return errorsList.toString();
    }
}
