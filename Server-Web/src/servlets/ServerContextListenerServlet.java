package servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import manager.SimulationManager;
import utils.ServletUtils;


public class ServerContextListenerServlet implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SimulationManager simulationManager = ServletUtils.getSimulationManager(sce.getServletContext());
        simulationManager.shutdownThreadPool();
    }
}
