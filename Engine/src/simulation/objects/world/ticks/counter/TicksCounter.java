package simulation.objects.world.ticks.counter;

public class TicksCounter {
    private int ticks = 0;

    public int getTicks() {
        return ticks;
    }

    public void moveTicksByOne(){
        ticks++;
    }

    public void resetTicks(){
        ticks = 0;
    }
}
