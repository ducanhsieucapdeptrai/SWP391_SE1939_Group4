package controller.general;

import DAO.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for handling the Category Management page
 * Supports CRUD operations for categories
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {"/category-management"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }
        
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            
            String sortBy = request.getParameter("sortBy");
            String searchTerm = request.getParameter("search");
            
            List<Category> categories = categoryDAO.getCategories(sortBy, searchTerm);
            
            request.setAttribute("categories", categories);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("searchTerm", searchTerm);
            
            request.setAttribute("pageContent", "/category_management.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching categories: " + e.getMessage());
            request.setAttribute("pageContent", "/category_management.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            
            if ("add".equals(action)) {
                String name = request.getParameter("name");
                
                if (name != null && !name.trim().isEmpty()) {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(name);
                    
                    boolean success = categoryDAO.addCategory(newCategory);
                    if (success) {
                        request.setAttribute("successMessage", "Category added successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to add category.");
                    }
                } else {
                    request.setAttribute("errorMessage", "Name is required.");
                }
            } else if ("edit".equals(action)) {
                String idStr = request.getParameter("id");
                String name = request.getParameter("name");
                
                if (idStr != null && name != null && !name.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    
                    Category category = new Category();
                    category.setCategoryId(id);
                    category.setCategoryName(name);
                    
                    boolean success = categoryDAO.updateCategory(category);
                    if (success) {
                        request.setAttribute("successMessage", "Category updated successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update category.");
                    }
                } else {
                    request.setAttribute("errorMessage", "ID and name are required.");
                }
            } else if ("delete".equals(action)) {
                String idStr = request.getParameter("id");
                
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    
                    boolean success = categoryDAO.deleteCategory(id);
                    if (success) {
                        request.setAttribute("successMessage", "Category deleted successfully.");
                    } else {
                        request.setAttribute("errorMessage", "Failed to delete category. Make sure no subcategories are using it.");
                    }
                } else {
                    request.setAttribute("errorMessage", "ID is required for deletion.");
                }
            }
            
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
        }
        
        request.setAttribute("pageContent", "/category_management.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the CRUD operations for category management";
    }
}
