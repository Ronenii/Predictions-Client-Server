package gui.app.menu.request.table.refresher;

import gui.app.menu.request.table.RequestTableComponentController;
import manager.UserServerAgent;

import java.util.TimerTask;

public class RequestsStatusRefresher extends TimerTask {

    private final RequestTableComponentController requestTableComponentController;

    public RequestsStatusRefresher(RequestTableComponentController requestTableComponentController) {
        this.requestTableComponentController = requestTableComponentController;
    }

    @Override
    public void run() {
        UserServerAgent.getRequestsStatusUpdates(requestTableComponentController);
    }
}
