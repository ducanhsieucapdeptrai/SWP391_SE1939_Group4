package controller;

import DAO.UsersDAO;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@WebServlet(name = "GoogleLoginServlet", urlPatterns = {"/google-login"})
public class GoogleLoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "465186853940-n9tq656f08eojf173dcmjhh7gvkcfar3.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:9999/Quanlyvattu/google-login";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null || code.isEmpty()) {
            String generatedState = UUID.randomUUID().toString();
            session.setAttribute("oauth2_state", generatedState);

            String authUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                    + "?scope=email%20profile"
                    + "&access_type=online"
                    + "&include_granted_scopes=true"
                    + "&prompt=select_account"
                    + "&response_type=code"
                    + "&redirect_uri=" + REDIRECT_URI
                    + "&client_id=" + CLIENT_ID
                    + "&state=" + generatedState;

            response.sendRedirect(authUrl);
            return;
        }

        String expectedState = (String) session.getAttribute("oauth2_state");
        if (expectedState == null || !expectedState.equals(state)) {
            request.setAttribute("errorMsg", "Invalid state parameter (potential CSRF attack).");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            ).execute();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(tokenResponse.getIdToken());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                Users user = UsersDAO.getUserByEmail(email);
                if (user == null) {
                    session.invalidate();
                    request.setAttribute("errorMsg", "Your Gmail is not registered in the system!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                if (!user.isIsActive()) {
                    session.invalidate();
                    request.setAttribute("errorMsg", "Your account is deactivated. Please contact the administrator.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("currentUser", user);

//                String redirectUrl = switch (user.getRoleId()) {
//                    case 1 ->
//                        "admin/dashboard.jsp";
//                    case 2 ->
//                        "staff/dashboard.jsp";
//                    case 3 ->
//                        "director/dashboard.jsp";
//                    case 4 ->
//                        "company/home.jsp";
//                    default ->
//                        null;
//                };
//
//                if (redirectUrl != null) {
//                    response.sendRedirect(redirectUrl);
//                } else {
//                    session.invalidate();
//                    request.setAttribute("errorMsg", "Unrecognized user role. Please contact the administrator.");
//                    request.getRequestDispatcher("login.jsp").forward(request, response);
//                }
                response.sendRedirect("homepage.jsp");

            } else {
                session.invalidate();
                request.setAttribute("errorMsg", "Invalid Google ID Token.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            session.invalidate();
            request.setAttribute("errorMsg", "Security or network error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.invalidate();
            request.setAttribute("errorMsg", "Login error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Google OAuth 2.0 Login Servlet with role handling and account check.";
    }
}
