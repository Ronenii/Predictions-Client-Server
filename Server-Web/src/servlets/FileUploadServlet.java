package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import manager.SimulationManager;
import server2client.simulation.load.result.DTOLoadResult;
import utils.ServletUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

        // Generate a unique name for the temporary file
        String uniqueFileName = "temp_" + System.currentTimeMillis() + ".xml";
        Path tempFile = Files.createTempFile(null, uniqueFileName);

        for (Part part : partCollection) {
            try (InputStream inputStream = part.getInputStream()) {
                // TODO: Exception while trying to copy.
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        // We try to convert the given file  to a simulation definition
        SimulationManager manager = ServletUtils.getSimulationManager(getServletContext());
        File file = tempFile.toFile();
        DTOLoadResult dtoLoadResult = manager.loadSimulationFromFile(file);

        // If the simulation file wasn't loaded because of a problem with the content set the response as a bad request.
        if (!dtoLoadResult.isSucceed()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Convert the load result to a json and write it to the response.
        String responseJsonContent = gson.toJson(dtoLoadResult);

        resp.getOutputStream().println(responseJsonContent);

        // Delete the temporary file after we are finished using it.
        Files.delete(tempFile);
    }
}
