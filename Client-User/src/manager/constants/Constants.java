package manager.constants;

public class Constants {
    // global constants

    public final static int REFRESH_RATE = 2000;
    public final static int REQUEST_TABLE_REFRESH_RATE = 2000;

    // Server resource locations
    private final static String BASE_DOMAIN = "localhost";
    private final static String PORT = ":8080";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + PORT;

    private final static String CONTEXT_PATH = "/Predictions_Web";

    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    private final static String CLIENT_CONTEXT = "/client";

    private final static String CLIENT_CONTEXT_PATH = FULL_SERVER_PATH + CLIENT_CONTEXT;

    public final static String CLIENT_CONNECT_PATH = CLIENT_CONTEXT_PATH + "/connect";

    public final static String SIMULATIONS_DETAILS_PATH = FULL_SERVER_PATH + "/get_simulation_definitions";

    public final static String SIMULATION_REQUEST_PATH = CLIENT_CONTEXT_PATH + "/request";

    public final static String REQUEST_STATUS_UPDATE_PATH = SIMULATION_REQUEST_PATH + "/status";

    public final static String SINGLE_SIMULATION_PREVIEW_DATA_PATH = CLIENT_CONTEXT_PATH + "/execution/details";

    public final static String SEND_POPULATION_DATA_PATH = CLIENT_CONTEXT_PATH + "/execution/population";

    public final static String SEND_ENV_VARS_DATA_PATH = CLIENT_CONTEXT_PATH + "/execution/env/vars";

    public final static String START_SIMULATION_PATH = CLIENT_CONTEXT_PATH + "/simulation/start";

    public final static String GET_SIMULATION_RUN_DATA_PATH = CLIENT_CONTEXT_PATH + "/simulation/run/data";

    public final static String SET_DTO_CONTROL_BAR_PATH = CLIENT_CONTEXT_PATH + "/simulation/control/bar";


}
