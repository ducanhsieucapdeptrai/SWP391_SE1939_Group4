package controller.director;

import DAO.PurchaseOrderDAO;
import DAO.RequestDAO;
import Helper.AuthorizationHelper;
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

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager", "Company Staff")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

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
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Purchase Order not found");
            return;
        }

        List<PurchaseOrderDetail> details = poDAO.getPODetails(poId);
        po.setDetails(details);

        String requestNote = reqDAO.getRequestNoteById(po.getRequestId());

        request.setAttribute("po", po);
        request.setAttribute("requestNote", requestNote);
        request.setAttribute("pageContent", "/View/Director/purchase-order-detail.jsp");

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String action = request.getParameter("action");
        String poIdStr = request.getParameter("poId");
        int poId;

        try {
            poId = Integer.parseInt(poIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Purchase Order ID.");
            return;
        }

        HttpSession session = request.getSession();
        int directorId = ((model.Users) session.getAttribute("currentUser")).getUserId();

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
