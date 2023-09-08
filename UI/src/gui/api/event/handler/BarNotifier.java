package gui.api.event.handler;

import gui.api.event.ErrorEvent;
import gui.notification.NotificationBarComponentController;

public interface BarNotifier {
    default void showNotification(String notification){
        BarNotifier parent = getNotificationBarParent();
        parent.showNotification(notification);
    }

    BarNotifier getNotificationBarParent();
}
