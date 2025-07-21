<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <title>Thống kê Vật tư</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/choices.js/public/assets/styles/choices.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/choices.js/public/assets/scripts/choices.min.js"></script>
</head>
<div class="container mx-auto px-4 py-6">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">Material Statistics</h1>
        <a href="${pageContext.request.contextPath}/advanced-dashboard" 
           class="inline-flex items-center text-blue-600 hover:text-blue-800">
            <i class="fas fa-arrow-left mr-2"></i> Back to Advanced Dashboard
        </a>
    </div>

    <!-- Filter Form -->
    <form action="material-statistics" method="POST" class="mb-8 p-6 bg-gray-50 rounded-lg border border-gray-200">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
            <!-- Report Type -->
            <div class="md:col-span-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">Report Type</label>
                <input type="hidden" name="reportType" id="reportTypeInput" value="${not empty selectedReportType ? selectedReportType : 'inventory'}">
                <div id="report-type-buttons" class="flex rounded-md shadow-sm">
                    <button type="button" data-type="inventory" class="report-type-btn relative inline-flex items-center px-4 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:z-10 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500">
                        Inventory Report
                    </button>
                    <button type="button" data-type="import" class="report-type-btn -ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:z-10 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500">
                        Warehouse Report
                    </button>
                    <button type="button" data-type="export" class="report-type-btn -ml-px relative inline-flex items-center px-4 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:z-10 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500">
                        goods issue
                    </button>
                </div>
            </div>

            <!-- Material Selection -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Select Materials</label>
                <select name="materialIds" multiple class="w-full p-2 border border-gray-300 rounded-md">
                    <c:forEach var="material" items="${materials}">
                        <option value="${material.materialId}" 
                            <c:forEach var="selectedId" items="${selectedMaterialIds}">
                                <c:if test="${selectedId == material.materialId}">selected</c:if>
                            </c:forEach>>
                            ${material.materialName}
                        </option>
                    </c:forEach>
                </select>
                <small class="text-gray-500">Leave blank to see all supplies</small>
            </div>

            <!-- Date Range -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">From date</label>
                <input type="date" name="startDate" value="${startDate}" 
                       class="w-full p-2 border border-gray-300 rounded-md">
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">By date</label>
                <input type="date" name="endDate" value="${endDate}" 
                       class="w-full p-2 border border-gray-300 rounded-md">
            </div>
        </div>

        <div class="mt-4 flex justify-between">
            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-6 rounded-lg">
                <i class="fas fa-search mr-2"></i> Filter data
            </button>
            <button type="button" onclick="exportData()" class="bg-green-600 hover:bg-green-700 text-white py-2 px-6 rounded-lg">
                <i class="fas fa-file-export mr-2"></i> Export Excel file
            </button>
        </div>
    </form>

        <!-- Results Table -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <c:choose>
            <%-- Inventory Report --%>
            <c:when test="${selectedReportType == 'inventory' and not empty statistics}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">STT</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material name</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Beginning balance</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total import</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total export</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Ending balance</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="stat" items="${statistics}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${status.index + 1}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${stat.materialId}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${stat.materialName}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${stat.initialStock}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-green-600">${stat.totalImported}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-red-600">${stat.totalExported}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${stat.finalStock}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <%-- Import Report --%>
            <c:when test="${selectedReportType == 'import' and not empty importReport}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">STT</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material name</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantity</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date of entry</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">The performer</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Note</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="item" items="${importReport}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${status.index + 1}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.materialId}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${item.materialName}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.quantity}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <fmt:formatDate value="${item.transactionDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.performedBy}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${item.note}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <%-- Export Report --%>
            <c:when test="${selectedReportType == 'export' and not empty exportReport}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">STT</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Material name</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantity</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date of entry</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">The performer</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Note</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="item" items="${exportReport}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${status.index + 1}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.materialId}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${item.materialName}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.quantity}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <fmt:formatDate value="${item.transactionDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.performedBy}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${item.note}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="p-8 text-center text-gray-500">
                    <i class="fas fa-inbox text-4xl mb-4"></i>
                    <p>No statistics available for the selected filter.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Initialize Choices.js for multi-select
        var multipleCancelButton = new Choices('select[name="materialIds"]', {
            removeItemButton: true,
            placeholder: true,
            placeholderValue: 'Chọn một hoặc nhiều vật tư',
            searchPlaceholderValue: 'Tìm kiếm vật tư',
        });

        // Report type button group logic
        const reportTypeButtons = document.querySelectorAll('.report-type-btn');
        const reportTypeInput = document.getElementById('reportTypeInput');

        // Function to set the active button
        const setActiveButton = (type) => {
            reportTypeButtons.forEach(button => {
                if (button.dataset.type === type) {
                    button.classList.add('bg-blue-600', 'text-white', 'hover:bg-blue-700');
                    button.classList.remove('bg-white', 'text-gray-700');
                } else {
                    button.classList.add('bg-white', 'text-gray-700');
                    button.classList.remove('bg-blue-600', 'text-white', 'hover:bg-blue-700');
                }
            });
        };

        // Set initial active button based on hidden input value
        setActiveButton(reportTypeInput.value);

        // Add click event listeners to buttons
        reportTypeButtons.forEach(button => {
            button.addEventListener('click', () => {
                const type = button.dataset.type;
                reportTypeInput.value = type;
                setActiveButton(type);
            });
        });
    });

    function exportData() {
        console.log('Export function called');

        // Get current form data
        const form = document.querySelector('form[action="material-statistics"]');
        if (!form) {
            alert('Form not found!');
            return;
        }

        console.log('Form found, creating export request...');

        // Create a new form for export
        const exportForm = document.createElement('form');
        exportForm.method = 'POST';
        exportForm.action = 'material-statistics';
        exportForm.style.display = 'none';

        // Copy all form data
        const formData = new FormData(form);
        for (let [key, value] of formData.entries()) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = value;
            exportForm.appendChild(input);
        }

        // Add export action
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = 'export';
        exportForm.appendChild(actionInput);

        // Submit export form
        document.body.appendChild(exportForm);
        console.log('Submitting export form...');
        exportForm.submit();

        // Clean up
        setTimeout(() => {
            document.body.removeChild(exportForm);
        }, 1000);
    }
</script>