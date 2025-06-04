package controller.general;

import DAO.CatalogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Catalog;
import model.Users;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the Catalog Management page
 * Supports CRUD operations for catalog items
 */
@WebServlet(name = "CatalogServlet", urlPatterns = {"/catalog-management"})
public class CatalogServlet extends HttpServlet {

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
            // Fetch catalog items from database
            CatalogDAO catalogDAO = new CatalogDAO();
            List<Catalog> catalogItems = catalogDAO.getAllCatalogItems();
            
            // Set attributes for JSP
            request.setAttribute("catalogItems", catalogItems);
            
            // Set the page content and forward to the layout
            request.setAttribute("pageContent", "/catalog_management.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching catalog items: " + e.getMessage());
            request.setAttribute("pageContent", "/catalog_management.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
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
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            CatalogDAO catalogDAO = new CatalogDAO();
            
            if ("add".equals(action)) {
                // Add new catalog item
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                
                if (name != null && !name.trim().isEmpty()) {
                    Catalog newItem = new Catalog();
                    newItem.setName(name);
                    newItem.setDescription(description);
                    
                    boolean success = catalogDAO.addCatalogItem(newItem);
                    if (success) {
                        request.setAttribute("successMessage", "Catalog item added successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to add catalog item.");
                    }
                } else {
                    request.setAttribute("errorMessage", "Name is required.");
                }
            } else if ("edit".equals(action)) {
                // Edit existing catalog item
                String idStr = request.getParameter("id");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                
                if (idStr != null && name != null && !name.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    
                    Catalog item = new Catalog();
                    item.setId(id);
                    item.setName(name);
                    item.setDescription(description);
                    
                    boolean success = catalogDAO.updateCatalogItem(item);
                    if (success) {
                        request.setAttribute("successMessage", "Catalog item updated successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update catalog item.");
                    }
                } else {
                    request.setAttribute("errorMessage", "ID and name are required.");
                }
            } else if ("delete".equals(action)) {
                // Delete catalog item
                String idStr = request.getParameter("id");
                
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    
                    boolean success = catalogDAO.deleteCatalogItem(id);
                    if (success) {
                        request.setAttribute("successMessage", "Catalog item deleted successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to delete catalog item.");
                    }
                } else {
                    request.setAttribute("errorMessage", "ID is required for deletion.");
                }
            }
            
            // Fetch updated catalog items
            List<Catalog> catalogItems = catalogDAO.getAllCatalogItems();
            request.setAttribute("catalogItems", catalogItems);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
        }
        
        // Forward back to the catalog management page
        request.setAttribute("pageContent", "/catalog_management.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the CRUD operations for catalog management";
    }
}
