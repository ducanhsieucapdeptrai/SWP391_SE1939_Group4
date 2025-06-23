package controller.general;

import dal.DBContext;
import DAO.RequestDAO;
import model.RequestDetailItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RequestUpdateServlet")
public class RequestUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            RequestDAO dao = new RequestDAO();

            List<RequestDetailItem> items = dao.getRequestDetails(requestId);
            boolean isUpdated = dao.isRequestUpdated(requestId);

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestItems", items);
            request.setAttribute("isAlreadyUpdated", isUpdated);
            request.setAttribute("editMode", false);
            request.setAttribute("canConfirm", canBeConfirmed(items));

            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reqlist");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode");
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        int totalItems = Integer.parseInt(request.getParameter("totalItems"));

        RequestDAO dao = new RequestDAO();

        try {
            List<RequestDetailItem> items = new ArrayList<>();
            for (int i = 0; i < totalItems; i++) {
                int materialId = Integer.parseInt(request.getParameter("materialId_" + i));
                String requestType = request.getParameter("requestType_" + i);
                int requestedQty = Integer.parseInt(request.getParameter("requestedQuantity_" + i));

                RequestDetailItem item = new RequestDetailItem();
                item.setMaterialId(materialId);
                item.setRequestTypeName(requestType);
                item.setQuantity(requestedQty);

                // Only read actual quantity in save/confirm modes
                if (!"edit".equalsIgnoreCase(mode)) {
                    String actualQtyStr = request.getParameter("actualQuantity_" + i);
                    int actualQty = (actualQtyStr != null && !actualQtyStr.isEmpty()) ? Integer.parseInt(actualQtyStr) : 0;
                    item.setActualQuantity(actualQty);
                }

                items.add(item);
            }

            boolean isSuccess = false;
            boolean editMode = false;
            String message = "";
            String messageType = "";

            if ("edit".equalsIgnoreCase(mode)) {
                editMode = true;

            } else if ("save".equalsIgnoreCase(mode)) {
                try (Connection conn = new DBContext().getConnection()) {
                    isSuccess = dao.saveActualQuantities(requestId, items, conn);
                }
                editMode = true;
                message = isSuccess ? "Saved successfully." : "Failed to save actual quantities.";
                messageType = isSuccess ? "success" : "error";

            } else if ("confirm".equalsIgnoreCase(mode)) {
                try (Connection conn = new DBContext().getConnection()) {
                    String requestType = items.get(0).getRequestTypeName();
                    isSuccess = dao.updateActualQuantities(conn, requestId, items, requestType);
                }
                message = isSuccess ? "Request confirmed and stock updated!" : "Request update failed! Stock check or condition failed.";
                messageType = isSuccess ? "success" : "error";
            }

            // Reload data after action
            List<RequestDetailItem> updatedItems = dao.getRequestDetails(requestId);
            boolean isUpdated = dao.isRequestUpdated(requestId);
            boolean canConfirm = canBeConfirmed(updatedItems);

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestItems", updatedItems);
            request.setAttribute("isAlreadyUpdated", isUpdated);
            request.setAttribute("editMode", editMode);
            request.setAttribute("canConfirm", canConfirm);
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "System error during request update.");
            request.setAttribute("messageType", "error");
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    private boolean canBeConfirmed(List<RequestDetailItem> items) {
        if (items == null || items.isEmpty()) return false;

        for (RequestDetailItem item : items) {
            int actual = item.getActualQuantity();
            int requested = item.getQuantity();
            String type = item.getRequestTypeName();

            if ("Import".equalsIgnoreCase(type) || "Purchase".equalsIgnoreCase(type)) {
                if (actual < requested) return false;
            } else if ("Export".equalsIgnoreCase(type) || "Repair".equalsIgnoreCase(type)) {
                if (actual != requested) return false;
            } else {
                return false;
            }
        }
        return true;
    }
}
