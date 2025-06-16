<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Pending Purchase Orders</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100">
        <div class="container mx-auto px-6 py-8 bg-white shadow rounded mt-6">
            <h2 class="text-2xl font-bold mb-4 text-blue-800">Pending Purchase Orders</h2>

            <c:if test="${empty pendingOrders}">
                <div class="text-gray-600 italic">No pending purchase orders found.</div>
            </c:if>

            <c:if test="${not empty pendingPOs}">
                <table class="min-w-full bg-white border border-gray-200 rounded shadow">
                    <thead class="bg-gray-100 text-gray-700">
                        <tr>
                            <th class="px-4 py-2 border">PO ID</th>
                            <th class="px-4 py-2 border">Request ID</th>
                            <th class="px-4 py-2 border">Created By</th>
                            <th class="px-4 py-2 border">Created Date</th>
                            <th class="px-4 py-2 border">Total Price</th>
                            <th class="px-4 py-2 border">Note</th>
                            <th class="px-4 py-2 border">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="po" items="${pendingOrders}">
                        <tr class="hover:bg-gray-50">
                            <td class="px-4 py-2 border">${po.poId}</td>
                            <td class="px-4 py-2 border">${po.requestId}</td>
                            <td class="px-4 py-2 border">${po.createdBy}</td>
                            <td class="px-4 py-2 border">${po.createdDate}</td>
                            <td class="px-4 py-2 border text-green-700 font-semibold">${po.totalPrice} VND</td>
                            <td class="px-4 py-2 border">${po.note}</td>
                            <td class="px-4 py-2 border text-center">
                                <a href="${pageContext.request.contextPath}/po-detail?poId=${po.poId}"
                                   class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700">View</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </body>
</html>
