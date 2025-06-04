package controller.general;

import jakarta.servlet.http.*;

import DAO.HomepageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import model.Users;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

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

        HomepageDAO homepageDAO = new HomepageDAO();

        try {
            int totalMaterials = homepageDAO.getTotalMaterialCount();
            int totalImports = homepageDAO.getTotalImportCount();
            int totalExports = homepageDAO.getTotalExportCount();
            int pendingRequests = homepageDAO.getPendingRequestCount();

            request.setAttribute("totalMaterials", totalMaterials);
            request.setAttribute("totalImports", totalImports);
            request.setAttribute("totalExports", totalExports);
            request.setAttribute("pendingRequests", pendingRequests);
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            StringBuilder labelsBuilder = new StringBuilder("[");
            for (int i = 0; i < months.length; i++) {
                labelsBuilder.append("\"").append(months[i]).append("\"");
                if (i < months.length - 1) {
                    labelsBuilder.append(",");
                }
            }
            labelsBuilder.append("]");
            String chartLabels = labelsBuilder.toString();

            int[] sampleData = {totalMaterials / 3, totalImports, totalExports, pendingRequests,
                totalMaterials / 2, totalImports / 2, totalExports / 2, pendingRequests / 2,
                totalMaterials / 4, totalImports / 4, totalExports / 4, pendingRequests / 4};

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

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("totalMaterials", 0);
            request.setAttribute("totalImports", 0);
            request.setAttribute("totalExports", 0);
            request.setAttribute("pendingRequests", 0);
            request.setAttribute("todaySales", 0);
            request.setAttribute("totalSales", 0);
            request.setAttribute("todayRevenue", "0");
            request.setAttribute("totalRevenue", "0");

            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            StringBuilder labelsBuilder = new StringBuilder("[");
            for (int i = 0; i < months.length; i++) {
                labelsBuilder.append("\"").append(months[i]).append("\"");
                if (i < months.length - 1) {
                    labelsBuilder.append(",");
                }
            }
            labelsBuilder.append("]");

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
