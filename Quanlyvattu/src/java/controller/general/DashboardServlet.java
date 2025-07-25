package controller.general;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import DAO.MaterialDAO;
import model.Material;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the advanced dashboard page
 * Displays the main dashboard with buttons for Audit Log, Statistics, and Catalog Management
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/advanced-dashboard"})
public class DashboardServlet extends HttpServlet {

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
        
        // Fetch recent material entries
       
        // Set the page content and forward to the layout
        request.setAttribute("pageContent", "/dashboard.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the advanced dashboard display with recent material entries";
    }
}
