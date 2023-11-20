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

@WebServlet(name = "ChangeRequestStatusServlet", urlPatterns = {"/admin/requests/status"})
public class ChangeRequestStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimulationManager manager = ServletUtils.getSimulationManager(getServletContext());
        int reqId = Integer.parseInt(req.getParameter(Constants.REQUEST_ID));
        String reqStatus = req.getParameter(Constants.REQUEST_STATUS);

        manager.changeRequestStatus(reqId, reqStatus);

    }
}
