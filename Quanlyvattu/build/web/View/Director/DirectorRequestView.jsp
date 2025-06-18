<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Pending Requests</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100 text-gray-900">
        <div class="container mx-auto p-6">
            <h1 class="text-2xl font-bold mb-6">Pending Material Requests</h1>

            <c:if test="${empty pendingRequests}">
                <div class="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4" role="alert">
                    <p class="font-bold">No pending requests</p>
                    <p>There are currently no material requests waiting for approval.</p>
                </div>
            </c:if>

            <c:if test="${not empty pendingRequests}">
                <table class="min-w-full bg-white border rounded shadow overflow-hidden">
                    <thead class="bg-blue-800 text-white">
                        <tr>
                            <th class="px-6 py-3 text-left text-sm font-medium">Request ID</th>
                            <th class="px-6 py-3 text-left text-sm font-medium">Requester</th>
                            <th class="px-6 py-3 text-left text-sm font-medium">Type</th>
                            <th class="px-6 py-3 text-left text-sm font-medium">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${pendingRequests}">
                            <tr class="border-b hover:bg-gray-50">
                                <td class="px-6 py-4">${req.requestId}</td>
                                <td class="px-6 py-4">${req.requester.fullName}</td>
                                <td class="px-6 py-4">${req.requestType.requestTypeName}</td>
                                <td class="px-6 py-4">
                                    <a href="${pageContext.request.contextPath}/request-detail?id=${req.requestId}"
                                       class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded text-sm">
                                        View Detail
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- PHÂN TRANG chuyển xuống dưới bảng -->
                <c:if test="${totalPages > 1}">
                    <div class="flex justify-center mt-6 space-x-2">
                        <c:if test="${currentPage > 1}">
                            <a href="?page=${currentPage - 1}"
                               class="px-3 py-1 bg-gray-300 rounded hover:bg-gray-400">&laquo; Prev</a>
                        </c:if>

                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <a href="?page=${i}"
                               class="px-3 py-1 rounded ${i == currentPage ? 'bg-blue-500 text-white' : 'bg-gray-200 hover:bg-gray-300'}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="?page=${currentPage + 1}"
                               class="px-3 py-1 bg-gray-300 rounded hover:bg-gray-400">Next &raquo;</a>
                        </c:if>
                    </div>
                </c:if>
            </c:if>

            <div class="mt-6">
                <a href="${pageContext.request.contextPath}/dashboard"
                   class="inline-block text-blue-600 hover:underline">
                    <i class="fas fa-arrow-left mr-1"></i> Back to Dashboard
                </a>
            </div>
        </div>
    </body>
</html>
