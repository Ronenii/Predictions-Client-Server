package servlets;

import client2server.simulation.load.DTOLoadFile;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import server2client.simulation.load.success.DTOLoadResult;
import utils.ServletUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import java.io.IOException;

@WebServlet(name = "FileUploadServlet", urlPatterns = "/admin/file_upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");

        // Uploading a file is multipart, in here we create a collection of all the given file parts.
        Collection<Part> partCollection = req.getParts();

        // We create a temporary file to pass to the engine
        Path tempFile = Files.createTempFile("temp", ".tmp");

        for (Part part : partCollection) {
            InputStream partInputStream = part.getInputStream();

            // Append the content of each part to the temporary file
            Files.copy(partInputStream, tempFile);
        }

        // We try to convert the given file to a simulation definition
        DTOLoadResult dtoLoadResult = ServletUtils.getSimulationManager(getServletContext()).loadSimulationFromFile(tempFile.toFile());

        // If the simulation file wasn't loaded because of a problem with the content set the response as a bad request.
        if (!dtoLoadResult.isSucceed()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Convert the load result to a json and write it to the response.
        String responseJsonContent = gson.toJson(dtoLoadResult);

        resp.getOutputStream().println(responseJsonContent);
    }
}
