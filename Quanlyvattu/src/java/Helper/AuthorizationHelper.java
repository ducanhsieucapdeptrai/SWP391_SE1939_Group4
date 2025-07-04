/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

/**
 *
 * @author thinh
 */
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.Users;
import model.Role;

public class AuthorizationHelper {

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("currentUser") != null;
    }

    public static boolean hasRole(HttpServletRequest request, String requiredRole) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Users user = (Users) session.getAttribute("currentUser");
        if (user == null || user.getRole() == null) {
            return false;
        }

        Role role = user.getRole();
        return requiredRole.equalsIgnoreCase(role.getRoleName());
    }

    public static boolean hasAnyRole(HttpServletRequest request, String... allowedRoles) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Users user = (Users) session.getAttribute("currentUser");
        if (user == null || user.getRole() == null) {
            return false;
        }

        String userRole = user.getRole().getRoleName();
        for (String role : allowedRoles) {
            if (role.equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        return false;
    }
}
