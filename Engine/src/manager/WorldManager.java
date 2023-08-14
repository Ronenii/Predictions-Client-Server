package manager;

import jaxb.unmarshal.Reader;
import simulation.objects.world.World;

public class WorldManager implements EngineInterface{
   private World world;

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
