package controller.general;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Material;
import model.Users;
import model.MaterialStatus;
import DAO.MaterialDAO;
import DAO.CatalogDAO;
import DAO.MaterialStatusDAO;
import DAO.NotificationDAO;
import model.Catalog;
import model.SubCategory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AddMaterialServlet", urlPatterns = {"/material-add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class AddMaterialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Fetch categories, subcategories, and statuses for dropdowns
            CatalogDAO catalogDAO = new CatalogDAO();
            MaterialStatusDAO statusDAO = new MaterialStatusDAO();

            List<Catalog> categories = catalogDAO.getAllCategories();
            List<SubCategory> subcategories = catalogDAO.getAllSubCategories();
            List<MaterialStatus> statuses = statusDAO.getAllStatuses();

            request.setAttribute("categories", categories);
            // Manually build JSON string for subcategories to safely pass to JavaScript
            StringBuilder subcategoriesJson = new StringBuilder();
            subcategoriesJson.append("[");
            for (int i = 0; i < subcategories.size(); i++) {
                SubCategory sub = subcategories.get(i);
                if (sub == null || sub.getSubCategoryName() == null) {
                    continue; // Defensive coding
                }
                subcategoriesJson.append("{");
                subcategoriesJson.append("\"id\":").append(sub.getSubCategoryId()).append(",");
                // Escape all special characters to create a valid JSON string
                String safeName = sub.getSubCategoryName()
                        .replace("\\", "\\\\") // 1. backslash
                        .replace("\"", "\\\"") // 2. double quote
                        .replace("\b", "\\b") // 3. backspace
                        .replace("\f", "\\f") // 4. form feed
                        .replace("\n", "\\n") // 5. newline
                        .replace("\r", "\\r") // 6. carriage return
                        .replace("\t", "\\t");    // 7. tab
                subcategoriesJson.append("\"name\":\"").append(safeName).append("\",");
                subcategoriesJson.append("\"categoryId\":").append(sub.getCategoryId());
                subcategoriesJson.append("}");
                if (i < subcategories.size() - 1) {
                    subcategoriesJson.append(",");
                }
            }
            subcategoriesJson.append("]");

            request.setAttribute("subcategories", subcategories); // Keep for compatibility if needed elsewhere
            request.setAttribute("subcategoriesJson", subcategoriesJson.toString());
            request.setAttribute("statuses", statuses);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading form data: " + e.getMessage());
        }

        // Set the page content and forward to the layout
        request.setAttribute("pageContent", "/add-material.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Get form data
            String materialName = request.getParameter("materialName");
            int subCategoryId = Integer.parseInt(request.getParameter("subCategoryId"));
            int statusId = Integer.parseInt(request.getParameter("statusId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int minQuantity = Integer.parseInt(request.getParameter("minQuantity"));
            double price = Double.parseDouble(request.getParameter("price"));
            String description = request.getParameter("description");

            // Handle file upload
            String imagePath = null;
            Part filePart = request.getPart("image");

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String fileExtension = "";
                int lastDot = fileName.lastIndexOf('.');
                if (lastDot > 0) {
                    fileExtension = fileName.substring(lastDot);
                }

                // Generate unique filename
                String uniqueFileName = "material_" + UUID.randomUUID().toString() + fileExtension;

                // Get the upload directory path
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "images" + File.separator + "materials";

                // Create upload directory if it doesn't exist
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Save the file
                String filePath = uploadPath + File.separator + uniqueFileName;
                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                }

                // Set the relative path for database
                imagePath = "assets/images/materials/" + uniqueFileName;
            } else {
                // Set default image if no image is uploaded
                imagePath = "assets/images/materials/default.png";
            }

            // Create material object
            Material material = new Material();
            material.setMaterialName(materialName);
            material.setSubCategoryId(subCategoryId);
            material.setStatusId(statusId);
            material.setQuantity(quantity);
            material.setMinQuantity(minQuantity);
            material.setPrice(price);
            material.setDescription(description);
            material.setImage(imagePath);


            // Add material to database
            MaterialDAO materialDAO = new MaterialDAO();
            boolean success = materialDAO.addMaterial(material);

            if (success) {
                // 🔔 Thông báo vật tư mới
                try {
                    NotificationDAO notiDAO = new NotificationDAO();
                    notiDAO.createMaterialNotification(material.getMaterialId(), material.getMaterialName());
                } catch (Exception e) {
                    e.printStackTrace(); // Không crash nếu lỗi noti
                }

                request.setAttribute("successMessage", "Material added successfully!");
                request.setAttribute("material", null); // Xoá form
            } else {
                request.setAttribute("errorMessage", "Failed to add material. Please try again.");
                request.setAttribute("material", material); // Giữ lại dữ liệu
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing form: " + e.getMessage());
        }

        // Redirect back to the add material page
        doGet(request, response);
    }
}
