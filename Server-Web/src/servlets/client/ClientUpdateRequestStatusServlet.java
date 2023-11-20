package servlets.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "ClientUpdateRequestStatusServlet", urlPatterns = {"/client/request/status"})
public class ClientUpdateRequestStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(req);
        DTORequestStatusUpdate dtoRequestStatusUpdate = simulationManager.getDtoRequestStatusUpdate(usernameFromSession);

        resp.setContentType("application/json");
        String responseJsonContent = gson.toJson(dtoRequestStatusUpdate);
        resp.getOutputStream().println(responseJsonContent);
    }
}
