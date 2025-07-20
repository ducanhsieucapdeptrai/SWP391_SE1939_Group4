<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container mx-auto">
    <h2 class="text-2xl font-semibold mb-6">Advanced Dashboard</h2>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      
        
        <!-- Statistics Button Card -->
        <div class="bg-white rounded-lg shadow-md p-6 transition-transform transform hover:scale-105">
            <div class="flex flex-col items-center text-center">
                <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-4">
                    <i class="fas fa-chart-pie text-green-600 text-2xl"></i>
                </div>
                <h3 class="text-lg font-semibold mb-2">Statistics</h3>
                <p class="text-gray-600 mb-4">Xem thống kê chi tiết về nhập, xuất, tồn kho vật tư.</p>
                <a href="${pageContext.request.contextPath}/material-statistics" class="bg-green-600 hover:bg-green-700 text-white py-2 px-4 rounded-lg transition duration-300">
                    View Statistics
                </a>
            </div>
        </div>
        
        <!-- Catalog Management Button Card -->
        <div class="bg-white rounded-lg shadow-md p-6 transition-transform transform hover:scale-105">
            <div class="flex flex-col items-center text-center">
                <div class="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mb-4">
                    <i class="fas fa-book text-purple-600 text-2xl"></i>
                </div>
                <h3 class="text-lg font-semibold mb-2">Catalog Management</h3>
                <p class="text-gray-600 mb-4">Manage catalog items and their descriptions.</p>
                <a href="${pageContext.request.contextPath}/catalog-management" class="bg-purple-600 hover:bg-purple-700 text-white py-2 px-4 rounded-lg transition duration-300">
                    Manage Catalog
                </a>
            </div>
        </div>
    </div>
    
    <!-- Recent Material Entries -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <!-- Recent Imports -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4 text-blue-600">Recent Material Imports</h3>
            <c:choose>
                <c:when test="${not empty recentImports}">
                    <table class="w-full">
                        <thead>
                            <tr class="bg-gray-100 text-left">
                                <th class="p-2">Material</th>
                                <th class="p-2">Quantity</th>
                                <th class="p-2">Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${recentImports}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="p-2">
                                        <div class="font-medium">${material.materialName}</div>
                                        <div class="text-sm text-gray-500">${material.categoryName} / ${material.subCategoryName}</div>
                                    </td>
                                    <td class="p-2 font-medium">${material.importQuantity}</td>
                                    <td class="p-2">
                                        <div class="font-medium">
                                            <fmt:formatDate value="${material.importDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <div class="text-sm text-gray-500">
                                            <fmt:formatDate value="${material.importDate}" pattern="HH:mm"/>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-center">No recent imports today</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Recent Exports -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4 text-red-600">Recent Material Exports</h3>
            <c:choose>
                <c:when test="${not empty recentExports}">
                    <table class="w-full">
                        <thead>
                            <tr class="bg-gray-100 text-left">
                                <th class="p-2">Material</th>
                                <th class="p-2">Quantity</th>
                                <th class="p-2">Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${recentExports}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="p-2">
                                        <div class="font-medium">${material.materialName}</div>
                                        <div class="text-sm text-gray-500">${material.categoryName} / ${material.subCategoryName}</div>
                                    </td>
                                    <td class="p-2 font-medium">${material.exportQuantity}</td>
                                    <td class="p-2">
                                        <div class="font-medium">
                                            <fmt:formatDate value="${material.exportDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <div class="text-sm text-gray-500">
                                            <fmt:formatDate value="${material.exportDate}" pattern="HH:mm"/>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-center">No recent exports today</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <!-- Back to Dashboard Button -->
    <div class="text-center mt-8">
        <a href="${pageContext.request.contextPath}/dashboard" class="inline-flex items-center text-blue-600 hover:text-blue-800">
            <i class="fas fa-arrow-left mr-2"></i> Back to Main Dashboard
        </a>
    </div>
</div>
