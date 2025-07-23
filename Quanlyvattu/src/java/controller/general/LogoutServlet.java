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
        performLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        performLogout(request, response);
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
            
            // Invalidate the session (this clears all attributes)
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
        newSession.setAttribute("logoutSuccess", "Đăng xuất thành công!");
        
        logger.info("User logout completed successfully");
        response.sendRedirect("login");
    }

    @Override
    public String getServletInfo() {
        return "Simple logout servlet - performs immediate logout without confirmation.";
    }
}
