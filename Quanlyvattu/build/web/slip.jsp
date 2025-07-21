<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Slip - ${requestType} Request #${requestId}</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        @media print {
            body * {
                visibility: hidden;
            }
            #print-area, #print-area * {
                visibility: visible;
            }
            #print-area {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
            }
        }
    </style>
</head>
<body class="bg-white text-black">

<div id="print-area" class="max-w-4xl mx-auto p-8 bg-white border border-gray-300 shadow-sm rounded-lg mt-10">
    <!-- Header -->
    <div class="text-center mb-8">
    <h1 class="text-3xl font-bold">Warehouse Invoice</h1>
    <p class="text-sm text-gray-600 mt-1">Request Type: <strong>${requestType}</strong></p>
    <p class="text-sm text-gray-600">Request ID: <strong>#${requestId}</strong></p>
    <p class="text-sm text-gray-800 mt-1">
        Slip ID: <span class="font-mono font-semibold text-blue-700">${taskLog.slipCode}</span>
    </p>
</div>

    <!-- Request Info -->
    <div class="grid grid-cols-2 gap-4 mb-6 text-sm">
        <div>
            <p><strong>Type:</strong> ${requestType}</p>
            <p><strong>Date:</strong> <fmt:formatDate value="${signDate}" pattern="dd/MM/yyyy HH:mm"/></p>
        </div>
        <div>
            <p><strong>Processed by:</strong> ${staffName}</p>
            <p><strong>Description:</strong> ${note}</p>
        </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
        <table class="min-w-full text-sm border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 border-b text-left">#</th>
                <th class="px-4 py-2 border-b text-left">Material</th>
                <th class="px-4 py-2 border-b text-center">Quantity</th>
                <th class="px-4 py-2 border-b text-center">Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="detail" items="${taskLog.slipDetails}" varStatus="loop">
                <tr class="border-b">
                    <td class="px-4 py-2">${loop.index + 1}</td>
                    <td class="px-4 py-2">${detail.materialName}</td>
                    <td class="px-4 py-2 text-center">${detail.quantity}</td>
                    <td class="px-4 py-2 text-center">
                        <fmt:formatDate value="${taskLog.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Footer -->
    <div class="mt-10 grid grid-cols-2 text-sm">
        <div>
            <p><strong>Warehouse Staff Signature:</strong></p>
            <p class="mt-12 font-medium underline">${staffName}</p>
        </div>
        <div class="text-right">
            <p><strong>Date:</strong></p>
            <p class="mt-12 font-medium">
                <fmt:formatDate value="${signDate}" pattern="dd/MM/yyyy"/>
            </p>
        </div>
    </div>
</div>

<!-- Buttons -->
<div class="mt-8 text-center no-print">
    <button onclick="window.print()"
            class="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium">
        Print Invoice
    </button>
    <a href="taskUpdate?requestId=${requestId}"
       class="ml-4 px-6 py-2 bg-gray-200 hover:bg-gray-300 text-gray-700 rounded-lg font-medium">
        Back to Task
    </a>
</div>

</body>
</html>
