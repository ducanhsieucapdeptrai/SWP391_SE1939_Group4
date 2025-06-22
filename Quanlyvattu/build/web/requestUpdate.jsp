<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Re-include Bootstrap and FontAwesome (in case layout doesn't) -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

<style>
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
.table-container {
    overflow: auto;
    border-radius: 10px;
}
.table thead th {
    background-color: #1e3a8a;
    color: white;
}
.badge-primary { background-color: #1e3a8a !important; }
.badge-info { background-color: #0284c7 !important; }
.badge-success { background-color: #16a34a !important; }
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
.stock-info {
    font-size: 0.85rem;
    color: #6b7280;
    font-style: italic;
}
.alert-success {
    background-color: #22c55e;
    color: white;
}
.alert-danger {
    background-color: #ef4444;
    color: white;
}
.app-sidebar,
.app-sidebar a {
    color: white !important;
}
.btn-confirm.bg-red-600:hover {
    background-color: #dc2626;
}
</style>

<div class="header-card flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
    <div>
        <h1 class="text-3xl font-bold flex items-center gap-2">
            <i class="fas fa-edit text-white text-3xl"></i>
            Request Confirmation
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

<c:if test="${isAlreadyUpdated}">
    <div class="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 my-4">
        <p><strong>Note:</strong> This request has already been updated and cannot be modified again.</p>
    </div>
</c:if>

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

<!-- Main Form -->
<div class="content-card">
    <form method="post" action="RequestUpdateServlet" id="updateForm">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="totalItems" value="${fn:length(requestItems)}" />

        <c:if test="${not showOnlyBack}">
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
                        <tr class="hover:bg-gray-50">
                            <!-- Request Type -->
                            <td class="p-3">
                                <span class="text-black px-3 py-1 rounded-lg font-semibold shadow-sm inline-block">
                                    ${item.requestTypeName}
                                </span>
                            </td>

                            <!-- Material Name -->
                            <td class="p-3">
                                <strong class="text-gray-800">${item.materialName}</strong>
                                <input type="hidden" name="materialId_${status.index}" value="${item.materialId}" />
                            </td>

                            <!-- Requested Quantity -->
                            <td class="p-3 text-center">
                                <span class="bg-sky-600 text-white px-3 py-1 rounded-lg font-semibold shadow-sm inline-block">
                                    ${item.quantity}
                                </span>
                            </td>

                            <!-- Actual Quantity (input) -->
                            <td class="p-3 text-center">
                                <c:choose>
                                    <c:when test="${isAlreadyUpdated}">
                                        <input type="number"
                                               class="border border-gray-300 bg-gray-100 rounded-md px-3 py-1 w-24 text-center"
                                               name="actualQuantity_${status.index}"
                                               value="${item.actualQuantity}"
                                               disabled />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="number"
                                               class="border border-gray-300 rounded-md px-3 py-1 w-24 text-center focus:outline-none focus:ring-2 focus:ring-blue-600"
                                               name="actualQuantity_${status.index}"
                                               value="${item.actualQuantity}"
                                               min="0"
                                               max="${item.stockQuantity}"
                                               required />
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <!-- Stock Quantity -->
                            <td class="p-3 text-center">
                                <span class="bg-green-600 text-white px-3 py-1 rounded-lg font-semibold shadow-sm inline-block">
                                    ${item.stockQuantity}
                                </span>
                                <div class="text-xs text-gray-500 italic mt-1">Available in stock</div>
                            </td>

                            <!-- Note -->
                            <td class="p-3 text-gray-600 italic">
                                <c:choose>
                                    <c:when test="${not empty item.note}">
                                        ${item.note}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-gray-400">No notes</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        </c:if>

        <div class="form-actions">
            <a onclick="history.back()" class="btn-back">
                <i class="fas fa-arrow-left me-2"></i>Back to Requests
            </a>
        
            <c:choose>
                <c:when test="${isAlreadyUpdated}">
                    <button type="button" class="btn btn-confirm opacity-50 cursor-not-allowed" disabled>
                        <i class="fas fa-ban me-2"></i>Already Updated
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-confirm" onclick="return confirmUpdate()">
                        <i class="fas fa-check me-2"></i>Confirm Request
                    </button>
                </c:otherwise>
            </c:choose>
            <c:if test="${isAlreadyUpdated}">
    <form method="post" action="RequestUpdateServlet" onsubmit="return confirm('Are you sure you want to revert this request?')">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="mode" value="revert" />
        <button type="submit" class="btn-confirm bg-red-600 hover:bg-red-700">
            <i class="fas fa-undo-alt me-2"></i>Revert Confirmation
        </button>
    </form>
</c:if>
        </div>
    </form>
</div>

<!-- JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script>
function confirmUpdate() {
    return confirm("Are you sure you want to update the actual quantities?");
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('input[name^="actualQuantity_"]').forEach(function (input) {
        input.addEventListener("input", function () {
            const max = parseInt(this.getAttribute("max"));
            const value = parseInt(this.value);
            if (value > max) {
                this.setCustomValidity("Actual quantity cannot exceed stock quantity (" + max + ")");
            } else {
                this.setCustomValidity("");
            }
        });
    });
});
</script>
