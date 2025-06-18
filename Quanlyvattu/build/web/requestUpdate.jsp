<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Re-include Bootstrap and FontAwesome (in case layout doesn't) -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

<style>
body {
    background-color: #f9fafb;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
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
.badge-primary {
    background-color: #1e3a8a !important;
}
.badge-info {
    background-color: #0284c7 !important;
}
.badge-success {
    background-color: #16a34a !important;
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
</style>

<div class="main-container">
    <!-- Header -->
    <div class="header-card">
        <h1><i class="fas fa-edit me-2"></i>Update Request</h1>
        <p>Update actual quantities for request items</p>
    </div>

    <!-- Alert Message -->
    <c:if test="${not empty message}">
        <div class="alert ${messageType eq 'success' ? 'alert-success' : 'alert-danger'}">
            <c:choose>
                <c:when test="${messageType eq 'success'}">
                    <i class="fas fa-check-circle me-2"></i>
                </c:when>
                <c:otherwise>
                    <i class="fas fa-exclamation-triangle me-2"></i>
                </c:otherwise>
            </c:choose>
            ${message}
        </div>
    </c:if>

    <!-- Main Form -->
    <div class="content-card">
        <form method="post" action="RequestUpdateServlet" id="updateForm">
            <input type="hidden" name="requestId" value="${requestId}" />
            <input type="hidden" name="totalItems" value="${fn:length(requestItems)}" />

            <div class="table-container">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Request Type</th>
                                <th>Material Name</th>
                                <th>Requested Qty</th>
                                <th>Actual Quantity</th>
                                <th>Stock</th>
                                <th>Note</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${requestItems}" varStatus="status">
                                <tr>
                                    <td><span class="badge badge-primary">${item.requestTypeName}</span></td>
                                    <td>
                                        <strong>${item.materialName}</strong>
                                        <input type="hidden" name="materialId_${status.index}" value="${item.materialId}" />
                                    </td>
                                    <td><span class="badge badge-info">${item.quantity}</span></td>
                                    <td>
                                        <input type="number"
                                               class="form-control"
                                               name="actualQuantity_${status.index}"
                                               value="${item.actualQuantity}"
                                               min="0"
                                               max="${item.stockQuantity}"
                                               style="width: 100px;"
                                               required />
                                    </td>
                                    <td>
                                        <span class="badge badge-success">${item.stockQuantity}</span>
                                        <div class="stock-info">Available in stock</div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty item.note}">
                                                <span class="text-muted">${item.note}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted fst-italic">No notes</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="form-actions">
                <a href="reqlist" class="btn-back">
                    <i class="fas fa-arrow-left me-2"></i>Back to Requests
                </a>
                <button type="submit" class="btn btn-confirm" onclick="return confirmUpdate()">
                    <i class="fas fa-check me-2"></i>Save
                </button>
            </div>
        </form>
    </div>
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
