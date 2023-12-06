package servlets.client;

import client2server.simulation.control.bar.DTOSimulationControlBar;
import client2server.simulation.request.DTORequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "setStopPausePlayOrSkipFwdServlet", urlPatterns = "/client/simulation/control/bar")
public class setStopPausePlayOrSkipFwdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        String simId = req.getParameter(Constants.SIMULATION_ID);
        DTOSimulationControlBar dtoSimulationControlBar = gson.fromJson(req.getReader(), DTOSimulationControlBar.class);

        synchronized (this) {
            simulationManager.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
        }
    }
}
