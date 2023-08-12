package engine2ui.simulation.prview.properties.property.api;

abstract public class DTOProperty {
    private final String name;
    private final String type;
    private final boolean isRandomInit;

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
