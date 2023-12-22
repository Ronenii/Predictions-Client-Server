package gui.app.menu.execution.queue.refresher;

import gui.app.menu.execution.queue.ExecutionQueueComponentController;
import manager.AdminServerAgent;

import java.util.TimerTask;

public class ExecutionQueueRefresher extends TimerTask {
    private final ExecutionQueueComponentController executionQueueComponentController;

    public ExecutionQueueRefresher(ExecutionQueueComponentController executionQueueComponentController) {
        this.executionQueueComponentController = executionQueueComponentController;
    }

    @Override
    public void run() {
        AdminServerAgent.getExecutionQueueAddedSimulations(executionQueueComponentController);
    }
}
