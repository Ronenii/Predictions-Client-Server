package manager;

import engine2ui.simulation.result.ResultData;
import jaxb.unmarshal.Reader;
import simulation.objects.world.World;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldManager implements EngineInterface{
   private World world;
   private Map<UUID, ResultData> pastSimulations;

   public WorldManager() {
      world = null;
      pastSimulations = new HashMap<>();
   }

   @Override
   public String getCurrentSimulationDetails() {
      return null;
   }

   @Override
   public String getSimulationDetailsById(int simId) {
      return null;
   }

   @Override
   public String[] getAllSimulationDetailsInShortFormat() {
      return new String[0];
   }

   @Override
   public void loadSimulationFromFile(String path) {
      this.world = Reader.readWorldFromXML(path);
   }

   @Override
   public void runSimulation() {

   }
}
