package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.prview.SimulationsPreviewData;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

/**
 * Responsible for handling whenever a client tries to get all possible simulations.
 */
@WebServlet (name = "SendSimulationDefinitionsServlet", urlPatterns = "/get_simulation_definitions")
public class SendSimulationDefinitionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String sessionSimulationBreakdownVersionStr = SessionUtils.getSimulationBreakdownVersion(req);
        int sessionSimulationBreakdownVersionInt = Integer.parseInt(sessionSimulationBreakdownVersionStr);
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());

        resp.setContentType("application/json");
        if(sessionSimulationBreakdownVersionInt < simulationManager.getSimulationBreakdownVersion()) {
            // Convert the preview data to a json and write it to the response.
            SimulationsPreviewData simulationsPreviewData = simulationManager.getCurrentSimulationsDetails();
            String responseJsonContent = gson.toJson(simulationsPreviewData);

            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
