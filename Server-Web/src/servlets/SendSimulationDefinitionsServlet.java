package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet (name = "SendSimulationDefinitionsServlet", urlPatterns = "/get_simulation_definitions")
public class SendSimulationDefinitionsServlet extends HttpServlet {

}
