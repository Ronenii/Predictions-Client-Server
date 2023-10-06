package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

/**
 * Responsible for handling whenever a client tries to get all possible simulations.
 */
@WebServlet (name = "SendSimulationDefinitionsServlet", urlPatterns = "/get_simulation_definitions")
public class SendSimulationDefinitionsServlet extends HttpServlet {

}
