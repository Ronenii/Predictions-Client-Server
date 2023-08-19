package engine2ui.simulation.result.generator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A class responsible for generating unique IDs of length 8.
 */
public class IdGenerator {

    private static final Set<String> generatedIds = new HashSet<>();

    /**
     * Generates a UUID, removes all '-' from it, and takes out the first 8 chars of the UUID.
     * Cross-references it with previously generated unique IDs to see if the generated ID is unique.
     * @return A newly generated unique ID.
     */
    public static String generateID(){
        String id;
        do{
            UUID uuid = UUID.randomUUID();
            id = uuid.toString().replace("-", "").substring(0, 8);
        }while(!isUniqueId(id));
        generatedIds.add(id);

        return id;
    }

    /**
     * @param id the generated id
     * @return true if the given id hasn't been generated before, false otherwise.
     */
    private static boolean isUniqueId(String id){
        return !generatedIds.contains(id);
    }

    public static void clearIds(){
        generatedIds.clear();
    }
}
