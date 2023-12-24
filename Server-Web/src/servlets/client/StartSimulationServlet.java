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
import server2client.simulation.execution.StartResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "StartSimulationServlet", urlPatterns = "/client/simulation/start")
public class StartSimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        int reqId = Integer.parseInt(req.getParameter(Constants.REQUEST_ID));

        synchronized (this) {
            StartResponse response = simulationManager.startSimulation(reqId, SessionUtils.getUsername(req));

            resp.setContentType("application/json");
            String responseJsonContent = gson.toJson(response);
            resp.getOutputStream().println(responseJsonContent);
        }


    }
}
