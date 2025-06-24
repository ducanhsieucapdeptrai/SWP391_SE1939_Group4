package controller.staff;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.PurchaseOrderDetail;

import java.io.IOException;
import java.util.List;

public class CreatePurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String submitFlag = request.getParameter("submit");
        String requestIdRaw = request.getParameter("requestId");

        if (requestIdRaw == null || requestIdRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing requestId");
            return;
        }

        int requestId = Integer.parseInt(requestIdRaw);
        System.out.println("[DEBUG] Submit flag: " + submitFlag + " | requestId: " + requestId);

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        RequestDAO rdao = new RequestDAO();

        if ("true".equals(submitFlag)) {
            if (dao.existsByRequestId(requestId)) {
                request.setAttribute("error", "A Purchase Order has already been created for this request.");
                request.setAttribute("pageContent", "/View/CompanyStaff/my-request.jsp");
                request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
                return;
            }

            List<PurchaseOrderDetail> detailList = dao.getPurchasePreviewByRequest(requestId);
            double total = detailList.stream().mapToDouble(PurchaseOrderDetail::getTotal).sum();
            String note = rdao.getRequestNoteById(requestId);

            int poId = dao.insertPurchaseOrderWithStatus(requestId, userId, total, note, "Pending");
            for (PurchaseOrderDetail d : detailList) {
                dao.insertPODetail(poId, d);
            }

            response.sendRedirect("my-request?message=po_created");
        } else {
            List<PurchaseOrderDetail> detailList = dao.getPurchasePreviewByRequest(requestId);
            double total = detailList.stream().mapToDouble(PurchaseOrderDetail::getTotal).sum();

            request.setAttribute("detailList", detailList);
            request.setAttribute("total", total);
            request.setAttribute("requestId", requestId);
            request.setAttribute("pageContent", "/View/CompanyStaff/purchase-order-form.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }
}
