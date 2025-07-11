package controller.general;

import DAO.WarehouseReportDAO;
import model.RequestList;
import model.RequestDetailItem;
import model.TaskLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/warehousereport")
public class WarehouseReportServlet extends HttpServlet {
    private WarehouseReportDAO warehouseReportDAO;

    @Override
    public void init() throws ServletException {
        warehouseReportDAO = new WarehouseReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        
        // Get data from DAO
        RequestList requestList = warehouseReportDAO.getRequestById(requestId);
        List<RequestDetailItem> requestDetails = warehouseReportDAO.getRequestDetailsById(requestId);
        Map<Date, List<TaskLog>> taskLogsByDate = warehouseReportDAO.getTaskLogsByDate(requestId);

        // Set attributes for JSP
        request.setAttribute("request", requestList);
        request.setAttribute("requestDetails", requestDetails);
        request.setAttribute("taskLogsByDate", taskLogsByDate);

        // Forward to JSP
         request.setAttribute("pageContent", "/WarehouseStaffReport.jsp");
                request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
