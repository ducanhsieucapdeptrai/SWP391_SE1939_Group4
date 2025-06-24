package controller.staff;

import DAO.AssignTaskDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import model.RequestList;

import java.io.IOException;
import java.util.List;

@WebServlet("/tasklist")
public class TaskListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("requestType");
        String requestedBy = request.getParameter("requestedBy");
        String requestDate = request.getParameter("requestDate");
        String view = request.getParameter("view"); // "ongoing" hoặc "upcoming" (mặc định là "ongoing")

        if (view == null || view.isEmpty()) {
            view = "ongoing";
        }

        AssignTaskDAO dao = new AssignTaskDAO();
        List<RequestList> list;

        // Gọi DAO phù hợp với bộ lọc
        if ("upcoming".equals(view)) {
            list = dao.getUpcomingTasksFiltered(type, requestedBy, requestDate);
        } else {
            list = dao.getOngoingTasksTodayFiltered(type, requestedBy, requestDate);
        }

        // Danh sách loại yêu cầu
        List<String> requestTypes = dao.getAllRequestTypes();
        List<String> requesterNames = dao.getAllRequesterNames();
        request.setAttribute("requesterNames", requesterNames);

        // Gửi dữ liệu cho JSP
        request.setAttribute("approvedRequestList", list);
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("filterType", type);
        request.setAttribute("filterRequestedBy", requestedBy);
        request.setAttribute("filterRequestDate", requestDate);
        request.setAttribute("activeView", view);

        request.setAttribute("pageContent", "/tasklist.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("transfer".equals(action)) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            try {
                org.json.JSONObject json = new org.json.JSONObject(sb.toString());

                // Hai mảng: transferIds (checkbox IsTransfer) và revertIds (checkbox Revert)
                org.json.JSONArray transferIds = json.optJSONArray("transferIds");
                org.json.JSONArray revertIds = json.optJSONArray("revertIds");

                AssignTaskDAO dao = new AssignTaskDAO();

                // Set IsTransferredToday = true cho transferIds
                if (transferIds != null) {
                    for (int i = 0; i < transferIds.length(); i++) {
                        int requestId = transferIds.getInt(i);
                        dao.updateIsTransferredToday(requestId, true);
                    }
                }

                // Set IsTransferredToday = false cho revertIds
                if (revertIds != null) {
                    for (int i = 0; i < revertIds.length(); i++) {
                        int requestId = revertIds.getInt(i);
                        dao.updateIsTransferredToday(requestId, false);
                    }
                }

                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true}");
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false}");
            }
        }
    }

}
