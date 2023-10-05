package utils;

import manager.SimulationManager;
import user.UserManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String SIMULATION_MANAGER_ATTRIBUTE_NAME = "simulationManager";
    private static final Object userManagerLock = new Object();
    private static final Object simulationManagerLock = new Object();
    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static SimulationManager getSimulationManager(ServletContext servletContext) {
        synchronized (simulationManagerLock) {
            if (servletContext.getAttribute(SIMULATION_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SIMULATION_MANAGER_ATTRIBUTE_NAME, new SimulationManager());
            }
        }
        return (SimulationManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }}
