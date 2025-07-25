<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <title>Purchase Order Requests</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    </head>
    <body class="bg-gray-100">
        <div class="container mx-auto px-4 py-6">
            <h1 class="text-2xl font-bold text-gray-800 mb-4">Approved Purchase Requests</h1>

            <c:if test="${empty requestList}">
                <div class="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded relative">
                    No approved requests found that require a purchase order.
                </div>
            </c:if>

            <c:if test="${not empty requestList}">
                <table class="min-w-full bg-white shadow-md rounded-lg overflow-hidden border border-gray-200">
                    <thead class="bg-gray-200 text-gray-700">
                        <tr>
                            <th class="py-2 px-4 text-left">Requested By</th>
                            <th class="py-2 px-4 text-left">Request Date</th>
                            <th class="py-2 px-4 text-left">Note</th>
                            <th class="py-2 px-4 text-left">Approved Request By</th>
                            <th class="py-2 px-4 text-left">Approved Date</th>
                            <th class="py-2 px-4 text-center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${requestList}">
                            <tr class="border-t hover:bg-gray-50">
                                <td class="py-2 px-4">${req.requestedByName}</td>
                                <td class="py-2 px-4">
                                    <fmt:formatDate value="${req.requestDate}" pattern="yyyy-MM-dd HH:mm" />
                                </td>
                                <td class="py-2 px-4">${req.note}</td>
                                <td class="py-2 px-4">${req.approvedByName}</td>
                                <td class="py-2 px-4">
                                    <fmt:formatDate value="${req.approvedDate}" pattern="yyyy-MM-dd HH:mm" />
                                </td>
                                <td class="py-2 px-4 text-center">
                                    <c:if test="${sessionScope.userRole == 'Accountant'}">
                                        <a href="${pageContext.request.contextPath}/create-purchase-order?requestId=${req.requestId}"
                                           class="bg-green-500 hover:bg-green-600 text-white text-sm font-semibold py-1 px-3 rounded">
                                            Create Purchase Order
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </body>
</html>
