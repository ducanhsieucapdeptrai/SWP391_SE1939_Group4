package controller.director;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.PurchaseOrderDetail;
import model.PurchaseOrderList;

@WebServlet("/purchase-order-detail")
public class PurchaseOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing PO ID");
            return;
        }

        int poId;
        try {
            poId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid PO ID");
            return;
        }

        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        RequestDAO reqDAO = new RequestDAO();

        PurchaseOrderList po = poDAO.getPurchaseOrderById(poId);
        if (po == null) {
            System.out.println("[DEBUG] Purchase Order #" + poId + " not found.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Purchase Order not found");
            return;
        }

        System.out.println("[DEBUG] Loaded PO: " + po.getPoId() + ", requestId = " + po.getRequestId());

        List<PurchaseOrderDetail> details = poDAO.getPODetails(poId);
        po.setDetails(details);

        System.out.println("[DEBUG] Found " + (details != null ? details.size() : 0) + " material(s) for PO #" + poId);
        for (PurchaseOrderDetail d : details) {
            System.out.println("   - " + d.getMaterialName() + " | Qty: " + d.getQuantity() + " | Total: " + d.getTotal());
        }

        String requestNote = reqDAO.getRequestNoteById(po.getRequestId());
        System.out.println("[DEBUG] Request note: " + requestNote);

        request.setAttribute("po", po);
        request.setAttribute("requestNote", requestNote);
        request.setAttribute("pageContent", "/View/Director/purchase-order-detail.jsp");

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int poId = Integer.parseInt(request.getParameter("poId"));
        HttpSession session = request.getSession();
        int directorId = (int) session.getAttribute("userId");

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        boolean success = false;

        if ("approve".equalsIgnoreCase(action)) {
            success = dao.updateStatus(poId, "Approved", directorId);
            request.setAttribute("alertMessage", "Approved successfully!");
        } else if ("reject".equalsIgnoreCase(action)) {
            success = dao.updateStatus(poId, "Rejected", directorId);
            request.setAttribute("alertMessage", "Rejected successfully!");
        }

        if (!success) {
            request.setAttribute("alertMessage", "Failed to update status.");
        }

        PurchaseOrderList po = dao.getPurchaseOrderById(poId);
        List<PurchaseOrderDetail> details = dao.getPODetails(poId);
        po.setDetails(details);

        RequestDAO reqDAO = new RequestDAO();
        String requestNote = reqDAO.getRequestNoteById(po.getRequestId());

        request.setAttribute("po", po);
        request.setAttribute("details", details);
        request.setAttribute("requestNote", requestNote);
        request.setAttribute("pageContent", "/View/Director/purchase-order-detail.jsp");

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
