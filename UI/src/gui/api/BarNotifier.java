package gui.api;

public interface BarNotifier {
    default void showNotification(String notification){
        BarNotifier parent = getNotificationBar();
        parent.showNotification(notification);
    }

    BarNotifier getNotificationBar();
}
