<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

    <!-- Ví dụ: Bảng dữ liệu -->
    <div class="bg-white rounded-lg shadow-md p-4">
        <h3 class="text-lg font-semibold mb-2">Recent Material Entries</h3>
        <table class="min-w-full divide-y divide-gray-200">
            <thead>
                <tr>
                    <th class="px-4 py-2">ID</th>
                    <th class="px-4 py-2">Material</th>
                    <th class="px-4 py-2">Quantity</th>
                    <th class="px-4 py-2">Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${recentMaterials}">
                    <tr class="bg-gray-100">
                        <td class="px-4 py-2">${item.id}</td>
                        <td class="px-4 py-2">${item.name}</td>
                        <td class="px-4 py-2">${item.quantity}</td>
                        <td class="px-4 py-2">${item.date}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
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