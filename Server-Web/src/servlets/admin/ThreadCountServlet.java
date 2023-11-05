package servlets.admin;

import constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import utils.ServletUtils;


import java.io.IOException;

@WebServlet(name = "ThreadCountServlet", urlPatterns = {"/admin/thread/count"})
public class ThreadCountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimulationManager manager = ServletUtils.getSimulationManager(getServletContext());
        String simName = req.getParameter(Constants.SIMULATION_NAME);
        int threadCount = Integer.parseInt(req.getParameter(Constants.THREAD_COUNT));

        manager.updateSimulationThreadCount(simName, threadCount);
    }
}
