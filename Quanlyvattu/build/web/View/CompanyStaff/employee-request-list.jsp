<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>My Material Requests</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <!-- Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body class="bg-gray-100">

        <div class="container mx-auto p-6 bg-white rounded shadow mt-6">
            <h2 class="text-2xl font-semibold mb-4">My Material Requests</h2>

            <!-- Filter -->
            <form method="get" class="mb-4 flex items-center gap-4">
                <label for="status" class="font-medium">Filter by Status:</label>
                <select name="status" id="status" class="border px-2 py-1 rounded">
                    <option value="">-- All --</option>
                    <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="Approved" ${param.status == 'Approved' ? 'selected' : ''}>Approved</option>
                    <option value="Rejected" ${param.status == 'Rejected' ? 'selected' : ''}>Rejected</option>
                </select>
                <button type="submit" class="px-4 py-1 bg-blue-600 text-white rounded hover:bg-blue-700">
                    Filter
                </button>
            </form>

            <!-- Table -->
            <table class="min-w-full bg-white border rounded shadow">
                <thead class="bg-gray-200">
                    <tr>
                        <th class="px-4 py-2 text-left">Request ID</th>
                        <th class="px-4 py-2 text-left">Date</th>
                        <th class="px-4 py-2 text-left">Type</th>
                        <th class="px-4 py-2 text-left">Status</th>
                        <th class="px-4 py-2 text-left">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="req" items="${requestList}">
                        <tr class="border-b hover:bg-gray-50">
                            <td class="px-4 py-2">${req.requestId}</td>
                            <td class="px-4 py-2">
                                <fmt:formatDate value="${req.requestDate}" pattern="yyyy-MM-dd" />
                            </td>
                            <td class="px-4 py-2">${req.requestType.requestTypeName}</td>
                            <td class="px-4 py-2">
                                <c:choose>
                                    <c:when test="${req.status == 'Approved'}">
                                        <span class="text-green-600 font-medium">Approved</span>
                                    </c:when>
                                    <c:when test="${req.status == 'Rejected'}">
                                        <span class="text-red-600 font-medium">Rejected</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-yellow-600 font-medium">Pending</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="px-4 py-2">
                                <div class="flex justify-between items-center">
                                    <a href="request-detail?id=${req.requestId}" 
                                       class="text-blue-600 hover:underline">
                                        View
                                    </a>

                                    <c:if test="${req.status == 'Approved' && req.requestType.requestTypeName == 'Material Purchase'}">
                                        <a href="create-po?requestId=${req.requestId}" 
                                           class="inline-flex items-center bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm shadow ml-auto">
                                            <i class="fas fa-file-invoice-dollar mr-1"></i> Create Purchase Order
                                        </a>
                                    </c:if>
                                </div>
                            </td>

                        </tr>
                    </c:forEach>

                    <!-- No data row -->
                    <c:if test="${empty requestList}">
                        <tr>
                            <td colspan="5" class="text-center text-gray-500 py-4">
                                No requests found.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </body>
</html>
