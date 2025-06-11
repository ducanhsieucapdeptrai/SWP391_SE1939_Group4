<<<<<<< HEAD
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Material Detail</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-6">
    <div class="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-md">
        <h1 class="text-2xl font-bold text-gray-800 mb-6">Material Details</h1>

        <div class="grid grid-cols-2 gap-6 text-gray-700">
            <div>
                <p class="font-semibold">Material Name</p>
                <p class="bg-gray-100 p-2 rounded">${m.materialName}</p>
            </div>

            <div>
                <p class="font-semibold">Category</p>
                <p class="bg-gray-100 p-2 rounded">${m.categoryName}</p>
            </div>

            <div>
                <p class="font-semibold">Subcategory</p>
                <p class="bg-gray-100 p-2 rounded">${m.subCategoryName}</p>
            </div>

            <div>
                <p class="font-semibold">Price</p>
                <p class="bg-gray-100 p-2 rounded">
                    <fmt:formatNumber value="${m.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                </p>
            </div>

            <div>
                <p class="font-semibold">Current Stock</p>
                <p class="bg-gray-100 p-2 rounded">${m.quantity}</p>
            </div>

            <div>
                <p class="font-semibold">Minimum Stock</p>
                <p class="bg-gray-100 p-2 rounded">${m.minQuantity}</p>
            </div>

            <div>
                <p class="font-semibold">Status</p>
                <p class="bg-gray-100 p-2 rounded">${m.statusName}</p>
            </div>
        </div>

        <!-- Nút điều hướng -->
        <div class="mt-8 flex justify-between">
            <!-- Quay lại danh sách -->
            <a href="${pageContext.request.contextPath}/materiallist" class="bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded">
                Back to List
            </a>

            <!-- Nút Edit -->
            <a href="${pageContext.request.contextPath}/editmaterial?materialId=${m.materialId}" 
               class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded">
                ✏️ Edit
            </a>
        </div>
    </div>
</body>
</html>
=======
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mx-auto">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-semibold">Audit Log</h2>
        <div>
            <a href="${pageContext.request.contextPath}/advanced-dashboard" class="inline-flex items-center text-blue-600 hover:text-blue-800">
                <i class="fas fa-arrow-left mr-2"></i> Back to Advanced Dashboard
            </a>
        </div>
    </div>
    
    <!-- Error Message Display -->
    <c:if test="${not empty errorMessage}">
        <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
            <p>${errorMessage}</p>
        </div>
    </c:if>
    
    <!-- Sorting Controls -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center">
            <h3 class="text-lg font-semibold">Sort Options</h3>
            <div>
                <a href="${pageContext.request.contextPath}/audit-log?sort=desc" 
                   class="px-3 py-1 rounded ${currentSort == 'desc' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}">
                    Newest First
                </a>
                <a href="${pageContext.request.contextPath}/audit-log?sort=asc" 
                   class="px-3 py-1 rounded ml-2 ${currentSort == 'asc' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}">
                    Oldest First
                </a>
            </div>
        </div>
    </div>
    
    <!-- Audit Log Table -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Timestamp</th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <c:choose>
                    <c:when test="${not empty auditLogs}">
                        <c:forEach var="log" items="${auditLogs}">
                            <tr class="hover:bg-gray-50">
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${log.id}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${log.action}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    ${not empty log.userName ? log.userName : 'User #'.concat(log.userId)}
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <fmt:formatDate value="${log.timestamp}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">
                                No audit logs found. System activity will be recorded here.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>
>>>>>>> 42f42e462b572ce24598a0e945660367605ba88b
