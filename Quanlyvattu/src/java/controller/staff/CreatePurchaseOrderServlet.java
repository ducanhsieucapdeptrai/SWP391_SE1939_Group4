package controller.staff;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.PurchaseOrderDetail;
import model.RequestList;

import java.io.IOException;
import java.util.List;

public class CreatePurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!AuthorizationHelper.hasAnyRole(request, "Company Staff", "Warehouse Manager", "Warehouse Staff")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String submitFlag = request.getParameter("submitFlag");
        String requestIdRaw = request.getParameter("requestId");

        if (requestIdRaw == null || requestIdRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing requestId");
            return;
        }

        int requestId = Integer.parseInt(requestIdRaw);

        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        RequestDAO reqDAO = new RequestDAO();

        List<PurchaseOrderDetail> detailList = poDAO.getPurchasePreviewByRequest(requestId);
        double total = detailList.stream().mapToDouble(PurchaseOrderDetail::getTotal).sum();
        RequestList requestInfo = reqDAO.getRequestById(requestId);
        String senderName = requestInfo != null ? requestInfo.getRequestedByName() : "Unknown";

        if ("true".equalsIgnoreCase(submitFlag)) {
            if (poDAO.existsByRequestId(requestId)) {
                request.setAttribute("error", "A Purchase Order has already been created for this request.");
            } else {
                String note = reqDAO.getRequestNoteById(requestId);
                int poId = poDAO.insertPurchaseOrderWithStatus(requestId, userId, total, note, "Pending");
                for (PurchaseOrderDetail d : detailList) {
                    poDAO.insertPODetail(poId, d);
                }
                request.setAttribute("poCreated", true);
            }
        }

        request.setAttribute("detailList", detailList);
        request.setAttribute("total", total);
        request.setAttribute("requestId", requestId);
        request.setAttribute("senderName", senderName);
        request.setAttribute("pageContent", "/View/CompanyStaff/purchase-order-form.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
