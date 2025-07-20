<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Import-Export-Inventory Statistics</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/choices.js/public/assets/styles/choices.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/choices.js/public/assets/scripts/choices.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

    <div class="container mx-auto p-8">
        <div class="bg-white rounded-lg shadow-xl p-6 md:p-8 mb-8">
            <div class="flex justify-between items-center mb-6">
                <h1 class="text-3xl font-bold text-gray-800">Import-Export-Inventory Statistics</h1>
                <a href="${pageContext.request.contextPath}/dashboard.jsp" class="text-blue-600 hover:text-blue-800 transition duration-300">
                    <i class="fas fa-arrow-left mr-2"></i>Back to Dashboard
                </a>
            </div>

            <!-- Filter Form -->
            <form action="inventory-statistics" method="POST" class="mb-8 p-6 bg-gray-50 rounded-lg border border-gray-200">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <!-- Material Selection -->
                    <div>
                        <label for="materialIds" class="block text-sm font-medium text-gray-700 mb-2">Select Materials (leave blank for all)</label>
                        <select id="materialIds" name="materialIds" multiple>
                            <c:forEach var="material" items="${materials}">
                                <option value="${material.materialId}" 
                                    <c:forEach var="selectedId" items="${selectedMaterialIds}">
                                        <c:if test="${selectedId == material.materialId}">selected</c:if>
                                    </c:forEach>>
                                    ${material.materialName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Date Range -->
                    <div>
                        <label for="startDate" class="block text-sm font-medium text-gray-700 mb-2">Start Date</label>
                        <input type="date" id="startDate" name="startDate" value="${selectedStartDate}" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                    </div>
                    <div>
                        <label for="endDate" class="block text-sm font-medium text-gray-700 mb-2">End Date</label>
                        <input type="date" id="endDate" name="endDate" value="${selectedEndDate}" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                    </div>
                </div>
                <div class="mt-6 text-right">
                    <button type="submit" class="inline-flex items-center px-6 py-2 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        <i class="fas fa-chart-bar mr-2"></i>Generate Report
                    </button>
                </div>
            </form>

            <!-- Results Table -->
            <c:if test="${not empty statistics}">
                <div class="overflow-x-auto">
                    <table class="min-w-full bg-white border-gray-200 shadow-md rounded-lg">
                        <thead class="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                            <tr>
                                <th class="py-3 px-6 text-left">Material Name</th>
                                <th class="py-3 px-6 text-center">Initial Stock</th>
                                <th class="py-3 px-6 text-center">Imported in Period</th>
                                <th class="py-3 px-6 text-center">Exported in Period</th>
                                <th class="py-3 px-6 text-center">Final Stock</th>
                            </tr>
                        </thead>
                        <tbody class="text-gray-700 text-sm font-light">
                            <c:forEach var="stat" items="${statistics}">
                                <tr class="border-b border-gray-200 hover:bg-gray-100">
                                    <td class="py-3 px-6 text-left whitespace-nowrap font-medium">${stat.materialName}</td>
                                    <td class="py-3 px-6 text-center">${stat.initialStock}</td>
                                    <td class="py-3 px-6 text-center text-green-600 font-semibold">+${stat.totalImported}</td>
                                    <td class="py-3 px-6 text-center text-red-600 font-semibold">-${stat.totalExported}</td>
                                    <td class="py-3 px-6 text-center font-bold text-indigo-700">${stat.finalStock}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${empty statistics and not empty selectedStartDate}">
                <p class="text-center text-gray-500 mt-8">No data found for the selected criteria.</p>
            </c:if>

            <c:if test="${not empty error}">
                <p class="text-center text-red-500 mt-8">${error}</p>
            </c:if>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const choices = new Choices('#materialIds', {
                removeItemButton: true,
                placeholder: true,
                placeholderValue: 'Select materials...',
                allowHTML: true
            });
        });
    </script>

</body>
</html>
