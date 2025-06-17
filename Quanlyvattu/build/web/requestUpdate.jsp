<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Request - Warehouse Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .main-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .header-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .header-card h1 {
            margin: 0;
            font-size: 2.2rem;
            font-weight: 600;
        }
        
        .header-card p {
            margin: 10px 0 0 0;
            opacity: 0.9;
            font-size: 1.1rem;
        }
        
        .content-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }
        
        .table-container {
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        
        .table {
            margin: 0;
        }
        
        .table thead th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-size: 0.9rem;
        }
        
        .table tbody td {
            padding: 15px;
            vertical-align: middle;
            border-color: #e9ecef;
        }
        
        .table tbody tr:hover {
            background-color: #f8f9fa;
            transition: background-color 0.3s ease;
        }
        
        .badge {
            padding: 8px 12px;
            font-size: 0.8rem;
            font-weight: 500;
            border-radius: 20px;
        }
        
        .badge-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .badge-info {
            background: linear-gradient(135deg, #2196F3 0%, #21CBF3 100%);
        }
        
        .badge-success {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
        }
        
        .form-control {
            border-radius: 8px;
            border: 2px solid #e9ecef;
            padding: 10px 15px;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .btn-confirm {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
        }
        
        .btn-confirm:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
            color: white;
        }
        
        .btn-back {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-back:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(108, 117, 125, 0.4);
            color: white;
            text-decoration: none;
        }
        
        .alert {
            border-radius: 10px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
            color: #155724;
        }
        
        .alert-danger {
            background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
            color: #721c24;
        }
        
        .quantity-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .stock-info {
            font-size: 0.85rem;
            color: #6c757d;
            font-style: italic;
        }
        
        .form-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
        }
        
        @media (max-width: 768px) {
            .main-container {
                padding: 10px;
            }
            
            .header-card, .content-card {
                padding: 20px;
            }
            
            .table-responsive {
                font-size: 0.9rem;
            }
            
            .form-actions {
                flex-direction: column;
            }
            
            .btn-confirm, .btn-back {
                width: 100%;
                margin-bottom: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="main-container">
        <!-- Header -->
        <div class="header-card">
            <h1><i class="fas fa-edit me-3"></i>Update Request</h1>
            <p>Update actual quantities for request items</p>
        </div>
        
        <!-- Alert Messages -->
        <c:if test="${not empty message}">
            <div class="alert ${messageType eq 'success' ? 'alert-success' : 'alert-danger'} alert-dismissible fade show" role="alert">
                <c:choose>
                    <c:when test="${messageType eq 'success'}">
                        <i class="fas fa-check-circle me-2"></i>
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-exclamation-triangle me-2"></i>
                    </c:otherwise>
                </c:choose>
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <!-- Main Content -->
        <div class="content-card">
            <form method="post" action="RequestUpdateServlet" id="updateForm">
                <input type="hidden" name="requestId" value="${requestId}">
                
                <div class="table-container">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th><i class="fas fa-tag me-2"></i>Request Type</th>
                                    <th><i class="fas fa-box me-2"></i>Material Name</th>
                                    <th><i class="fas fa-hashtag me-2"></i>Requested Qty</th>
                                    <th><i class="fas fa-clipboard-check me-2"></i>Actual Quantity</th>
                                    <th><i class="fas fa-warehouse me-2"></i>Stock</th>
                                    <th><i class="fas fa-sticky-note me-2"></i>Note</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${requestItems}" varStatus="status">
                                    <tr>
                                        <td>
                                            <span class="badge badge-primary">
                                                ${item.requestTypeName}
                                            </span>
                                        </td>
                                        <td>
                                            <strong>${item.materialName}</strong>
                                            <input type="hidden" name="materialId" value="${item.materialId}">
                                        </td>
                                        <td>
                                            <span class="badge badge-info">${item.quantity}</span>
                                        </td>
                                        <td>
                                            <div class="quantity-info">
                                                <input type="number" 
                                                       class="form-control" 
                                                       name="actualQuantity" 
                                                       value="${item.actualQuantity}" 
                                                       min="0" 
                                                       max="${item.stockQuantity}"
                                                       style="width: 100px;"
                                                       required>
                                            </div>
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
                        <i class="fas fa-check me-2"></i>Confirm Request
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmUpdate() {
            return confirm('Are you sure you want to update the actual quantities for this request?');
        }
        
        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
        
        // Validate quantity inputs
        document.querySelectorAll('input[name="actualQuantity"]').forEach(function(input) {
            input.addEventListener('input', function() {
                const max = parseInt(this.getAttribute('max'));
                const value = parseInt(this.value);
                
                if (value > max) {
                    this.setCustomValidity('Actual quantity cannot exceed stock quantity (' + max + ')');
                } else {
                    this.setCustomValidity('');
                }
            });
        });
    </script>
</body>
</html>