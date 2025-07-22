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
                        System.out.println("[DEBUG] getSubTypes AJAX called");
                        int typeId = Integer.parseInt(request.getParameter("typeId"));
                        System.out.println("[DEBUG] typeId: " + typeId);
                        CreateRequestExport_PurcharDAO dao1 = new CreateRequestExport_PurcharDAO();
                        List<RequestSubType> subTypes = dao1.getAllRequestSubtypes();
                        System.out.println("[DEBUG] subTypes loaded: " + (subTypes != null ? subTypes.size() : "null"));
                        
                        out.println("<option value=''>--Select--</option>");
                        int matchCount = 0;
                        for (RequestSubType st : subTypes) {
                            if (st.getRequestTypeId() == typeId) {
                                out.println("<option value='" + st.getSubTypeId() + "'>" 
                                    + st.getSubTypeName() + "</option>");
                                matchCount++;
                            }
                        }
                        System.out.println("[DEBUG] Matching subtypes found: " + matchCount);
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

        // Check for success parameter
        String success = request.getParameter("success");
        if ("1".equals(success)) {
            request.setAttribute("message", "Request created successfully!");
        }
        
        // Load initial data
        try {
            System.out.println("[DEBUG] Loading initial data for createRequest...");
            CreateRequestExport_PurcharDAO dao = new CreateRequestExport_PurcharDAO();
            List<RequestType> types = dao.getAllRequestTypes();
            List<Category> categories = dao.getAllCategories();
            
            System.out.println("[DEBUG] Types loaded: " + (types != null ? types.size() : "null"));
            System.out.println("[DEBUG] Categories loaded: " + (categories != null ? categories.size() : "null"));
            
            if (types != null && !types.isEmpty()) {
                System.out.println("[DEBUG] First type: " + types.get(0).getRequestTypeName());
            }
            if (categories != null && !categories.isEmpty()) {
                System.out.println("[DEBUG] First category: " + categories.get(0).getCategoryName());
            }
            
            request.setAttribute("types", types);
            request.setAttribute("categories", categories);
            request.setAttribute("pageContent", "/createRequest.jsp");
            
            System.out.println("[DEBUG] Forwarding to layout.jsp with data...");
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
            
            // SubType is optional for Purchase requests (typeId might vary, check by name if needed)
            String subTypeParam = request.getParameter("subTypeId");
            Integer subTypeId = null;
            if (subTypeParam != null && !subTypeParam.trim().isEmpty()) {
                try {
                    int parsedSubTypeId = Integer.parseInt(subTypeParam);
                    // If subTypeId is 0, treat as null (for Purchase requests)
                    subTypeId = (parsedSubTypeId == 0) ? null : parsedSubTypeId;
                } catch (NumberFormatException e) {
                    // SubType not provided or invalid, keep as null
                }
            }
            
            String note = request.getParameter("note");
            
            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            
            // Debug logging
            System.out.println("Debug - materialIds: " + (materialIds != null ? materialIds.length : "null"));
            System.out.println("Debug - quantities: " + (quantities != null ? quantities.length : "null"));
            
            // Debug all parameters
            System.out.println("All parameters:");
            request.getParameterMap().forEach((key, values) -> {
                System.out.println("  " + key + ": " + java.util.Arrays.toString(values));
            });
            
            if (materialIds == null || quantities == null || materialIds.length == 0 
                    || materialIds.length != quantities.length) {
                String errorMsg = "Invalid materials data - ";
                if (materialIds == null) errorMsg += "materialIds is null; ";
                if (quantities == null) errorMsg += "quantities is null; ";
                if (materialIds != null && materialIds.length == 0) errorMsg += "no materials selected; ";
                if (materialIds != null && quantities != null && materialIds.length != quantities.length) {
                    errorMsg += "length mismatch (" + materialIds.length + " vs " + quantities.length + "); ";
                }
                throw new ServletException(errorMsg);
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
            CreateRequestExport_PurcharDAO dao4 = new CreateRequestExport_PurcharDAO();
            int requestId = dao4.createRequest(userId, typeId, subTypeId, note, details);
            
            if (requestId > 0) {
                // Use redirect to avoid POST resubmission and ensure fresh data load
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
