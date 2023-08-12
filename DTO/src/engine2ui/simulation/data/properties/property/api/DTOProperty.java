package engine2ui.simulation.data.properties.property.api;

abstract public class DTOProperty {
    private String name;
    private String type;
    private boolean isRandomInit;

    public DTOProperty(String name, String type, boolean isRandomInit) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("\tName: %s\n", name));
        ret.append(String.format("\tType: %s\n", type));
        ret.append(String.format("\tRandomly initialized: %s\n", isRandomInit));

        return ret.toString();
    }
}
