<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    String staffNameAttr = (String) request.getAttribute("staffName");
    if (staffNameAttr == null || staffNameAttr.equals("Unknown")) {
        Object userObj = session.getAttribute("currentUser");
        if (userObj != null && userObj instanceof model.Users) {
            request.setAttribute("staffName", ((model.Users) userObj).getFullName());
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Slip Preview - ${requestType} Request #${requestId}</title>
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

<!-- Info slip -->
<div id="print-area" class="max-w-4xl mx-auto p-8 bg-white border border-gray-300 shadow-sm rounded-lg mt-6">
    <!-- Header -->
    <div class="text-center mb-8">
        <h1 class="text-3xl font-bold">Warehouse Invoice</h1>
        <p class="text-gray-600 mt-2">${requestType} Request #${requestId}</p>
    </div>

    <!-- Info -->
    <div class="grid grid-cols-2 gap-4 mb-6 text-sm">
        <div>
            <p><strong>Type:</strong> ${requestType}</p>
            <p><strong>Date:</strong> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm"/></p>
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
            <c:forEach var="item" items="${previewItems}" varStatus="loop">
                <tr class="border-b">
                    <td class="px-4 py-2">${loop.index + 1}</td>
                    <td class="px-4 py-2">${item.materialName}</td>
                    <td class="px-4 py-2 text-center">${item.quantity}</td>
                    <td class="px-4 py-2 text-center">
                        <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm"/>
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
            <p class="mt-12 font-medium underline">
                ${staffName}
            </p>
        </div>
        <div class="text-right">
            <p><strong>Date:</strong></p>
            <p class="mt-12 font-medium">
                <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/>
            </p>
        </div>
    </div>
</div>


<script>
    window.onload = function () {
        setTimeout(() => window.print(), 300);
    };
</script>

</body>
</html>
