package servlets.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.SimulationManager;
import server2client.simulation.thread.data.ThreadData;
import utils.CookiesUtils;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "SendThreadDataServlet", urlPatterns = "/admin/thread/data")
public class SendThreadDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        int threadDataVersion = 0;
        String cookieSavedVersion = CookiesUtils.getSavedValueOnCookie(req, Constants.THREAD_DATA_VERSION);
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());

        if(cookieSavedVersion != null) {
            threadDataVersion = Integer.parseInt(cookieSavedVersion);
        }

        resp.setContentType("application/json");
        if(threadDataVersion < simulationManager.getThreadDataVersion()) {
            ThreadData threadData = simulationManager.getThreadData();
            String responseJsonContent = gson.toJson(threadData);

            if(cookieSavedVersion == null){
                CookiesUtils.createAndSaveNewCookie(resp, String.valueOf(simulationManager.getSimulationBreakdownVersion()), Constants.THREAD_DATA_VERSION);

            } else {
                CookiesUtils.updateValueOnCookie(req, resp, String.valueOf(simulationManager.getSimulationBreakdownVersion()), Constants.THREAD_DATA_VERSION);
            }

            resp.getOutputStream().println(responseJsonContent);

        }
    }
}
