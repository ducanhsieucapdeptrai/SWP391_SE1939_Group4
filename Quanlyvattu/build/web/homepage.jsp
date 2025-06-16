<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<div class="container mx-auto">
    <h2 class="text-2xl font-semibold mb-6">Dashboard</h2>

    <!-- Ví dụ: Biểu đồ -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <h3 class="text-lg font-semibold mb-2">Material Flow Chart</h3>
        
        <!-- Statistics Dashboard -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
            <!-- Total Material -->
            <div class="bg-blue-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Material</p>
                    <p class="text-xl font-bold">${totalMaterials}</p>
                </div>
            </div>
            
            <!-- Total Import -->
            <div class="bg-green-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4h13M3 8h9m-9 4h6m4 0l4-4m0 0l4 4m-4-4v12" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Import</p>
                    <p class="text-xl font-bold">${totalImports}</p>
                </div>
            </div>
            
            <!-- Total Export -->
            <div class="bg-indigo-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4h13M3 8h9m-9 4h9m5-4v12m0 0l-4-4m4 4l4-4" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Export</p>
                    <p class="text-xl font-bold">${totalExports}</p>
                </div>
            </div>
            
            <!-- Pending Requests -->
            <div class="bg-yellow-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-yellow-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Pending Requests</p>
                    <p class="text-xl font-bold">${pendingRequests}</p>
                </div>
            </div>
        </div>
        
        <canvas id="materialChart" width="400" height="200"></canvas>
    </div>

    <!-- Advanced Dashboard Button -->
    <div class="text-center mb-6">
        <a href="${pageContext.request.contextPath}/advanced-dashboard" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded-lg inline-flex items-center transition duration-300">
            <i class="fas fa-chart-line mr-2"></i> More Advanced Features
        </a>
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
                    <p class="text-gray-500 text-center">No recent imports found</p>
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
                    <p class="text-gray-500 text-center">No recent exports found</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Chart data preparation -->
<c:set var="chartLabelsJson" value="${chartLabels != null ? chartLabels : '[]'}" scope="request" />
<c:set var="chartDataJson" value="${chartData != null ? chartData : '[]'}" scope="request" />

<!-- Chart.js initialization -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Get chart data from hidden elements to avoid JSP/JS mixing issues
    document.addEventListener('DOMContentLoaded', function() {
        const ctx = document.getElementById('materialChart').getContext('2d');
        
        // Safe parsing of chart data
        let chartLabelsData = [];
        let chartValuesData = [];
        
        try {
            chartLabelsData = JSON.parse('${chartLabelsJson}');
        } catch (e) {
            console.error('Error parsing chart labels:', e);
        }
        
        try {
            chartValuesData = JSON.parse('${chartDataJson}');
        } catch (e) {
            console.error('Error parsing chart data:', e);
        }
        
        // Create chart
        const materialChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: chartLabelsData,
                datasets: [{
                    label: 'Materials',
                    data: chartValuesData,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)'
                }]
            },
           options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });
</script>