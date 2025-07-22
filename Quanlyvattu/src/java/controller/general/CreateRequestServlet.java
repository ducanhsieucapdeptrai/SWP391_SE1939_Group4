package controller.general;

import DAO.CreateRequestExport_PurcharDAO;
import DAO.ProjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/createrequest")
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            try {
                switch (action) {
                    case "getSubTypes":
                        int typeId = Integer.parseInt(request.getParameter("typeId"));
                        CreateRequestExport_PurcharDAO dao1 = new CreateRequestExport_PurcharDAO();
                        List<RequestSubType> subTypes = dao1.getAllRequestSubtypes();
                        out.println("<option value=''>--Select--</option>");
                        for (RequestSubType st : subTypes) {
                            if (st.getRequestTypeId() == typeId) {
                                out.println("<option value='" + st.getSubTypeId() + "'>" 
                                    + st.getSubTypeName() + "</option>");
                            }
                        }
                        break;

                    case "getSubCategories":
                        int catId = Integer.parseInt(request.getParameter("catId"));
                        CreateRequestExport_PurcharDAO dao2 = new CreateRequestExport_PurcharDAO();
                        List<SubCategory> subCats = dao2.getSubCategoriesByCategoryId(catId);
                        out.println("<option value=''>--All--</option>");
                        for (SubCategory sc : subCats) {
                            out.println("<option value='" + sc.getSubCategoryId() + "'>" 
                                + sc.getSubCategoryName() + "</option>");
                        }
                        break;

                    case "getMaterials":
                        int subCatId = Integer.parseInt(request.getParameter("subCatId"));
                        CreateRequestExport_PurcharDAO dao3 = new CreateRequestExport_PurcharDAO();
                        List<Material> materials = dao3.getMaterialsBySubCategoryId(subCatId);
                        out.println("<option value=''>--Select--</option>");
                        for (Material m : materials) {
                            out.println("<option value='" + m.getMaterialId() + "' "
                                + "data-stock='" + m.getQuantity() + "' "
                                + "data-min='" + m.getMinQuantity() + "' "
                                + "data-name='" + m.getMaterialName() + "' "
                                + "data-img='assets/images/materials/" + m.getImage() + "'>"
                                + m.getMaterialName() + "</option>");
                        }
                        break;
                }
                return;

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter");
                return;
            }
        }

        // Load initial form data
        try {
            CreateRequestExport_PurcharDAO dao = new CreateRequestExport_PurcharDAO();
            List<RequestType> types = dao.getAllRequestTypes();
            List<Category> categories = dao.getAllCategories();

            // Load active projects for dropdown
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projectList = projectDAO.getAllActiveProjects();

            request.setAttribute("types", types);
            request.setAttribute("categories", categories);
            request.setAttribute("projectList", projectList);

            // Show message if redirected after success
            String success = request.getParameter("success");
            if ("1".equals(success)) {
                request.setAttribute("message", "Request created successfully!");
            }

            request.setAttribute("pageContent", "/createRequest.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading form data");
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

            String subTypeParam = request.getParameter("subTypeId");
            Integer subTypeId = null;
            if (subTypeParam != null && !subTypeParam.trim().isEmpty()) {
                int parsedSubTypeId = Integer.parseInt(subTypeParam);
                subTypeId = (parsedSubTypeId == 0) ? null : parsedSubTypeId;
            }

            String note = request.getParameter("note");

            // Get projectId (optional)
            String projectIdRaw = request.getParameter("projectId");
            Integer projectId = null;
            if (projectIdRaw != null && !projectIdRaw.trim().isEmpty()) {
                projectId = Integer.parseInt(projectIdRaw);
            }

            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");

            if (materialIds == null || quantities == null || materialIds.length == 0 
                    || materialIds.length != quantities.length) {
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
