<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Request Detail - Material Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .material-image {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
        }
        .back-button {
            transition: all 0.3s ease;
        }
        .back-button:hover {
            transform: translateX(-5px);
        }
        .material-card {
            transition: transform 0.2s ease;
        }
        .material-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .info-section {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .action-buttons {
            position: sticky;
            bottom: 20px;
            z-index: 1000;
        }
        .btn-approve {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            color: white;
            transition: all 0.3s ease;
        }
        .btn-approve:hover {
            background: linear-gradient(45deg, #218838, #1ba085);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.4);
            color: white;
        }
        .btn-reject {
            background: linear-gradient(45deg, #dc3545, #e74c3c);
            border: none;
            color: white;
            transition: all 0.3s ease;
        }
        .btn-reject:hover {
            background: linear-gradient(45deg, #c82333, #dc2c2e);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(220, 53, 69, 0.4);
            color: white;
        }
        .modal-header.bg-success {
            background-color: #28a745 !important;
        }
        .modal-header.bg-danger {
            background-color: #dc3545 !important;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container-fluid py-4">
        <!-- Header -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h1 class="h3 mb-0">
                            <i class="fas fa-file-alt me-2"></i>
                            Request Detail 
                        </h1>
                        <p class="text-muted mb-0">View detailed information about this request</p>
                    </div>
                    <button onclick="history.back()" class="btn btn-outline-secondary back-button">
                        <i class="fas fa-arrow-left me-2"></i>
                        Back to Requests
                    </button>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="card shadow-sm">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-list me-2"></i>
                            Requested Materials
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty requestDetails}">
                                <div class="row">
                                    <c:set var="totalValue" value="0" />
                                    <c:forEach var="detail" items="${requestDetails}" varStatus="status">
                                        <c:set var="totalValue" value="${totalValue + detail.totalValue}" />
                                        <div class="col-md-6 col-lg-4 mb-3">
                                            <div class="card material-card h-100">
                                                <div class="card-body">
                                                    <div class="d-flex align-items-start">
                                                        <c:choose>
                                                            <c:when test="${not empty detail.image}">
                                                                <img src="${detail.image}" alt="${detail.materialName}" 
                                                                     class="material-image me-3" 
                                                                     onerror="this.src='assets/images/no-image.png'">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="material-image me-3 bg-light d-flex align-items-center justify-content-center">
                                                                    <i class="fas fa-image text-muted"></i>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div class="flex-grow-1">
                                                            <h6 class="card-title mb-1">${detail.materialName}</h6>
                                                            <p class="card-text text-muted small mb-2">
                                                                ${detail.categoryName} / ${detail.subCategoryName}
                                                            </p>
                                                            <div class="row text-sm">
                                                                <div class="col-6">
                                                                    <strong>Quantity:</strong><br>
                                                                    <span class="text-primary h6">${detail.quantity}</span>
                                                                </div>
                                                                <div class="col-6">
                                                                    <strong>Unit Price:</strong><br>
                                                                    <span class="text-success">
                                                                        <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$"/>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                            <hr class="my-2">
                                                            <div class="text-center">
                                                                <strong>Total Value:</strong>
                                                                <span class="text-success h6">
                                                                    <fmt:formatNumber value="${detail.totalValue}" type="currency" currencySymbol="$"/>
                                                                </span>
                                                            </div>
                                                            <c:if test="${not empty detail.description}">
                                                                <p class="card-text text-muted small mt-2">
                                                                    <i class="fas fa-info-circle me-1"></i>
                                                                    ${detail.description}
                                                                </p>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                
                                
                                <div class="row mt-4">
                                    <div class="col-12">
                                        <div class="card bg-light">
                                            <div class="card-body">
                                                <div class="row text-center">
                                                    <div class="col-md-4">
                                                        <h5 class="text-primary">
                                                            <i class="fas fa-boxes me-2"></i>
                                                            Total Items
                                                        </h5>
                                                        <h4 class="mb-0">${requestDetails.size()}</h4>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <h5 class="text-info">
                                                            <i class="fas fa-calculator me-2"></i>
                                                            Total Quantity
                                                        </h5>
                                                        <h4 class="mb-0">
                                                            <c:set var="totalQuantity" value="0" />
                                                            <c:forEach var="detail" items="${requestDetails}">
                                                                <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}" />
                                                            </c:forEach>
                                                            ${totalQuantity}
                                                        </h4>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <h5 class="text-success">
                                                            <i class="fas fa-dollar-sign me-2"></i>
                                                            Total Value
                                                        </h5>
                                                        <h4 class="mb-0">
                                                            <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol="$"/>
                                                        </h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                
                                <div class="row mt-4 action-buttons">
                                    <div class="col-12">
                                        <div class="card shadow-lg">
                                            <div class="card-body py-3">
                                                <div class="d-flex justify-content-center gap-3">
                                                    <button type="button" class="btn btn-approve btn-lg px-5" 
                                                            data-bs-toggle="modal" data-bs-target="#approveModal">
                                                        <i class="fas fa-check-circle me-2"></i>
                                                        Approve Request
                                                    </button>
                                                    <button type="button" class="btn btn-reject btn-lg px-5" 
                                                            data-bs-toggle="modal" data-bs-target="#rejectModal">
                                                        <i class="fas fa-times-circle me-2"></i>
                                                        Reject Request
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No materials found in this request</h5>
                                    <c:if test="${not empty message}">
                                        <p class="text-muted">${message}</p>
                                    </c:if>
                                    <c:if test="${empty message}">
                                        <p class="text-muted">This request doesn't contain any material details.</p>
                                    </c:if>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    
    <div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="approveModalLabel">
                        <i class="fas fa-check-circle me-2"></i>
                        Approve Request
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="request-detail" method="post">
                    <div class="modal-body">
                        <div class="text-center mb-3">
                            <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                            <h5>Confirm Request Approval</h5>
                            <p class="text-muted">Are you sure you want to approve this request?</p>
                        </div>
                        <div class="mb-3">
                            <label for="approveNote" class="form-label">Approval Note (Optional)</label>
                            <textarea class="form-control" id="approveNote" name="note" rows="3" 
                                     placeholder="Add any notes about this approval..."></textarea>
                        </div>
                        <input type="hidden" name="requestId" value="${requestId}">
                        <input type="hidden" name="action" value="approve">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-2"></i>Approve Request
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    
    <div class="modal fade" id="rejectModal" tabindex="-1" aria-labelledby="rejectModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="rejectModalLabel">
                        <i class="fas fa-times-circle me-2"></i>
                        Reject Request
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="request-detail" method="post">
                    <div class="modal-body">
                        <div class="text-center mb-3">
                            <i class="fas fa-times-circle fa-3x text-danger mb-3"></i>
                            <h5>Confirm Request Rejection</h5>
                            <p class="text-muted">Are you sure you want to reject this request?</p>
                        </div>
                        <div class="mb-3">
                            <label for="rejectReason" class="form-label">Reason for Rejection <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="rejectReason" name="reason" rows="3" 
                                     placeholder="Please provide a reason for rejecting this request..." required></textarea>
                        </div>
                        <input type="hidden" name="requestId" value="${requestId}">
                        <input type="hidden" name="action" value="reject">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-ban me-2"></i>Reject Request
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    
    <c:if test="${not empty successMessage}">
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
            <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header bg-success text-white">
                    <i class="fas fa-check-circle me-2"></i>
                    <strong class="me-auto">Success</strong>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                    ${successMessage}
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
            <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header bg-danger text-white">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <strong class="me-auto">Error</strong>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                    ${errorMessage}
                </div>
            </div>
        </div>
    </c:if>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        
        document.addEventListener('DOMContentLoaded', function() {
            var toasts = document.querySelectorAll('.toast');
            toasts.forEach(function(toast) {
                var bsToast = new bootstrap.Toast(toast, { delay: 5000 });
                bsToast.show();
            });
        });
    </script>
</body>
</html>
