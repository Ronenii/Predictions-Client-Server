package jaxb.unmarshal.converter.api;

/**
 * A parent class for all validators. Ensures that each validator has an error list and the functions to
 * handle it.
 */
abstract public class Validator {
    private final StringBuilder errorsList;
    private int errorCount;

    public Validator() {
        errorCount = 0;
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
        errorCount++;
        errorsList.append(String.format("%s) In %s: %s, ",errorCount, operatingClass.getClass().getSimpleName(), objectName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    /**
     * For actions only. receives the given parameters, constructs and add the error message to the errorList string.
     *
     * @param ruleName the rule the error occurred in.
     * @param actionName the type of the action the error occurred in
     * @param actionNumber the action position in the rule's actions in the XML file.
     * @param error          the error that occurred
     */
    public void addActionErrorToList(String ruleName, String actionName, int actionNumber, String error) {
        errorCount++;
        errorsList.append(String.format("%s) In rule: %s, action number %d, type: %s, ",errorCount, ruleName, actionNumber, actionName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    /**
     * If errors occurred, this method add to the error list a message to inform the simulation creation due to errors of
     * the environment variables and/or entities values in the XML file stopped and the XML need to be fixed.
     */
    public void addEntitiesAndEnvPropCreationErrorMessage() {
        errorsList.append("Due to the errors provided, the simulation's environment variables and/or entities were not created and the conversion process stopped.\n")
                .append("Please fix the provided XML file where the errors occurred and reload the file to ensure proper creation of the simulation.\n");
    }

    /**
     * If errors occurred, this method add to the error list a message to inform the simulation creation due to errors of
     * the rules and/or ending conditions values in the XML file stopped and the XML need to be fixed.
     */
    public void addRulesAndEndingConditionsCreationErrorMessage() {
        errorCount++;
        errorsList.append("Due to the errors provided, the simulation's rules and/or ending conditions were not created and the conversion process stopped.\n")
                .append("Please fix the provided XML file where the errors occurred and reload the file to ensure proper creation of the simulation.\n");
    }

    /**
     * @return returns true if the validator found any errors.
     */
    public boolean containsErrors() {
        return errorCount > 0;
    }

    public String getErrorList() {
        errorsList.insert(0, String.format("%s error\\s while loading the file.\n", errorCount));
        return errorsList.toString();
    }
}
