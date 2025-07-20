package controller.statistic;

import DAO.MaterialDAO;
import DAO.StatisticDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Material;
import model.Statistic;
import model.MaterialStatistic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import util.CsvExportUtil;

@WebServlet(name = "MaterialStatisticServlet", urlPatterns = {"/material-statistics"})
public class MaterialStatisticServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> materials = materialDAO.getAllMaterials();
            request.setAttribute("materials", materials);
            request.setAttribute("pageContent", "/material-statistics.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading statistics page: " + e.getMessage());
            request.setAttribute("pageContent", "/material-statistics.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Handle export action separately to avoid conflicts
        if ("export".equals(action)) {
            handleExportAction(request, response);
            return;
        }

        // Handle normal page display
        handlePageDisplay(request, response);
    }

    private void handleExportAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StatisticDAO statisticDAO = new StatisticDAO();

        try {
            String reportType = request.getParameter("reportType");
            String[] materialIdStrings = request.getParameterValues("materialIds");
            String startDateString = request.getParameter("startDate");
            String endDateString = request.getParameter("endDate");

            List<Integer> materialIds = parseMaterialIds(materialIdStrings);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = startDateString != null && !startDateString.isEmpty() ? sdf.parse(startDateString) : new Date(0);
            Date endDate = endDateString != null && !endDateString.isEmpty() ? sdf.parse(endDateString) : new Date();

            System.out.println("DEBUG: Export action triggered for reportType: " + reportType);

            // Fetch data based on report type
            List<Statistic> inventoryData = null;
            List<MaterialStatistic> importData = null;
            List<MaterialStatistic> exportData = null;

            if ("inventory".equals(reportType)) {
                inventoryData = statisticDAO.getInventoryStatistics(materialIds, startDate, endDate);
                System.out.println("DEBUG: Inventory data size: " + (inventoryData != null ? inventoryData.size() : "null"));
            } else if ("import".equals(reportType)) {
                importData = statisticDAO.getImportStatistics(materialIds, startDate, endDate);
                System.out.println("DEBUG: Import data size: " + (importData != null ? importData.size() : "null"));
            } else if ("export".equals(reportType)) {
                exportData = statisticDAO.getExportStatistics(materialIds, startDate, endDate);
                System.out.println("DEBUG: Export data size: " + (exportData != null ? exportData.size() : "null"));
            }

            // Check if we have data to export
            boolean hasData = false;
            if ("inventory".equals(reportType) && inventoryData != null && !inventoryData.isEmpty()) {
                hasData = true;
            } else if ("import".equals(reportType) && importData != null && !importData.isEmpty()) {
                hasData = true;
            } else if ("export".equals(reportType) && exportData != null && !exportData.isEmpty()) {
                hasData = true;
            }

            if (!hasData) {
                // Send error response
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("<script>alert('No data available to export CSV file for the selected filter.'); window.history.back();</script>");
                response.getWriter().flush();
                return;
            }

            // Set response headers for CSV download
            response.setContentType("text/csv; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            String fileName;
            if ("inventory".equals(reportType)) {
                fileName = "BaoCaoTonKho.csv";
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                System.out.println("DEBUG: Exporting inventory data to CSV");
                CsvExportUtil.exportInventoryToCsv(inventoryData, response.getWriter());
            } else if ("import".equals(reportType)) {
                fileName = "BaoCaoNhapKho.csv";
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                System.out.println("DEBUG: Exporting import data to CSV");
                CsvExportUtil.exportMaterialStatisticsToCsv(reportType, importData, response.getWriter());
            } else { // export report type
                fileName = "BaoCaoXuatKho.csv";
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                System.out.println("DEBUG: Exporting export data to CSV");
                CsvExportUtil.exportMaterialStatisticsToCsv(reportType, exportData, response.getWriter());
            }

            response.getWriter().flush();
            System.out.println("DEBUG: CSV export completed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Send error response
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("<script>alert('Error exporting CSV: " + e.getMessage().replace("'", "\\'") + "'); window.history.back();</script>");
            response.getWriter().flush();
        }
    }

    private void handlePageDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StatisticDAO statisticDAO = new StatisticDAO();

        try {
            String reportType = request.getParameter("reportType");
            String[] materialIdStrings = request.getParameterValues("materialIds");
            String startDateString = request.getParameter("startDate");
            String endDateString = request.getParameter("endDate");

            List<Integer> materialIds = parseMaterialIds(materialIdStrings);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = startDateString != null && !startDateString.isEmpty() ? sdf.parse(startDateString) : new Date(0);
            Date endDate = endDateString != null && !endDateString.isEmpty() ? sdf.parse(endDateString) : new Date();

            // Fetch data based on report type
            if ("inventory".equals(reportType)) {
                List<Statistic> statistics = statisticDAO.getInventoryStatistics(materialIds, startDate, endDate);
                request.setAttribute("statistics", statistics);
            } else if ("import".equals(reportType)) {
                List<MaterialStatistic> importReport = statisticDAO.getImportStatistics(materialIds, startDate, endDate);
                request.setAttribute("importReport", importReport);
            } else if ("export".equals(reportType)) {
                List<MaterialStatistic> exportReport = statisticDAO.getExportStatistics(materialIds, startDate, endDate);
                request.setAttribute("exportReport", exportReport);
            }

            request.setAttribute("selectedReportType", reportType);
            request.setAttribute("selectedMaterialIds", materialIds);
            request.setAttribute("selectedStartDate", startDateString);
            request.setAttribute("selectedEndDate", endDateString);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error generating report: " + e.getMessage());
        }

        // Always load materials list and forward to page
        try {
            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> materials = materialDAO.getAllMaterials();
            request.setAttribute("materials", materials);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading materials list: " + e.getMessage());
        }

        request.setAttribute("pageContent", "/material-statistics.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    private List<Integer> parseMaterialIds(String[] materialIdStrings) {
        List<Integer> materialIds = null;
        if (materialIdStrings != null && materialIdStrings.length > 0) {
            if (materialIdStrings.length == 1 && materialIdStrings[0].isEmpty()) {
                materialIds = new ArrayList<>();
            } else {
                materialIds = Arrays.stream(materialIdStrings)
                                    .filter(s -> s != null && !s.isEmpty())
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
            }
        }
        return materialIds;
    }
}
