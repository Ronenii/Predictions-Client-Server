package simulation.objects.world.ticks.counter;

/**
 * This class holds ticks for a simulation.
 */
public class TicksCounter {
    private int ticks = 0;

    public int getTicks() {
        return ticks;
    }

    public void incrementTick(){
        ticks++;
    }

    public void resetTicks(){
        ticks = 0;
    }
}
