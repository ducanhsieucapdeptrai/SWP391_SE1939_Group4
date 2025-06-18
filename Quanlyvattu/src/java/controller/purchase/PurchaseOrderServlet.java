package controller.purchase;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import model.RequestDetail;
import model.RequestList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class PurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");

        if (requestIdParam == null) {
            response.sendRedirect("employee-request-list");
            return;
        }

        int requestId = Integer.parseInt(requestIdParam);
        RequestList requestInfo = RequestDAO.getRequestById(requestId);
        List<RequestDetail> requestDetails = RequestDAO.getRequestDetails(requestId);

        if (requestInfo == null || requestDetails == null) {
            response.sendRedirect("employee-request-list");
            return;
        }

        request.setAttribute("requestInfo", requestInfo);
        request.setAttribute("requestDetails", requestDetails);
        request.getRequestDispatcher("/View/CompanyStaff/purchase-order-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        HttpSession session = request.getSession();
        int creatorId = (int) session.getAttribute("userId");
        String note = request.getParameter("note");

        String[] materialIds = request.getParameterValues("materialId");
        String[] quantities = request.getParameterValues("quantity");

        PurchaseOrderDAO.createPurchaseOrder(creatorId, requestId, note, materialIds, quantities);

        response.sendRedirect("employee-request-list");
    }
}
