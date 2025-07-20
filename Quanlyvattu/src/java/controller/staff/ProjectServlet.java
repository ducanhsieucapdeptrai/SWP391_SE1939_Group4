package controller.staff;

import DAO.ProjectDAO;
import DAO.UserDAO;
import model.Project;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@WebServlet("/project")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
                 maxFileSize = 10 * 1024 * 1024,   // 10MB
                 maxRequestSize = 20 * 1024 * 1024)
public class ProjectServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final UserDAO userDAO = new UserDAO();

    private boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) return false;
        int roleId = user.getRoleId();
        return roleId == 1 || roleId == 3 || roleId == 4;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthorized(request)) {
            response.sendRedirect("dashboard");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "form":
                handleForm(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            default:
                handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String projectName = request.getParameter("projectName");
        String managerName = request.getParameter("managerName");
        String createdDate = request.getParameter("createdDate");
        String status = request.getParameter("status");

        String pageParam = request.getParameter("page");
        int currentPage = 1;
        int pageSize = 10;

        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Integer.parseInt(pageParam);
        }

        int offset = (currentPage - 1) * pageSize;

        List<Project> projects;
        int totalProjects;
        boolean hasFilter = (projectName != null && !projectName.isEmpty()) ||
                            (managerName != null && !managerName.isEmpty()) ||
                            (createdDate != null && !createdDate.isEmpty()) ||
                            (status != null && !status.isEmpty());

        if (hasFilter) {
            projects = projectDAO.getFilteredProjectsPaged(projectName, managerName, createdDate, status, offset, pageSize);
            totalProjects = projectDAO.countFilteredProjects(projectName, managerName, createdDate, status);
        } else {
            projects = projectDAO.getPagedProjects(offset, pageSize);
            totalProjects = projectDAO.countAllProjects();
        }

        int totalPages = (int) Math.ceil((double) totalProjects / pageSize);
        if (totalPages == 0) totalPages = 1;

        request.setAttribute("projectName", projectName);
        request.setAttribute("managerName", managerName);
        request.setAttribute("createdDate", createdDate);
        request.setAttribute("status", status);
        request.setAttribute("projects", projects);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("pageContent", "/projectList.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    private void handleForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        Project project = null;

        if (idRaw != null) {
            try {
                int id = Integer.parseInt(idRaw);
                project = projectDAO.getProjectById(id);
            } catch (NumberFormatException ignored) {}
        }

        List<Users> managers = userDAO.getUsersByRoleNames(List.of("Warehouse Manager", "Director"));

        request.setAttribute("project", project);
        request.setAttribute("managers", managers);
        request.setAttribute("pageContent", "/projectForm.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw != null) {
            try {
                int id = Integer.parseInt(idRaw);
                projectDAO.softDeleteProject(id);
            } catch (NumberFormatException ignored) {}
        }
        response.sendRedirect("project");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthorized(request)) {
            response.sendRedirect("dashboard");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String idRaw = request.getParameter("projectId");

        try {
            String name = request.getParameter("projectName");
            String desc = request.getParameter("description");
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            int managerId = Integer.parseInt(request.getParameter("managerId"));
            String status = request.getParameter("status");

            Project p;
            boolean isEdit = idRaw != null && !idRaw.isEmpty();
            if (isEdit) {
                int id = Integer.parseInt(idRaw);
                p = projectDAO.getProjectById(id);
                if (p == null) p = new Project();
                p.setProjectId(id);
            } else {
                p = new Project();
            }

            p.setProjectName(name);
            p.setDescription(desc);
            p.setStartDate(startDate);
            p.setEndDate(endDate);
            p.setManagerId(managerId);
            p.setStatus(status);

            Part filePart = request.getPart("attachment");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString().replaceAll("\\s+", "_");

                if (!fileName.isEmpty()) {
                    String uploadDir = getServletContext().getRealPath("/uploads");
                    Files.createDirectories(Paths.get(uploadDir));

                    String savedFileName = System.currentTimeMillis() + "_" + fileName;
                    String savedPath = "uploads/" + savedFileName;
                    filePart.write(uploadDir + File.separator + savedFileName);

                    if (isEdit && p.getAttachmentPath() != null) {
                        String oldPath = getServletContext().getRealPath("/") + p.getAttachmentPath();
                        File oldFile = new File(oldPath);
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                    }

                    p.setAttachmentPath(savedPath);
                }
            } else {
                if (!isEdit) {
                    p.setAttachmentPath(null);
                }
            }

            if (isEdit) {
                projectDAO.updateProject(p);
                response.sendRedirect("projectDetail?id=" + p.getProjectId());
            } else {
                projectDAO.insertProject(p);
                response.sendRedirect("project");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("project");
        }
    }
}
