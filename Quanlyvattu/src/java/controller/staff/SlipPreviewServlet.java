package controller.staff;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RequestDetailItem;
import model.Users;

import java.io.IOException;
import java.util.*;

@WebServlet("/slipPreview")
public class SlipPreviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String requestType = request.getParameter("requestType");
            String note = request.getParameter("note");

            // ✅ Dùng đúng session attribute đã set trong LoginServlet
            HttpSession session = request.getSession(false);
            Users currentUser = (Users) session.getAttribute("currentUser");
            System.out.println("✅ currentUser in session = " + session.getAttribute("currentUser"));
            String staffName = (currentUser != null && currentUser.getFullName() != null)
                    ? currentUser.getFullName()
                    : "Unknown";

            Map<String, String[]> paramMap = request.getParameterMap();
            List<RequestDetailItem> previewItems = new ArrayList<>();
            List<String> invalidQuantities = new ArrayList<>();
            boolean hasValidData = false;

            for (String param : paramMap.keySet()) {
                if (param.startsWith("actualQty_")) {
                    String materialIdStr = param.substring("actualQty_".length());
                    String qtyStr = request.getParameter(param);

                    if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                        try {
                            int materialId = Integer.parseInt(materialIdStr);
                            int actualQty = Integer.parseInt(qtyStr.trim());

                            if (actualQty > 0) {
                                RequestDetailItem item = new RequestDetailItem();
                                item.setMaterialId(materialId);
                                item.setQuantity(actualQty);
                                item.setMaterialName(request.getParameter("materialName_" + materialId));
                                previewItems.add(item);
                                hasValidData = true;
                            }
                        } catch (NumberFormatException e) {
                            invalidQuantities.add("ID: " + materialIdStr);
                        }
                    }
                }
            }

            if (!invalidQuantities.isEmpty()) {
                showAlertAndClose(response, "Invalid quantity for: " + String.join(", ", invalidQuantities));
                return;
            }

            if (!hasValidData) {
                showAlertAndClose(response, "Please enter at least one valid actual quantity.");
                return;
            }

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestType", requestType);
            request.setAttribute("note", note);
            request.setAttribute("staffName", staffName);
            request.setAttribute("previewItems", previewItems);
            request.setAttribute("now", new Date());

            request.getRequestDispatcher("/slipPreview.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            showAlertAndClose(response, "Error occurred during slip preview.");
        }
    }

    private void showAlertAndClose(HttpServletResponse response, String message) throws IOException {
        response.getWriter().println("<script>alert('" + message + "'); window.close();</script>");
    }
}
