package controller.general;

import DAO.WarehouseReportDAO;
import model.*;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/warehousereport")
public class WarehouseReportServlet extends HttpServlet {

    private WarehouseReportDAO dao;
    private static final int RECORDS_PER_PAGE = 5;

    @Override
    public void init() throws ServletException {
        dao = new WarehouseReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Nếu chưa đăng nhập
        if (session == null || session.getAttribute("currentUser") == null) {
            req.setAttribute("errorMsg", "You must be logged in to access this page.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }
        
        // Parse request ID
        int requestId;
        try {
            requestId = Integer.parseInt(req.getParameter("requestId"));
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Invalid or missing request ID.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        // Parse current page
        int currentPage = 1;
        try {
            String pageStr = req.getParameter("page");
            if (pageStr != null) {
                currentPage = Integer.parseInt(pageStr);
            }
        } catch (Exception ignored) {
        }

        // Fetch main request info
        RequestList requestInfo = dao.getRequestById(requestId);
        if (requestInfo == null) {
            req.setAttribute("errorMessage", "Request not found.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        List<RequestDetail> requestDetails = dao.getRequestDetailsByRequestId(requestId);
        req.setAttribute("requestInfo", requestInfo);
        req.setAttribute("requestDetails", requestDetails);

        // Determine type and fetch actual records
        String type = requestInfo.getRequestTypeName();
        boolean isImport = type.equalsIgnoreCase("Import") || type.equalsIgnoreCase("New purchase");
        boolean isExport = type.equalsIgnoreCase("Export") || type.equalsIgnoreCase("Export Materials");

        boolean hasRecords = false;
        int totalRecords = 0;

        if (isImport) {
            List<ImportList> imports = dao.getRelatedImportsByRequestId(requestId);
            List<ImportDetail> importDetails = dao.getRelatedImportDetailsByRequestId(requestId);
            importDetails.removeIf(d -> d.getQuantity() <= 0);
            totalRecords = importDetails.size();
            hasRecords = totalRecords > 0;

            int start = (currentPage - 1) * RECORDS_PER_PAGE;
            int end = Math.min(start + RECORDS_PER_PAGE, totalRecords);

            req.setAttribute("actualLists", imports.subList(start, end));
            req.setAttribute("actualDetails", importDetails.subList(start, end));

        } else if (isExport) {
            List<ExportList> exports = dao.getRelatedExportsByRequestId(requestId);
            List<ExportDetail> exportDetails = dao.getRelatedExportDetailsByRequestId(requestId);
            exportDetails.removeIf(d -> d.getQuantity() <= 0);
            totalRecords = exportDetails.size();
            hasRecords = totalRecords > 0;

            int start = (currentPage - 1) * RECORDS_PER_PAGE;
            int end = Math.min(start + RECORDS_PER_PAGE, totalRecords);

            req.setAttribute("actualLists", exports.subList(start, end));
            req.setAttribute("actualDetails", exportDetails.subList(start, end));
        }

        // Pagination setup
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        req.setAttribute("hasRecords", hasRecords);
        req.setAttribute("totalRecords", totalRecords);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("startPage", startPage);
        req.setAttribute("endPage", endPage);
        req.setAttribute("startIndex", (currentPage - 1) * RECORDS_PER_PAGE);
        req.setAttribute("endIndex", Math.min(currentPage * RECORDS_PER_PAGE, totalRecords));

        // Forward to JSP
            req.setAttribute("pageContent", "/WarehouseStaffReport.jsp");

            req.getRequestDispatcher("/layout/layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


}
