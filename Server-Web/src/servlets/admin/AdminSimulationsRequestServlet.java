package servlets.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.request.DTORequests;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "AdminSimulationsRequestServlet", urlPatterns = {"/admin/requests"})
public class AdminSimulationsRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        DTORequests dtoRequests = simulationManager.getDTORequests();

        resp.setContentType("application/json");
        if(dtoRequests != null) {
            String responseJsonContent = gson.toJson(dtoRequests);
            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
