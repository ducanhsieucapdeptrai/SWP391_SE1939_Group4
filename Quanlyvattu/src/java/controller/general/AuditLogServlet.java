package controller.general;

import DAO.AuditLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AuditLog;
import model.Users;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling the Audit Log page
 * Displays audit logs with sorting capabilities
 */
@WebServlet(name = "AuditLogServlet", urlPatterns = {"/audit-log"})
public class AuditLogServlet extends HttpServlet {

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
        
        // Get sort parameter, default to descending (newest first)
        String sort = request.getParameter("sort");
        if (sort == null || sort.isEmpty()) {
            sort = "desc"; // Default sort order
        }
        
        try {
            // Fetch audit logs from database
            AuditLogDAO auditLogDAO = new AuditLogDAO();
            // Get audit logs with the specified sort order
            List<AuditLog> auditLogs = auditLogDAO.getAllAuditLogs(sort);
            request.setAttribute("auditLogs", auditLogs);
            request.setAttribute("currentSort", sort);
            
            // Set the page content and forward to the layout
            request.setAttribute("pageContent", "/audit_log.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching audit logs: " + e.getMessage());
            request.setAttribute("pageContent", "/audit_log.jsp");
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
        return "Handles the display of audit logs with sorting capabilities";
    }
}
