package servlets.admin;

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

@WebServlet(name = "SendPreviewDataServlet", urlPatterns = {"/admin/preview/data"})
public class SendPreviewDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String simName = req.getParameter(Constants.SIMULATION_NAME);

        resp.setContentType("application/json");
        PreviewData previewData = simulationManager.getPreviewDataByName(simName);
        String responseJsonContent = gson.toJson(previewData);
        resp.getOutputStream().println(responseJsonContent);
    }
}
