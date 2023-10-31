package utils;

import constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static String getUsername (HttpServletRequest request) {
        // false as a parameter for the "getSession" method - if there is not any session exists for this client, don't creat a new one.
        HttpSession session = request.getSession(false);
        // If there is a session the return value will be the username string, otherwise, return null.
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getSimulationBreakdownVersion(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(session.getAttribute(Constants.SIMULATION_BREAKDOWN_VERSION) == null) {
            session.setAttribute(Constants.SIMULATION_BREAKDOWN_VERSION, 0);
        }

        return session.getAttribute(Constants.SIMULATION_BREAKDOWN_VERSION).toString();
    }

    public static void setSimulationBreakdownVersion(HttpServletRequest request, int newVersion) {
        HttpSession session = request.getSession(false);

        session.setAttribute(Constants.SIMULATION_BREAKDOWN_VERSION, newVersion);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
