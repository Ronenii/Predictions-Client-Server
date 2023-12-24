package gui.app.menu.management.thread.refresher;

import gui.app.menu.management.thread.ThreadManagerComponentController;
import manager.AdminServerAgent;

import java.util.TimerTask;

public class ThreadDataRefresher extends TimerTask {

    private final ThreadManagerComponentController threadManagerComponentController;

    public ThreadDataRefresher(ThreadManagerComponentController threadManagerComponentController) {
        this.threadManagerComponentController = threadManagerComponentController;
    }

    @Override
    public void run() {
        AdminServerAgent.getThreadData(threadManagerComponentController);
    }
}
