package manager.constant;

public class Constants {

    // global constants

    public final static int REFRESH_RATE = 2000;

    // Server resource locations
    private final static String BASE_DOMAIN = "localhost";
    private final static String PORT = ":8080";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + PORT;

    private final static String CONTEXT_PATH = "/Predictions_Web";

    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    private final static String ADMIN_CONTEXT = "/admin";

    private final static String ADMIN_CONTEXT_PATH = FULL_SERVER_PATH + ADMIN_CONTEXT;

    public final static String ADMIN_CONNECT_PATH = ADMIN_CONTEXT_PATH + "/connect";
    public final static String ADMIN_DISCONNECT_PATH = ADMIN_CONTEXT_PATH + "/disconnect";
    public final static String ADMIN_LOAD_DETAILS_PATH = ADMIN_CONTEXT_PATH + "/load/details";
    public final static String FILE_UPLOAD_PATH = ADMIN_CONTEXT_PATH + "/file_upload";
    public final static String SIMULATIONS_DETAILS_CONTEXT_PATH = FULL_SERVER_PATH + "/get_simulation_definitions";
    public final static String SIMULATIONS_PREVIEW_DATA_PATH = ADMIN_CONTEXT_PATH + "/preview/data";
    public final static String ADMIN_THREAD_COUNT = ADMIN_CONTEXT_PATH + "/thread/count";
    public final static String ADMIN_REQUEST_REFRESHER = ADMIN_CONTEXT_PATH +"/requests";
    public final static String ADMIN_CHANGE_REQUEST_STATUS = ADMIN_REQUEST_REFRESHER + "/status";
    public final static String ADMIN_THREAD_DATA_PATH = ADMIN_CONTEXT_PATH + "/thread/data";

    private final static String CLIENT_CONTEXT = "/client";

    private final static String CLIENT_CONTEXT_PATH = FULL_SERVER_PATH + CLIENT_CONTEXT;

    public final static String GET_SIMULATION_RUN_DATA_PATH = CLIENT_CONTEXT_PATH + "/simulation/run/data";

    public final static String GET_SIMULATIONS_ADDED_PATH = ADMIN_CONTEXT_PATH + "/simulation/added";
}
