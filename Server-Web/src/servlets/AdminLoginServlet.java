package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/connect"})
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if(userManager.isAdminConnected){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            userManager.isAdminConnected = true;
        }
    }
}
