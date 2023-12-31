package servlets.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "AdminConnectServlet", urlPatterns = {"/admin/connect"})
public class AdminConnectServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if(userManager.isAdminConnected){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            userManager.isAdminConnected = true;
        }
    }
}
