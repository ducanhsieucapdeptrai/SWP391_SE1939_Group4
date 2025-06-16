package controller.purchase;

import DAO.MaterialDAO;
import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.Material;
import model.RequestDetail;
import model.RequestList;

import java.io.IOException;
import java.util.List;

public class PurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));

            // Fetch request header info
            RequestList requestInfo = RequestDAO.getRequestById(requestId);
            List<RequestDetail> requestDetails = RequestDAO.getRequestDetails(requestId);

            // Attach material information to each line
            MaterialDAO materialDAO = new MaterialDAO();
            for (RequestDetail detail : requestDetails) {
                Material material = materialDAO.getMaterialById(detail.getMaterialId());
                detail.setMaterial(material);
            }

            // Kiểm tra nếu có thông báo thành công
            String success = request.getParameter("success");
            if ("po_created".equals(success)) {
                request.setAttribute("success", "po_created");
            }

            request.setAttribute("requestInfo", requestInfo);
            request.setAttribute("details", requestDetails);
            request.setAttribute("pageContent", "/View/CompanyStaff/purchase-order-form.jsp");

            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading purchase order form.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer employeeId = (Integer) request.getSession().getAttribute("userId");
        if (employeeId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String note = request.getParameter("note");
            String[] materialIds = request.getParameterValues("materialIds");
            String[] quantities = request.getParameterValues("quantities");

            // Create PO
            PurchaseOrderDAO.createPurchaseOrder(employeeId, requestId, note, materialIds, quantities);

            
            response.sendRedirect(request.getContextPath() + "/create-po?requestId=" + requestId + "&success=po_created");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to create purchase order.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

}
