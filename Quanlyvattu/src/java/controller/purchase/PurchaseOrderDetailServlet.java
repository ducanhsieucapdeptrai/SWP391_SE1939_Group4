package controller.purchase;

import DAO.PurchaseOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.PurchaseOrderList;

public class PurchaseOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String poIdParam = request.getParameter("poId"); // ✅ đổi từ id → poId
        if (poIdParam == null) {
            response.sendRedirect("pending-po");
            return;
        }

        try {
            int poId = Integer.parseInt(poIdParam);
            PurchaseOrderList po = PurchaseOrderDAO.getPurchaseOrderById(poId);
            request.setAttribute("po", po);
            if (po == null) {
                response.sendRedirect("pending-po");
                return;
            }

            request.setAttribute("po", po); // ✅ đúng key phải là "po" để view dùng ${po.xxx}
            request.getRequestDispatcher("/View/Director/PurchaseOrderDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("pending-po");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action");

            if ("submit".equals(action)) {
                // Xử lý tạo PO
                String note = request.getParameter("note");
                String[] materialIds = request.getParameterValues("materialIds");
                String[] quantities = request.getParameterValues("quantities");

                PurchaseOrderDAO.createPurchaseOrder(userId, requestId, note, materialIds, quantities);
                session.setAttribute("successMessage", "Purchase Order created successfully!");

            } else if ("cancel".equals(action)) {
                String cancelReason = request.getParameter("cancelReason");
                int poId = PurchaseOrderDAO.getPOIdByRequestId(requestId);

                if (poId > 0) {
                    // Sử dụng String datetime thay vì Timestamp
                    String currentDateTime = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    PurchaseOrderDAO.rejectPurchaseOrder(
                            poId,
                            userId,
                            "Cancelled by requester: " + cancelReason,
                            currentDateTime // Truyền String datetime
                    );
                }
                session.setAttribute("successMessage", "PO cancelled successfully");
            }

            response.sendRedirect(request.getContextPath() + "/create-po?requestId=" + requestId);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to process purchase order");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
