package gui.app.menu.simulation.breakdown.refresher;

import gui.app.menu.simulation.breakdown.SimBreakdownMenuController;
import manager.UserServerAgent;

import java.util.TimerTask;

public class SimulationBreakdownRefresher extends TimerTask {

    private final SimBreakdownMenuController simBreakdownMenuController;

    public SimulationBreakdownRefresher(SimBreakdownMenuController simBreakdownMenuController) {
        this.simBreakdownMenuController = simBreakdownMenuController;
    }

    @Override
    public void run() {

        UserServerAgent.updateSimBreakdown(simBreakdownMenuController);
    }
}
