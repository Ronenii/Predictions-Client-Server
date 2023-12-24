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
import server2client.simulation.prview.PreviewData;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SingleSimulationPreviewDataServlet", urlPatterns = "/client/execution/details")
public class SingleSimulationPreviewDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String simName = req.getParameter(Constants.SIMULATION_NAME);
        PreviewData previewData = simulationManager.getDefinitionPreviewDataByName(simName);

        resp.setContentType("application/json");
        String responseJsonContent = gson.toJson(previewData);
        resp.getOutputStream().println(responseJsonContent);
    }
}
