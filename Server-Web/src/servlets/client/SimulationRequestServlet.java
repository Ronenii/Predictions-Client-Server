package servlets.client;

import client2server.simulation.request.DTORequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "SimulationRequestServlet", urlPatterns = {"/client/request"})
public class SimulationRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(req);
        DTORequest dtoRequest = gson.fromJson(req.getReader(), DTORequest.class);

        synchronized (this){
            int reqId = simulationManager.addNewRequest(dtoRequest, usernameFromSession);
            resp.setContentType("text/plain");
            resp.getWriter().print(reqId);
        }


    }
}
