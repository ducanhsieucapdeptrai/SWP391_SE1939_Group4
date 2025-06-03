<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-semibold">Material Statistics</h2>
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
    
    <!-- Statistics Dashboard -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
        <!-- Status Statistics Card -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4">Materials by Status</h3>
            <div class="mb-4">
                <canvas id="statusChart" width="400" height="300"></canvas>
            </div>
            <div class="overflow-auto max-h-48">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Count</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <c:forEach var="entry" items="${materialsByStatus}">
                            <tr>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${entry.key}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${entry.value}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty materialsByStatus}">
                            <tr>
                                <td colspan="2" class="px-6 py-4 text-center text-sm text-gray-500">No data available</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Type Statistics Card -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4">Materials by Type</h3>
            <div class="mb-4">
                <canvas id="typeChart" width="400" height="300"></canvas>
            </div>
            <div class="overflow-auto max-h-48">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Count</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <c:forEach var="entry" items="${materialsByType}">
                            <tr>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${entry.key}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${entry.value}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty materialsByType}">
                            <tr>
                                <td colspan="2" class="px-6 py-4 text-center text-sm text-gray-500">No data available</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <!-- Materials List Card -->
    <div class="bg-white rounded-lg shadow-md p-6">
        <h3 class="text-lg font-semibold mb-4">All Materials</h3>
        <div class="overflow-auto max-h-96">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <c:forEach var="material" items="${materials}">
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${material.materialId}</td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${material.materialName}</td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${material.statusName}</td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${material.description}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty materials}">
                        <tr>
                            <td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">No materials found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Chart.js for visualizations -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Prepare data for Status Chart
        const statusLabels = [];
        const statusData = [];
        const statusColors = [
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 99, 132, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)'
        ];
        
        <c:forEach var="entry" items="${materialsByStatus}" varStatus="status">
            statusLabels.push('${entry.key}');
            statusData.push(${entry.value});
        </c:forEach>
        
        // Create Status Chart
        const statusCtx = document.getElementById('statusChart').getContext('2d');
        const statusChart = new Chart(statusCtx, {
            type: 'pie',
            data: {
                labels: statusLabels,
                datasets: [{
                    data: statusData,
                    backgroundColor: statusColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right'
                    }
                }
            }
        });
        
        // Prepare data for Type Chart
        const typeLabels = [];
        const typeData = [];
        const typeColors = [
            'rgba(255, 159, 64, 0.6)',
            'rgba(255, 99, 132, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)'
        ];
        
        <c:forEach var="entry" items="${materialsByType}" varStatus="status">
            typeLabels.push('${entry.key}');
            typeData.push(${entry.value});
        </c:forEach>
        
        // Create Type Chart
        const typeCtx = document.getElementById('typeChart').getContext('2d');
        const typeChart = new Chart(typeCtx, {
            type: 'doughnut',
            data: {
                labels: typeLabels,
                datasets: [{
                    data: typeData,
                    backgroundColor: typeColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right'
                    }
                }
            }
        });
    });
</script>
