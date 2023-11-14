package gui.app.menu.allocation.refresher;

import gui.app.menu.allocation.AllocationComponentController;
import manager.AdminServerAgent;

import java.util.TimerTask;

public class AllocationRefresher extends TimerTask {
    private AllocationComponentController allocationComponentController;

    public AllocationRefresher(AllocationComponentController allocationComponentController) {
        this.allocationComponentController = allocationComponentController;
    }

    @Override
    public void run() {
        AdminServerAgent.updateRequestsTable(allocationComponentController);
    }
}
