<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-6">Request Detail-Actual Record</h1>

    <!-- Request Information -->
    <div class="bg-white p-6 rounded-lg shadow-md mb-6">
        <h2 class="text-xl font-semibold mb-4">Request Information</h2>
        <table class="w-full table-auto">
            <tr>
                <th class="px-4 py-2 text-left">Request ID</th>
                <td class="px-4 py-2">${request.requestId}</td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Requested By</th>
                <td class="px-4 py-2">${request.requestedByName}</td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Request Date</th>
                <td class="px-4 py-2">
                    <fmt:formatDate value="${request.requestDate}" pattern="dd-MM-yyyy HH:mm"/>
                </td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Request Type</th>
                <td class="px-4 py-2">${request.requestType.requestTypeName}</td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Sub Type</th>
                <td class="px-4 py-2">${request.subTypeName}</td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Status</th>
                <td class="px-4 py-2">${request.statusDescription}</td>
            </tr>
            <tr>
                <th class="px-4 py-2 text-left">Note</th>
                <td class="px-4 py-2">${request.note}</td>
            </tr>
        </table>
    </div>

    <!-- Request Details -->
    <div class="bg-white p-6 rounded-lg shadow-md mb-6">
        <h2 class="text-xl font-semibold mb-4">Request Details</h2>

        <c:choose>
            <c:when test="${fn:length(requestDetails) == 0}">
                <p class="text-red-500">No details found for this request.</p>
            </c:when>
            <c:otherwise>
                <table class="w-full table-auto">
                    <thead>
                        <tr class="bg-gray-200">
                            <th class="px-4 py-2 text-left">Image</th>
                            <th class="px-4 py-2 text-left">Material</th>
                            <th class="px-4 py-2 text-left">Quantity</th>
                            <th class="px-4 py-2 text-left">Actual Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="totalQuantity" value="0"/>
                        <c:set var="totalActualQuantity" value="0"/>
                        <c:forEach var="detail" items="${requestDetails}">
                            <tr>
                                <td class="px-4 py-2">
                                    <c:choose>
                                        <c:when test="${not empty detail.imageUrl}">
                                            <img src="${detail.imageUrl}" alt="${detail.materialName}" class="material-img"/>
                                        </c:when>
                                        <c:otherwise>No Image</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-4 py-2">${detail.materialName}</td>
                                <td class="px-4 py-2">${detail.quantity}</td>
                                <td class="px-4 py-2">${detail.actualQuantity}</td>
                            </tr>
                            <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}"/>
                            <c:set var="totalActualQuantity" value="${totalActualQuantity + detail.actualQuantity}"/>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Summary -->
    <div class="summary-box mb-6">
        <h2 class="text-xl font-semibold mb-2">Summary</h2>
        <p><strong>Total Quantity:</strong> ${totalQuantity}</p>
        <p><strong>Total Actual Quantity:</strong> ${totalActualQuantity}</p>
    </div>

    <!-- Task Slips Grouped by Date -->
    <div class="bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4">Actual Progress</h2>
        <a href="createTaskSlip?requestId=${request.requestId}"
           class="bg-green-500 text-white px-4 py-2 rounded mt-4 inline-block">
            Create Task Slip
        </a>

        <c:forEach var="entry" items="${taskLogsByDate.entrySet()}">
            <div class="date-group">
                <h3 class="text-lg font-semibold mb-2">
                    <fmt:formatDate value="${entry.key}" pattern="dd-MM-yyyy"/>
                </h3>
                <c:forEach var="task" items="${entry.value}">
                    <div class="task-box">
                        <p><strong>Task ID:</strong> ${task.taskId}</p>
                        <p><strong>Staff:</strong> ${task.staffName}</p>
                        <p><strong>Request Type:</strong> ${task.requestTypeName}</p>
                        <p>
                            <strong>Created At:</strong>
                            <fmt:formatDate value="${task.createdAt}" pattern="dd-MM-yyyy HH:mm"/>
                        </p>
                        <h4 class="font-semibold mt-2">Slip Details:</h4>
                        <ul class="list-disc pl-6">
                            <c:forEach var="slip" items="${task.slipDetails}">
                                <li>${slip.materialName}: ${slip.quantity}</li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>

        
    </div>
</div>
