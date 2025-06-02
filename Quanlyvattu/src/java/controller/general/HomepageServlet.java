package controller.general;

import DAO.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;
import java.text.DecimalFormat;

@WebServlet(name = "HomepageServlet", urlPatterns = {"/dashboard"})
public class HomepageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        // Load dashboard statistics data
        DashboardDAO dashboardDAO = new DashboardDAO();
        
        try {
            // Get statistics
            int totalMaterials = dashboardDAO.getTotalMaterialCount();
            int totalImports = dashboardDAO.getTotalImportCount();
            int totalExports = dashboardDAO.getTotalExportCount();
            int pendingRequests = dashboardDAO.getPendingRequestCount();
            
            // Get sales and revenue data
            int todaySales = dashboardDAO.getTodaySalesCount();
            int totalSales = dashboardDAO.getTotalSalesCount();
            double todayRevenue = dashboardDAO.getTodayRevenue();
            double totalRevenue = dashboardDAO.getTotalRevenue();
            
            // Format currency values
            DecimalFormat currencyFormat = new DecimalFormat("#,###");
            String formattedTodayRevenue = currencyFormat.format(todayRevenue);
            String formattedTotalRevenue = currencyFormat.format(totalRevenue);
            
            // Set attributes for the JSP
            request.setAttribute("totalMaterials", totalMaterials);
            request.setAttribute("totalImports", totalImports);
            request.setAttribute("totalExports", totalExports);
            request.setAttribute("pendingRequests", pendingRequests);
            request.setAttribute("todaySales", todaySales);
            request.setAttribute("totalSales", totalSales);
            request.setAttribute("todayRevenue", formattedTodayRevenue);
            request.setAttribute("totalRevenue", formattedTotalRevenue);
            
            // Prepare chart data
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            
            // Format the months array into proper JSON format for chart labels
            StringBuilder labelsBuilder = new StringBuilder("[");
            for (int i = 0; i < months.length; i++) {
                labelsBuilder.append("\"").append(months[i]).append("\"");
                if (i < months.length - 1) {
                    labelsBuilder.append(",");
                }
            }
            labelsBuilder.append("]");
            String chartLabels = labelsBuilder.toString(); // Format: ["Jan","Feb",...]
            
            // Sample data for chart - replace with actual data in production
            int[] sampleData = {totalMaterials/3, totalImports, totalExports, pendingRequests, totalMaterials/2, totalImports/2, 
                               totalExports/2, pendingRequests/2, totalMaterials/4, totalImports/4, totalExports/4, pendingRequests/4};
            
            StringBuilder chartDataBuilder = new StringBuilder("[");
            for (int i = 0; i < sampleData.length; i++) {
                chartDataBuilder.append(sampleData[i]);
                if (i < sampleData.length - 1) {
                    chartDataBuilder.append(",");
                }
            }
            chartDataBuilder.append("]");
            
            request.setAttribute("chartLabels", chartLabels);
            request.setAttribute("chartData", chartDataBuilder.toString());
            
            // Log for debugging
            System.out.println("Dashboard statistics loaded successfully");
            System.out.println("Total Materials: " + totalMaterials);
            System.out.println("Total Imports: " + totalImports);
            System.out.println("Total Exports: " + totalExports);
            System.out.println("Pending Requests: " + pendingRequests);
        } catch (Exception e) {
            System.out.println("Error loading dashboard statistics: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values in case of error
            request.setAttribute("totalMaterials", 0);
            request.setAttribute("totalImports", 0);
            request.setAttribute("totalExports", 0);
            request.setAttribute("pendingRequests", 0);
            request.setAttribute("todaySales", 0);
            request.setAttribute("totalSales", 0);
            request.setAttribute("todayRevenue", "0");
            request.setAttribute("totalRevenue", "0");
            
            // Set default chart data
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            
            // Format the months array into proper JSON format for chart labels
            StringBuilder labelsBuilder = new StringBuilder("[");
            for (int i = 0; i < months.length; i++) {
                labelsBuilder.append("\"").append(months[i]).append("\"");
                if (i < months.length - 1) {
                    labelsBuilder.append(",");
                }
            }
            labelsBuilder.append("]");
            
            // Default sample data (all zeros)
            StringBuilder chartDataBuilder = new StringBuilder("[");
            for (int i = 0; i < months.length; i++) {
                chartDataBuilder.append("0");
                if (i < months.length - 1) {
                    chartDataBuilder.append(",");
                }
            }
            chartDataBuilder.append("]");
            
            request.setAttribute("chartLabels", labelsBuilder.toString());
            request.setAttribute("chartData", chartDataBuilder.toString());
        }

        request.setAttribute("pageContent", "/homepage.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles homepage/dashboard display after login.";
    }
}
