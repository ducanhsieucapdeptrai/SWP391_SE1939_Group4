/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 *
 * @author Anonymous
 */
public class ImageHelper {

    private final String SAVE_URL = "/assets/images/UserImage/";
    private final String PROJECT_PATH;

    public ImageHelper(HttpServlet servlet) {
        this.PROJECT_PATH = servlet.getServletContext().getRealPath(SAVE_URL);
    }

    public String processImageUpload(Part imagePart, String imgName) throws IOException, ServletException {
        // Ensure that the image part is not null
        if (imagePart == null || imagePart.getSize() < 10) {
            throw new ServletException("Image part is missing");
        }
        // Make sure the directory exists
        // 1 build
        // 1 project
        File uploadDir = new File(PROJECT_PATH);//build
        File uploadProjectDir = new File(PROJECT_PATH.replace("\\build", ""));

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        if (!uploadProjectDir.exists()) {
            uploadProjectDir.mkdirs(); // img
        }
        String contentType = getExtensionFromContentType(imagePart.getContentType());
        // Generate the complete path to save the image
        String imagePath = uploadDir + File.separator + imgName + contentType;
        String imageProjectPath = uploadProjectDir + File.separator + imgName + contentType;

        // Write the image to the directory
        try (InputStream input = imagePart.getInputStream()) {
            Files.copy(input, Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
        }
        try (InputStream input = imagePart.getInputStream()) {
            Files.copy(input, Paths.get(imageProjectPath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Return the relative URL path that can be used to access the image
        return imgName + contentType;
    }

    public String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            default:
                return "." + contentType.split("/")[1]; // Unsupported type image/webp => .webp
        }
    }

    // remove 2 image from build and real project
    public boolean removeImage(String imageUrl) {
        File uploadDir = new File(PROJECT_PATH);
        File uploadProjectDir = new File(PROJECT_PATH.replace("\\build", ""));

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        if (!uploadProjectDir.exists()) {
            uploadProjectDir.mkdirs();
        }

        String imagePath = uploadDir + File.separator + imageUrl;
        String imageProjectPath = uploadProjectDir + File.separator + imageUrl;
        return new File(imagePath).delete()//build
                && new File(imageProjectPath).delete();//project
    }
}
