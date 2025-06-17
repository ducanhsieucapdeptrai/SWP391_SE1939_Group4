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

        <!-- Request Details Card -->
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
                                
                                <!-- Summary -->
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>