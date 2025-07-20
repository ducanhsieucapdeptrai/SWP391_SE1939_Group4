package controller.statistic;

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

@WebServlet(name = "InventoryStatisticServlet", urlPatterns = {"/inventory-statistics"})
public class InventoryStatisticServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materials = materialDAO.getAllMaterials();
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("/statistic.jsp").forward(request, response);
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
            if (materialIdStrings != null && materialIdStrings.length > 0 && !Arrays.asList(materialIdStrings).contains("")) {
                materialIds = Arrays.stream(materialIdStrings)
                                    .filter(s -> !s.isEmpty())
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
            }

            Date startDate = (startDateString == null || startDateString.isEmpty()) ? new Date(0) : sdf.parse(startDateString); // Start of epoch if empty
            Date endDate = (endDateString == null || endDateString.isEmpty()) ? new Date() : sdf.parse(endDateString); // Today if empty

            List<Statistic> statistics = statisticDAO.getInventoryStatistics(materialIds, startDate, endDate);

            request.setAttribute("statistics", statistics);
            request.setAttribute("selectedMaterialIds", materialIds != null ? materialIds : new ArrayList<>());
            request.setAttribute("selectedStartDate", startDateString);
            request.setAttribute("selectedEndDate", endDateString);

        } catch (ParseException | NumberFormatException e) {
            request.setAttribute("error", "Invalid input format. Please check your selections.");
            e.printStackTrace();
        }

        // Always forward back to the JSP, providing materials for the filter dropdown
        request.setAttribute("materials", materialDAO.getAllMaterials()); 
        request.getRequestDispatcher("/statistic.jsp").forward(request, response);
    }
}
