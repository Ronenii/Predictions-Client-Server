package servlets.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.runtime.SimulationRunData;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "SendSimRunDataServlet", urlPatterns = "/client/simulation/run/data")
public class SendSimRunDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String simId = req.getParameter(Constants.SIMULATION_ID);

        synchronized (this){
            SimulationRunData runData = simulationManager.getRunDataById(simId);

            resp.setContentType("application/json");
            String responseJsonContent = gson.toJson(runData);
            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
