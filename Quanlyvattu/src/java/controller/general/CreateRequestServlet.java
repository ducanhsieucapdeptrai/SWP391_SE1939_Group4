package controller.general;

import DAO.CreateRequestExport_PurcharDAO;
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
    
    private final CreateRequestExport_PurcharDAO dao = new CreateRequestExport_PurcharDAO();

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
                        List<RequestSubType> subTypes = dao.getAllRequestSubtypes();
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
                        List<SubCategory> subCats = dao.getSubCategoriesByCategoryId(catId);
                        out.println("<option value=''>--All--</option>");
                        for (SubCategory sc : subCats) {
                            out.println("<option value='" + sc.getSubCategoryId() + "'>" 
                                + sc.getSubCategoryName() + "</option>");
                        }
                        break;
                        
                    case "getMaterials":
                        int subCatId = Integer.parseInt(request.getParameter("subCatId"));
                        List<Material> materials = dao.getMaterialsBySubCategoryId(subCatId);
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

        // Load initial data
        try {
            List<RequestType> types = dao.getAllRequestTypes();
            List<Category> categories = dao.getAllCategories();
            
            request.setAttribute("types", types);
            request.setAttribute("categories", categories);
           request.setAttribute("pageContent", "/createRequest.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading form data");
        }

        doGet(req, resp);
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
            int subTypeId = Integer.parseInt(request.getParameter("subTypeId"));
            String note = request.getParameter("note");
            
            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            
            if (materialIds == null || quantities == null || materialIds.length == 0 
                    || materialIds.length != quantities.length) {
                throw new ServletException("Invalid materials data");
            }
            
            // Create details list
            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialIds.length; i++) {
                RequestDetail detail = new RequestDetail();
                detail.setMaterialId(Integer.parseInt(materialIds[i]));
                detail.setQuantity(Integer.parseInt(quantities[i]));
                details.add(detail);
            }
            
            // Create request
            int requestId = dao.createRequest(userId, typeId, subTypeId, note, details);
            
            if (requestId > 0) {
                session.setAttribute("message", "Request created successfully!");
                response.sendRedirect("reqlist");
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
