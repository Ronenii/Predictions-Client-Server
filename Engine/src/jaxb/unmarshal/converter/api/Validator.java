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
