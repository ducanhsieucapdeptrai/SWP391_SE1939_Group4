package controller.accountant;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.PurchaseOrderDetail;
import model.RequestList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-purchase-order")
public class CreatePurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!AuthorizationHelper.hasAnyRole(request, "Accountant", "Warehouse Staff", "Company Staff")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String requestIdRaw = request.getParameter("requestId");
        if (requestIdRaw == null || requestIdRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing requestId");
            return;
        }

        int requestId = Integer.parseInt(requestIdRaw);

        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        RequestDAO reqDAO = new RequestDAO();

        List<PurchaseOrderDetail> detailList = poDAO.getPurchasePreviewByRequest(requestId);
        RequestList requestInfo = reqDAO.getRequestById(requestId);
        String senderName = requestInfo != null ? requestInfo.getRequestedByName() : "Unknown";

        request.setAttribute("detailList", detailList);
        request.setAttribute("requestId", requestId);
        request.setAttribute("senderName", senderName);
        request.setAttribute("pageContent", "/View/CompanyStaff/purchase-order-form.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!AuthorizationHelper.hasAnyRole(request, "Accountant")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String requestIdRaw = request.getParameter("requestId");

        if (requestIdRaw == null || requestIdRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing requestId");
            return;
        }

        int requestId = Integer.parseInt(requestIdRaw);

        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        RequestDAO reqDAO = new RequestDAO();

        if (poDAO.existsByRequestId(requestId)) {
            request.setAttribute("error", "A Purchase Order has already been created for this request.");
        } else {
            List<PurchaseOrderDetail> detailList = poDAO.getPurchasePreviewByRequest(requestId);
            List<PurchaseOrderDetail> filledDetails = new ArrayList<>();
            double total = 0;

            for (PurchaseOrderDetail item : detailList) {
                String unitPriceStr = request.getParameter("unitPrice_" + item.getMaterialId());
                double unitPrice = unitPriceStr != null ? Double.parseDouble(unitPriceStr) : 0;
                item.setUnitPrice(unitPrice);
                item.setTotal(unitPrice * item.getQuantity());
                total += item.getTotal();
                filledDetails.add(item);
            }

            String note = reqDAO.getRequestNoteById(requestId);
            int poId = poDAO.insertPurchaseOrderWithStatus(requestId, userId, total, note, "Pending");

            for (PurchaseOrderDetail d : filledDetails) {
                poDAO.insertPODetail(poId, d);
            }

            request.setAttribute("poCreated", true);
        }

        // Reload lại dữ liệu để hiển thị sau khi submit
        List<PurchaseOrderDetail> detailList = poDAO.getPurchasePreviewByRequest(requestId);
        RequestList requestInfo = reqDAO.getRequestById(requestId);
        String senderName = requestInfo != null ? requestInfo.getRequestedByName() : "Unknown";

        request.setAttribute("detailList", detailList);
        request.setAttribute("requestId", requestId);
        request.setAttribute("senderName", senderName);
        request.setAttribute("pageContent", "/View/CompanyStaff/purchase-order-form.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
