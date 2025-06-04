package controller.general;

import DAO.StatisticsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Material;
import model.Users;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servlet for handling the Statistics page
 * Displays material statistics grouped by different criteria
 */
@WebServlet(name = "StatisticsServlet", urlPatterns = {"/statistics"})
public class StatisticsServlet extends HttpServlet {

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
            // Fetch statistics from database
            StatisticsDAO statisticsDAO = new StatisticsDAO();
            
            // Get list of materials
            List<Material> materials = statisticsDAO.getAllMaterials();
            
            // Get counts by status
            Map<String, Integer> materialsByStatus = statisticsDAO.getMaterialCountByStatus();
            
            // Get counts by type
            Map<String, Integer> materialsByType = statisticsDAO.getMaterialCountByType();
            
            // Set attributes for JSP
            request.setAttribute("materials", materials);
            request.setAttribute("materialsByStatus", materialsByStatus);
            request.setAttribute("materialsByType", materialsByType);
            
            // Set the page content and forward to the layout
            request.setAttribute("pageContent", "/statistics.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching statistics: " + e.getMessage());
            request.setAttribute("pageContent", "/statistics.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the display of material statistics";
    }
}
