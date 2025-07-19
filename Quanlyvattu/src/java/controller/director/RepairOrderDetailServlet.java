package controller.director;

import DAO.RepairOrderDAO;
import DAO.RequestDAO;
import DAO.RODetailDAO;
import Helper.AuthorizationHelper;
import model.RepairOrderList;
import model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/repair-order-detail")
public class RepairOrderDetailServlet extends HttpServlet {

    private final RepairOrderDAO roDao = new RepairOrderDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final RODetailDAO roDetailDAO = new RODetailDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing RO ID");
            return;
        }

        int roId;
        try {
            roId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid RO ID");
            return;
        }

        RepairOrderList ro = roDao.getRepairOrderById(roId);
        if (ro == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Repair Order not found");
            return;
        }

        ro.setDetails(roDetailDAO.getRepairDetailsByROId(roId));
        String requestNote = requestDAO.getRequestNoteById(ro.getRequestId());

        request.setAttribute("ro", ro);
        request.setAttribute("requestNote", requestNote);
        request.setAttribute("pageContent", "/View/Director/repair-order-detail.jsp");
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
        int roId = Integer.parseInt(request.getParameter("roId"));
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        int approverId = currentUser.getUserId();

        boolean success = false;
        String alertMessage = "";
        StringBuilder debug = new StringBuilder("⚠️ Bắt đầu xử lý " + action.toUpperCase() + "...\n");

        if ("approve".equalsIgnoreCase(action)) {
            success = roDao.updateStatus(roId, "Approved", approverId);
            debug.append("✔️ Cập nhật status: ").append(success ? "OK\n" : "FAIL\n");

            if (success) {
                RepairOrderList ro = roDao.getRepairOrderById(roId);
                ro.setDetails(roDetailDAO.getRepairDetailsByROId(roId));
                ro.setApprovedBy(approverId);
                debug.append("📦 Số vật tư trong RO: ").append(ro.getDetails().size()).append("\n");

                try {
                    boolean exportCreated = roDetailDAO.createExportRequestFromRepairOrder(ro);
                    debug.append("🚀 Tạo Export Request: ").append(exportCreated ? "THÀNH CÔNG\n" : "THẤT BẠI\n");

                    alertMessage = exportCreated
                            ? "✅ Đã duyệt phiếu và tạo yêu cầu xuất kho thành công."
                            : "⚠️ Duyệt thành công nhưng KHÔNG tạo được yêu cầu xuất kho.";
                } catch (Exception e) {
                    alertMessage = "❌ Exception khi tạo yêu cầu xuất kho.";
                    debug.append("❌ Lỗi: ").append(e.getMessage()).append("\n");
                }
            } else {
                alertMessage = "❌ Duyệt phiếu thất bại.";
            }

            request.setAttribute("debugLog", debug.toString());

        } else if ("reject".equalsIgnoreCase(action)) {
            success = roDao.updateStatus(roId, "Rejected", approverId);
            alertMessage = success ? "❌ Phiếu sửa chữa đã bị từ chối." : "⚠️ Không thể từ chối phiếu sửa chữa.";
        }

        // Load lại dữ liệu
        RepairOrderList ro = roDao.getRepairOrderById(roId);
        ro.setDetails(roDetailDAO.getRepairDetailsByROId(roId));
        String requestNote = requestDAO.getRequestNoteById(ro.getRequestId());

        request.setAttribute("ro", ro);
        request.setAttribute("requestNote", requestNote);
        request.setAttribute("alertMessage", alertMessage);
        request.setAttribute("pageContent", "/View/Director/repair-order-detail.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
