<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Bootstrap & FontAwesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

<style>
/* Layout & Style giữ nguyên */
.main-container {
    max-width: 1200px;
    margin: auto;
    padding: 20px;
}
.header-card {
    background-color: #1e3a8a;
    color: white;
    padding: 30px;
    border-radius: 15px;
    margin-bottom: 30px;
}
.content-card {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 5px 20px rgba(0,0,0,0.08);
}
.table-container { overflow: auto; border-radius: 10px; }
.table thead th {
    background-color: #1e3a8a;
    color: white;
}
.form-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
}
.btn-confirm {
    background-color: #16a34a;
    color: white;
    border-radius: 25px;
    padding: 12px 30px;
    font-weight: 600;
}
.btn-back {
    background-color: #6b7280;
    color: white;
    border-radius: 25px;
    padding: 12px 30px;
    font-weight: 600;
    text-decoration: none;
}
.alert-success { background-color: #22c55e; color: white; }
.alert-danger { background-color: #ef4444; color: white; }
.btn-confirm.bg-yellow-500:hover { background-color: #d97706; }
</style>

<!-- Header -->
<div class="header-card flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
    <div>
        <h1 class="text-3xl font-bold flex items-center gap-2">
            <i class="fas fa-edit text-white text-3xl"></i> Request Confirmation
        </h1>
        <p class="mt-1 text-white text-sm md:text-base opacity-90">
            Update actual quantities for request items
        </p>
    </div>
    <div class="flex items-center gap-2 bg-white text-blue-900 px-4 py-2 rounded-lg shadow-md">
        <i class="fas fa-info-circle"></i>
        <span class="font-medium text-sm">Ensure values do not exceed stock</span>
    </div>
</div>

<!-- Alert Message -->
<c:if test="${not empty message}">
    <div class="mb-6 p-4 rounded-lg flex items-center gap-3
        ${messageType eq 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'}">
        <i class="fas ${messageType eq 'success' ? 'fa-check-circle' : 'fa-exclamation-triangle'} text-2xl"></i>
        <div>
            <h3 class="font-semibold text-lg">
                ${messageType eq 'success' ? 'Update Successful!' : 'Update Failed!'}
            </h3>
            <p class="text-sm">${message}</p>
        </div>
    </div>
</c:if>

<!-- Main Content -->
<div class="content-card">
    <form method="post" action="RequestUpdateServlet">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="totalItems" value="${fn:length(requestItems)}" />

        <div class="table-container overflow-x-auto rounded-lg">
            <table class="min-w-full text-sm text-left">
                <thead>
                    <tr class="bg-blue-800 text-white text-base">
                        <th class="p-3 font-semibold">Request Type</th>
                        <th class="p-3 font-semibold">Material Name</th>
                        <th class="p-3 font-semibold text-center">Requested Qty</th>
                        <th class="p-3 font-semibold text-center">Actual Quantity</th>
                        <th class="p-3 font-semibold text-center">Stock</th>
                        <th class="p-3 font-semibold">Note</th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <c:forEach var="item" items="${requestItems}" varStatus="status">
                        <tr>
                            <td class="p-3">${item.requestTypeName}</td>
                            <td class="p-3">
                                <strong>${item.materialName}</strong>
                                <input type="hidden" name="materialId_${status.index}" value="${item.materialId}" />
                                <input type="hidden" name="requestType_${status.index}" value="${item.requestTypeName}" />
                            </td>
                            <td class="p-3 text-center">
                                ${item.quantity}
                                <input type="hidden" name="requestedQuantity_${status.index}" value="${item.quantity}" />
                            </td>
                            <td class="p-3 text-center">
                                <c:choose>
                                    <c:when test="${editMode and not isAlreadyUpdated}">
                                        <input type="number" name="actualQuantity_${status.index}" min="0"
                                               max="${item.stockQuantity}" value="${item.actualQuantity}" required
                                               class="border rounded-md px-3 py-1 w-24 text-center" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="number" value="${item.actualQuantity}" disabled
                                               class="border bg-gray-100 rounded-md px-3 py-1 w-24 text-center" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="p-3 text-center">${item.stockQuantity}</td>
                            <td class="p-3 text-gray-600 italic">
                                <c:choose>
                                    <c:when test="${not empty item.note}">${item.note}</c:when>
                                    <c:otherwise><span class="text-gray-400">No notes</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Action Buttons -->
        <div class="form-actions">
            <a href="reqlist" class="btn-back">
                <i class="fas fa-arrow-left me-2"></i>Back
            </a>

            <c:choose>
                <c:when test="${isAlreadyUpdated}">
                    <button class="btn-confirm opacity-50 cursor-not-allowed" disabled>
                        Already Updated
                    </button>
                </c:when>
                <c:when test="${editMode}">
                    <button type="submit" name="mode" value="save" class="btn-confirm">
                        <i class="fas fa-save me-2"></i>Save
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="submit" name="mode" value="edit" class="btn-confirm bg-yellow-500">
                        <i class="fas fa-edit me-2"></i>Edit
                    </button>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${canConfirm and not isAlreadyUpdated}">
            <div class="form-actions">
                <button type="submit" name="mode" value="confirm" class="btn-confirm bg-green-600 hover:bg-green-700">
                    <i class="fas fa-check me-2"></i>Confirm Request
                </button>
            </div>
        </c:if>
    </form>
</div>
