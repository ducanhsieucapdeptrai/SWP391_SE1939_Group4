package controller.general;

import DAO.MaterialDAO;
import DAO.StatisticDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import model.Material;
import model.Statistic;

@WebServlet(name = "StatisticsServlet", urlPatterns = {"/statistics"})
public class StatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materials = materialDAO.getAllMaterials();
        request.setAttribute("materials", materials);
        // Forward to the JSP page that will be included in the layout
        request.setAttribute("pageContent", "/statistics.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StatisticDAO statisticDAO = new StatisticDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String[] materialIdStrings = request.getParameterValues("materialIds");
            String startDateString = request.getParameter("startDate");
            String endDateString = request.getParameter("endDate");

            List<Integer> materialIds = null;
            if (materialIdStrings != null && materialIdStrings.length > 0) {
                materialIds = Arrays.stream(materialIdStrings)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
            }

            // Default to today if dates are not provided or are empty
            Date startDate = (startDateString == null || startDateString.isEmpty()) ? new Date() : sdf.parse(startDateString);
            Date endDate = (endDateString == null || endDateString.isEmpty()) ? new Date() : sdf.parse(endDateString);

            List<Statistic> statistics = statisticDAO.getInventoryStatistics(materialIds, startDate, endDate);

            request.setAttribute("statistics", statistics);
            request.setAttribute("materials", materialDAO.getAllMaterials()); // For repopulating the filter
            request.setAttribute("selectedMaterialIds", materialIds != null ? materialIds : new ArrayList<>());
            request.setAttribute("selectedStartDate", startDateString);
            request.setAttribute("selectedEndDate", endDateString);

        } catch (ParseException | NumberFormatException e) {
            request.setAttribute("error", "Invalid input format. Please check your selections.");
            e.printStackTrace();
        }

        // Forward back to the JSP page, which will be included in the layout
        request.setAttribute("pageContent", "/statistics.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
