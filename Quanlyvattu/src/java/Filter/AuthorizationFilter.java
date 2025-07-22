package Filter;

import Helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Authorization filter to check user permissions for accessing servlets
 * This filter will be applied to all requests and check if user has permission
 * to access the requested URL based on the authorization matrix
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {
    
    // URLs that don't require authorization check
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/login", "/logout", "/logout.jsp", "/change_password.jsp", 
        "/css/", "/js/", "/assets/", "/images/", "/favicon.ico",
        "/error", "/403.jsp", "/404.jsp", "/500.jsp",
        "/layout/", "/WEB-INF/", "/META-INF/"
    );
    
    // URLs that require login but no specific permission
    private static final List<String> LOGIN_ONLY_PATHS = Arrays.asList(
        "/dashboard", "/edit-profile", "/change-password",
        "/profile", "/notifications", "/help"
    );
    
    // Critical admin functions that cannot be disabled (always accessible to admin)
    private static final List<String> PROTECTED_ADMIN_PATHS = Arrays.asList(
        "/user-matrix", "/userlist", "/reset-pass-list"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Skip authorization for excluded paths
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        if (!AuthorizationHelper.isLoggedIn(httpRequest)) {
            // Redirect to login page
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // For login-only paths, just check if user is logged in
        if (isLoginOnlyPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if this is a protected admin path
        if (isProtectedAdminPath(path)) {
            // Must have BOTH admin role AND specific permission
            if (isAdminOrManager(httpRequest) && AuthorizationHelper.hasPermission(httpRequest, path)) {
                chain.doFilter(request, response);
                return;
            } else {
                String errorMsg = "Access denied. This protected admin function requires both admin role and specific permission.";
                httpRequest.setAttribute("errorMessage", errorMsg);
                httpRequest.setAttribute("requestedUrl", path);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/error/403.jsp");
                return;
            }
        }
        
        // Check specific permission for the requested path
        if (!AuthorizationHelper.hasPermission(httpRequest, path)) {
            // User doesn't have permission, redirect to 403 error page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/error/403.jsp?error=" + 
                java.net.URLEncoder.encode("You don't have permission to access this resource.", "UTF-8") + 
                "&url=" + java.net.URLEncoder.encode(path, "UTF-8"));
            return;
        }
        
        // User has permission, continue with the request
        chain.doFilter(request, response);
    }
    
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(excluded -> 
            path.startsWith(excluded) || path.equals(excluded)
        );
    }
    
    private boolean isLoginOnlyPath(String path) {
        return LOGIN_ONLY_PATHS.stream().anyMatch(loginOnly -> 
            path.startsWith(loginOnly) || path.equals(loginOnly)
        );
    }
    
    private boolean isProtectedAdminPath(String path) {
        return PROTECTED_ADMIN_PATHS.stream().anyMatch(protectedPath -> 
            path.startsWith(protectedPath) || path.equals(protectedPath)
        );
    }
    
    private boolean isAdminOrManager(HttpServletRequest request) {
        try {
            // Check if user has admin role (Warehouse Manager or higher)
            return AuthorizationHelper.hasAnyRole(request, "Warehouse Manager", "Director", "Admin");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
