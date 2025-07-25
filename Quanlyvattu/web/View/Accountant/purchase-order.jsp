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

            <!-- Search Form -->
            <form method="get" class="mb-4 flex flex-wrap gap-2 items-end">
                <input type="text" name="searchName" placeholder="Search by Name" value="${searchName}" class="border rounded px-3 py-2" />
                <input type="text" name="searchNote" placeholder="Search by Note" value="${searchNote}" class="border rounded px-3 py-2" />
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Search</button>
                <a href="?" class="bg-gray-300 text-gray-800 px-4 py-2 rounded ml-2 hover:bg-gray-400">Reset Search</a>
            </form>

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

                <!-- Pagination -->
                <div class="flex justify-center items-center gap-2 mt-4">
                    <c:if test="${currentPage > 1}">
                        <a href="?searchName=${searchName}&searchNote=${searchNote}&page=${currentPage - 1}"
                           class="px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200 font-bold text-lg flex items-center justify-center">&lt;</a>
                    </c:if>
                    <span class="px-3 py-1 bg-blue-600 text-white rounded font-bold text-lg">${currentPage}</span>
                    <c:if test="${currentPage < totalPages}">
                        <a href="?searchName=${searchName}&searchNote=${searchNote}&page=${currentPage + 1}"
                           class="px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200 font-bold text-lg flex items-center justify-center">&gt;</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </body>
</html>
