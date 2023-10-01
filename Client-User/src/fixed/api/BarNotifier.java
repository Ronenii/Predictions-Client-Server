package gui.api;

/**
 * We add this to components that we want to notify the notification bar.
 */
public interface BarNotifier {
    /**
     * Shows the given notification in the bar and in the window if needed.
     */
    default void showNotification(String notification){
        BarNotifier parent = getNotificationBar();
        parent.showNotification(notification);
    }

    /**
     * Used mostly to get the notification bar component from the parent component.
     */
    BarNotifier getNotificationBar();
}
