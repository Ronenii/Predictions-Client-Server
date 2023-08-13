package engine2ui.simulation.genral;

import java.util.List;

public interface HasList {
    /**
     * @param list the list we wish to format
     * @param <T> the list's type.
     * @return the formatted list string as follows:
     * #1
     * list(0).toString()
     *
     * #2
     * list(1).toString()
     *
     * ...
     */
    default <T> String formatListToString(List<T> list){
        StringBuilder ret = new StringBuilder();

        for(int i =1; i < list.size(); i++){
            ret.append(String.format("\n#%s\n",i));
            ret.append(list.get(i-1));
        }

        return ret.toString();
    }
}
