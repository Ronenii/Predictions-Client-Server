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
import server2client.simulation.queue.AddedSimulationsData;
import utils.CookiesUtils;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SendSimulationsAddedServlet", urlPatterns = "/admin/simulation/added")
public class SendSimulationsAddedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        int simulationsAdded = 0;
        String cookieSavedVersion = CookiesUtils.getSavedValueOnCookie(req, Constants.SIMULATIONS_ADDED);
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());

        if(cookieSavedVersion != null){
            simulationsAdded = Integer.parseInt(cookieSavedVersion);
        }

        resp.setContentType("application/json");
        if(simulationsAdded < simulationManager.getSimulationsAdded()){
            AddedSimulationsData addedSimulations = simulationManager.getAddedSimulationsDTO();
            String responseJsonContent = gson.toJson(addedSimulations);

            if(cookieSavedVersion == null){
                CookiesUtils.createAndSaveNewCookie(resp, String.valueOf(simulationManager.getSimulationsAdded()), Constants.SIMULATIONS_ADDED);
            }else{
                CookiesUtils.updateValueOnCookie(req, resp, String.valueOf(simulationManager.getSimulationsAdded()), Constants.SIMULATIONS_ADDED);
            }

            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
