package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.prview.SimulationsPreviewData;
import utils.CookiesUtils;
import utils.ServletUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Responsible for handling whenever a client tries to get all possible simulations.
 */
@WebServlet (name = "SendSimulationDefinitionsServlet", urlPatterns = "/get_simulation_definitions")
public class SendSimulationDefinitionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        int simulationBreakdownVersionInt = 0;
        String cookieSavedVersion = CookiesUtils.getSavedValueOnCookie(req, Constants.SIMULATION_BREAKDOWN_VERSION);
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        // check for the last version of the client's sim breakdown.
        if(cookieSavedVersion != null) {
            simulationBreakdownVersionInt = Integer.parseInt(cookieSavedVersion);
        }

        resp.setContentType("application/json");
        if(simulationBreakdownVersionInt < simulationManager.getSimulationBreakdownVersion()) {
            // Convert the preview data to a json and write it to the response.
            SimulationsPreviewData simulationsPreviewData = simulationManager.getCurrentSimulationsDetails();
            String responseJsonContent = gson.toJson(simulationsPreviewData);
            // if the sim breakdown version cookie does not exist, create a new one, otherwise, update the value of the exists cookie.
            if(cookieSavedVersion == null){
                CookiesUtils.createAndSaveNewCookie(resp, String.valueOf(simulationManager.getSimulationBreakdownVersion()), Constants.SIMULATION_BREAKDOWN_VERSION);

            } else {
                CookiesUtils.updateValueOnCookie(req, resp, String.valueOf(simulationManager.getSimulationBreakdownVersion()), Constants.SIMULATION_BREAKDOWN_VERSION);
            }

            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
