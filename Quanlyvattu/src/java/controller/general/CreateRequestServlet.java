// CreateRequestServlet.java
package controller.general;

import DAO.CreateRequestExport_PurcharDAO;
import DAO.ProjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/createrequest")
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            CreateRequestExport_PurcharDAO dao = new CreateRequestExport_PurcharDAO();
            List<RequestType> types = dao.getAllRequestTypes();
            List<Material> materials = dao.getAllMaterials();
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projectList = projectDAO.getAllActiveProjects();

            request.setAttribute("types", types);
            request.setAttribute("materials", materials);
            request.setAttribute("projectList", projectList);
            System.out.println("Loaded materials count: " + materials.size());

            if ("1".equals(request.getParameter("success"))) {
                request.setAttribute("message", "Request created successfully!");
            }

            request.setAttribute("pageContent", "/createRequest.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            int typeId = Integer.parseInt(request.getParameter("typeId"));
            int subTypeId = (typeId == 1) ? 1 : Integer.parseInt(request.getParameter("subTypeId"));
            String note = request.getParameter("note");

            String projectRaw = request.getParameter("projectId");
            Integer projectId = (projectRaw != null && !projectRaw.isEmpty()) ? Integer.parseInt(projectRaw) : null;

            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");

            if (materialIds == null || quantities == null || materialIds.length != quantities.length) {
                throw new ServletException("Invalid materials data");
            }

            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialIds.length; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setMaterialId(Integer.parseInt(materialIds[i]));
                detail.setQuantity(Integer.parseInt(quantities[i]));
                details.add(detail);
            }

            CreateRequestExport_PurcharDAO dao = new CreateRequestExport_PurcharDAO();
            int requestId = dao.createRequest(userId, typeId, subTypeId, note, details, projectId);

            if (requestId > 0) {
                response.sendRedirect("createrequest?success=1");
            } else {
                request.setAttribute("error", "Failed to create request");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
