package manager.constant;

public class Constants {

    // global constants

    public final static int REFRESH_RATE = 2000;

    // Server resource locations
    private final static String BASE_DOMAIN = "localhost";

    private final static String BASE_URL = "http://" + BASE_DOMAIN;

    private final static String CONTEXT_PATH = "/Predictions_Web";

    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String ADMIN_CONNECT_PATH = FULL_SERVER_PATH + "/admin";
}
