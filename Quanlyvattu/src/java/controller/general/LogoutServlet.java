package controller.general;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // Get user info for display
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        // Set all necessary attributes for JSP
        request.setAttribute("currentUser", currentUser);
        
        // Set individual attributes from session for fallback
        request.setAttribute("userName", session.getAttribute("userName"));
        request.setAttribute("userEmail", session.getAttribute("userEmail"));
        request.setAttribute("userRole", session.getAttribute("userRole"));
        request.setAttribute("userImage", session.getAttribute("userImage"));
        
        // Additional fallback - get user info directly from currentUser object
        if (currentUser != null) {
            // Override with actual user data if available
            if (currentUser.getFullName() != null) {
                request.setAttribute("userName", currentUser.getFullName());
            }
            if (currentUser.getEmail() != null) {
                request.setAttribute("userEmail", currentUser.getEmail());
            }
            if (currentUser.getUserImage() != null) {
                request.setAttribute("userImage", currentUser.getUserImage());
            }
        }
        
        // Log for debugging
        logger.info("Logout page accessed by user: " + 
                   (currentUser != null ? currentUser.getEmail() + " (ID: " + currentUser.getUserId() + ")" : "Unknown"));
        
        // Forward to logout confirmation page
        request.getRequestDispatcher("logout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("confirm".equals(action)) {
            performLogout(request, response);
        } else if ("cancel".equals(action)) {
            // Redirect back to dashboard
            response.sendRedirect("dashboard");
        } else {
            // Invalid action, redirect to dashboard
            response.sendRedirect("dashboard");
        }
    }
    
    private void performLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Log the logout action
            Users currentUser = (Users) session.getAttribute("currentUser");
            if (currentUser != null) {
                logger.info("User logged out: " + currentUser.getEmail() + 
                           " (ID: " + currentUser.getUserId() + ")");
            }
            
            // Clear all session attributes
            session.removeAttribute("currentUser");
            session.removeAttribute("userId");
            session.removeAttribute("userName");
            session.removeAttribute("userEmail");
            session.removeAttribute("userRole");
            session.removeAttribute("userImage");
            
            // Clear any additional session attributes that might exist
            session.removeAttribute("loggedInUser"); // If you use this attribute name anywhere
            session.removeAttribute("user"); // If you use this attribute name anywhere
            
            // Invalidate the session
            session.invalidate();
        }
        
        // Clear remember me cookie if exists
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberedEmail".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    logger.info("Remember me cookie cleared");
                    break;
                }
            }
        }
        
        // Add cache control headers to prevent back button issues
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Set success message and redirect to login
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("logoutSuccess", "You have been successfully logged out.");
        
        logger.info("User logout completed successfully");
        response.sendRedirect("login");
    }

    @Override
    public String getServletInfo() {
        return "Handles user logout with confirmation and proper session cleanup.";
    }
}