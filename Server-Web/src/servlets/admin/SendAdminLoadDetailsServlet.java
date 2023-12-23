package servlets.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.admin.load.details.AdminLoadDetails;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "SendAdminLoadDetailsServlet", urlPatterns = {"/admin/load/details"})
public class SendAdminLoadDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if(!userManager.isFirstAdmin()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
            AdminLoadDetails adminLoadDetails = simulationManager.getAdminLoadDetails();

            resp.setContentType("application/json");
            String responseJsonContent = gson.toJson(adminLoadDetails);
            resp.getOutputStream().println(responseJsonContent);
        }

        userManager.changeFirstAdmin();
    }
}
