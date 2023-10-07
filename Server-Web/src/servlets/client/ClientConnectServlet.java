package servlets.client;

import constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "ClientConnectServlet", urlPatterns = {"/client/connect"})
public class ClientConnectServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(req);

        if(usernameFromSession == null) {
            //The user never logged in
            String usernameFromParameter = req.getParameter(Constants.USERNAME);

            if(usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                // The username attribute is empty or null.
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if(userManager.isUserExists(usernameFromParameter)) {
                        // A username with the given name is already exists.
                        resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    } else {
                        userManager.addUser(usernameFromParameter);
                        // create session for this user.
                        req.getSession().setAttribute(Constants.USERNAME, usernameFromParameter);
                    }
                }
            }
        }
    }
}
