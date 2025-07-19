package controller.staff;

import DAO.RepairOrderDAO;
import DAO.RequestDetailDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RepairOrderDetail;
import model.RequestList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-repair-order")
public class CreateRepairOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder debugLog = new StringBuilder();

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
        RepairOrderDAO roDAO = new RepairOrderDAO();
        RequestDetailDAO reqDAO = new RequestDetailDAO();

        RequestList requestInfo = reqDAO.getRequestById(requestId);
        String senderName = requestInfo != null ? requestInfo.getRequestedByName() : "Unknown";

        if ("true".equalsIgnoreCase(submitFlag)) {
            if (roDAO.existsByRequestId(requestId)) {
                response.sendRedirect("my-request?roAlready=true");
                return;
            }

            List<RepairOrderDetail> detailList = new ArrayList<>();
            int index = 0;

            while (true) {
                String materialIdRaw = request.getParameter("materialId_" + index);
                String quantityRaw = request.getParameter("quantity_" + index);
                String priceRaw = request.getParameter("estimatedPrice_" + index);
                String note = request.getParameter("note_" + index);

                if (materialIdRaw == null || quantityRaw == null || priceRaw == null) {
                    break;
                }

                try {
                    int materialId = Integer.parseInt(materialIdRaw);
                    int quantity = Integer.parseInt(quantityRaw);
                    double price = Double.parseDouble(priceRaw);

                    RepairOrderDetail detail = new RepairOrderDetail();
                    detail.setMaterialId(materialId);
                    detail.setQuantity(quantity);
                    detail.setUnitPrice(price);
                    detail.setMnote(note);

                    String line = String.format("Row %d: materialId=%d, quantity=%d, price=%.2f, note=%s",
                            index, materialId, quantity, price, note);
                    System.out.println(line);
                    debugLog.append(line).append("<br/>");

                    detailList.add(detail);
                } catch (NumberFormatException e) {
                    String err = "⚠ Error parsing number at row " + index + ": " + e.getMessage();
                    request.setAttribute("error", "Invalid input format.");
                    debugLog.append(err).append("<br/>");
                    System.out.println(err);
                    break;
                }

                index++;
            }

            boolean success = roDAO.createRepairOrderFromRequest(requestInfo, detailList, debugLog);
            if (success) {
                response.sendRedirect("my-request?roCreated=true");
                return;
            } else {
                request.setAttribute("error", "Failed to create Repair Order.");
                debugLog.append("❌ Failed to create Repair Order.<br/>");
                request.setAttribute("debugLogs", debugLog.toString()); // 🧠 ensure logs show up in JSP
            }
        }

        // Load lại dữ liệu nếu chưa submit hoặc bị lỗi
        List<RepairOrderDetail> repairPreviewList = roDAO.getRepairPreviewByRequest(requestId);
        request.setAttribute("detailList", repairPreviewList);
        request.setAttribute("requestId", requestId);
        request.setAttribute("senderName", senderName);

        if (request.getAttribute("debugLogs") == null) {
            request.setAttribute("debugLogs", debugLog.toString()); // fallback nếu chưa set
        }

        request.setAttribute("pageContent", "/View/CompanyStaff/repair-order-form.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
