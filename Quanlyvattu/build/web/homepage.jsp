<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto">
    <h2 class="text-2xl font-semibold mb-6">Dashboard</h2>

    <!-- Bắt đầu: Box thống kê -->
    <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-6">
        <div class="bg-white rounded-lg shadow-md flex items-center justify-between p-4">
            <i class="fas fa-chart-line text-blue-500 text-3xl"></i>
            <div class="text-right ml-3">
                <p class="text-sm text-gray-500">Total Material Category</p>
                <h6 class="text-lg font-semibold text-gray-700">${totalCategory}</h6>
            </div>
        </div>
        <div class="bg-white rounded-lg shadow-md flex items-center justify-between p-4">
            <i class="fas fa-chart-bar text-blue-500 text-3xl"></i>
            <div class="text-right ml-3">
                <p class="text-sm text-gray-500">Total Import</p>
                <h6 class="text-lg font-semibold text-gray-700">${totalImport}</h6>

            </div>
        </div>
        <div class="bg-white rounded-lg shadow-md flex items-center justify-between p-4">
            <i class="fas fa-chart-area text-blue-500 text-3xl"></i>
            <div class="text-right ml-3">
                <p class="text-sm text-gray-500">Total Export</p>
                <h6 class="text-lg font-semibold text-gray-700">${totalImport}</h6>

            </div>
        </div>
        <div class="bg-white rounded-lg shadow-md flex items-center justify-between p-4">
            <i class="fas fa-chart-pie text-blue-500 text-3xl"></i>
            <div class="text-right ml-3">
                <p class="text-sm text-gray-500">Pending request</p>

                <h6 class="text-lg font-semibold text-gray-700">${pendingRequest}</h6>
            </div>
        </div>
    </div>
    <!-- Kết thúc: Box thống kê -->

    <!-- Biểu đồ -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <h3 class="text-lg font-semibold mb-2">Material Flow Chart</h3>
        <canvas id="materialChart" width="400" height="200"></canvas>
    </div>

    <!-- Bảng dữ liệu -->
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

<!-- JS Biểu đồ -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const ctx = document.getElementById('materialChart').getContext('2d');
    const materialChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ${chartLabels},
            datasets: [{
                    label: 'Materials',
                    data: ${chartData},
                    backgroundColor: 'rgba(54, 162, 235, 0.6)'
                }]
        }
    });
</script>

<!-- FontAwesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
