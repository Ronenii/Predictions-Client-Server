package engine2ui.simulation.properties.property.api;

abstract public class DTOProperty {
    private String name;
    private String type;
    private boolean isRandomInit;

    public DTOProperty(String name, String type, boolean isRandomInit) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
    }
}
