package controller.staff;

import DAO.ProjectDAO;
import DAO.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Project;
import model.RequestList;
import model.Users;

import java.io.IOException;
import java.util.List;

@WebServlet("/projectDetail")
public class ProjectDetailServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("currentUser");

            if (user == null || (user.getRoleId() != 1 && user.getRoleId() != 3 && user.getRoleId() != 4)) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            String idRaw = request.getParameter("id");
            if (idRaw == null) {
                response.sendRedirect("project");
                return;
            }

            int projectId = Integer.parseInt(idRaw);

            Project project = projectDAO.getProjectById(projectId);
            if (project == null) {
                response.sendRedirect("project");
                return;
            }

            List<RequestList> requestList = requestDAO.getRequestsByProject(projectId);
            request.setAttribute("project", project);
            request.setAttribute("requestList", requestList);

            boolean hasRequests = !requestList.isEmpty();
            boolean allCompleted = hasRequests && projectDAO.areAllRequestsCompleted(projectId);
            request.setAttribute("allCompleted", allCompleted);

            boolean canConfirm = (user.getRoleId() == 3 && allCompleted);
            request.setAttribute("canConfirmCompletion", canConfirm);

            String successMsg = (String) session.getAttribute("successMsg");
            if (successMsg != null) {
                request.setAttribute("successMsg", successMsg);
                session.removeAttribute("successMsg");
            }

            request.setAttribute("pageContent", "/projectDetail.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("project");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null || user.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        if ("confirmComplete".equals(action)) {
            boolean allDone = projectDAO.areAllRequestsCompleted(projectId);
            if (allDone) {
                projectDAO.markProjectAsCompleted(projectId);
                session.setAttribute("successMsg", "✅ Project marked as completed successfully.");
            }
        }

        response.sendRedirect("projectDetail?id=" + projectId);
    }
}
