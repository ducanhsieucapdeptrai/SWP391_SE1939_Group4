<%-- 
    Document   : createRequest
    Created on : Jun 13, 2025, 3:42:26 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CreateRequestByStaff</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Create Material Request</h4>
                    </div>
                    <div class="card-body">
                        <!-- Display notifications -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form method="post" action="createrequest">
                            <!-- Request Type -->
                            <div class="mb-3">
                                <label for="requestTypeId" class="form-label">Request Type <span class="text-danger">*</span></label>
                                <select class="form-select" id="requestTypeId" name="requestTypeId" required>
                                    <option value="">-- Select request type --</option>
                                    <c:forEach var="type" items="${requestTypes}">
                                        <option value="${type.requestTypeId}">${type.requestTypeName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Material List -->
                            <div class="mb-3">
                                <label class="form-label">Material List <span class="text-danger">*</span></label>
                                <div id="materialList">
                                    <div class="material-item border p-3 mb-2 rounded">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label">Material</label>
                                                <select class="form-select" name="materialId" required>
                                                    <option value="">-- Select material --</option>
                                                    <c:forEach var="material" items="${materials}">
                                                        <option value="${material.materialId}" 
                                                                data-price="${material.price}" 
                                                                data-stock="${material.quantity}">
                                                            ${material.materialName} (In stock: ${material.quantity})
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-4">
                                                <label class="form-label">Quantity</label>
                                                <input type="number" class="form-control" name="quantity" 
                                                       min="1" required placeholder="Enter quantity">
                                            </div>
                                            <div class="col-md-2 d-flex align-items-end">
                                                <button type="button" class="btn btn-danger btn-sm remove-material" 
                                                        onclick="removeMaterial(this)">Remove</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <button type="button" class="btn btn-secondary btn-sm" onclick="addMaterial()">
                                    + Add Material
                                </button>
                            </div>

                            <!-- Note -->
                            <div class="mb-3">
                                <label for="note" class="form-label">Note</label>
                                <textarea class="form-control" id="note" name="note" rows="4" 
                                          placeholder="Enter notes, reason for request..."></textarea>
                            </div>

                            <!-- Buttons -->
                            <div class="d-flex justify-content-between">
                                <a href="dashboard.jsp" class="btn btn-secondary">Back</a>
                                <button type="submit" class="btn btn-primary">
                                    Submit Request to Director
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function addMaterial() {
            const materialList = document.getElementById('materialList');
            const newItem = document.querySelector('.material-item').cloneNode(true);
            
            // Reset values
            newItem.querySelectorAll('select, input').forEach(element => {
                element.value = '';
            });
            
            materialList.appendChild(newItem);
            updateRemoveButtons();
        }
        
        function removeMaterial(button) {
            const materialItems = document.querySelectorAll('.material-item');
            if (materialItems.length > 1) {
                button.closest('.material-item').remove();
            } else {
                alert('At least one material is required!');
            }
            updateRemoveButtons();
        }
        
        function updateRemoveButtons() {
            const materialItems = document.querySelectorAll('.material-item');
            const removeButtons = document.querySelectorAll('.remove-material');
            
            removeButtons.forEach(button => {
                button.style.display = materialItems.length > 1 ? 'inline-block' : 'none';
            });
        }
        
        // Initialize
        updateRemoveButtons();
        
        // Validate quantity based on stock
        document.addEventListener('change', function(e) {
            if (e.target.name === 'materialId') {
                const option = e.target.selectedOptions[0];
                const stock = option.getAttribute('data-stock');
                const quantityInput = e.target.closest('.material-item').querySelector('input[name="quantity"]');
                
                if (stock) {
                    quantityInput.setAttribute('max', stock);
                    quantityInput.setAttribute('title', `Maximum: ${stock}`);
                }
            }
        });
    </script>
</body>
</html>
