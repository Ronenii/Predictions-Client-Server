package servlets.client;

import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.execution.SetResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SendPopulationDataServlet", urlPatterns = "/client/execution/population")
public class SendPopulationDataServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        EntityPopulationUserInput entityPopulationUserInput = gson.fromJson(req.getReader(), EntityPopulationUserInput.class);

        synchronized (this){
            SetResponse response = simulationManager.setEntityPopulation(entityPopulationUserInput);

            resp.setContentType("application/json");
            String responseJsonContent = gson.toJson(response);
            resp.getOutputStream().println(responseJsonContent);
        }
    }
}
