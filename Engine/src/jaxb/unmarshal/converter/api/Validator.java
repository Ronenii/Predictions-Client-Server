package jaxb.unmarshal.converter.api;

abstract public class Validator {
    private final StringBuilder errorsList;

    public Validator() {
        this.errorsList = new StringBuilder();
    }


    /**
     * Receives the given parameters, constructs and add the error message to the errorList string.
     *
     * @param operatingClass the class the error occurred in.
     * @param objectName  the name of the object the error occurred in
     * @param error       the error that occurred
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
}
