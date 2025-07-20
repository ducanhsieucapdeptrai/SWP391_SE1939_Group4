<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Create Request</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <link rel="stylesheet" href="assets/css/style.css">
  <script src="assets/js/request.js"></script>
  <style>
    .top-bar { 
        display: flex; 
        justify-content: space-between; 
        align-items: center; 
        margin-bottom: 20px;
        padding: 10px;
        background: #f5f5f5;
    }
    .filter-panel { 
        margin: 20px 0;
        padding: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
    }
    .filter-panel select, .filter-panel input { 
        margin-right: 10px;
        padding: 5px;
        min-width: 150px;
    }
    table { 
        width: 100%; 
        border-collapse: collapse; 
        margin-top: 10px; 
    }
    table, th, td { 
        border: 1px solid #ccc; 
    }
    th { 
        background: #f5f5f5;
        padding: 10px 8px;
    }
    td { 
        padding: 8px; 
        text-align: left; 
    }
    .scroll-box { 
        max-height: 200px; 
        overflow-y: auto;
        border: 1px solid #ddd;
        border-radius: 5px;
        margin: 10px 0;
    }
    .action-btn { 
        margin-right: 5px;
        padding: 5px 10px;
        background: #dc3545;
        color: white;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }
    #btnAdd, #btnCreate {
        background: #007bff;
        color: white;
        padding: 8px 15px;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }
    #btnFilter {
        background: #6c757d;
        color: white;
        padding: 5px 10px;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }
    input[type="number"], input[type="text"] {
        padding: 5px;
        border: 1px solid #ddd;
        border-radius: 3px;
    }
    textarea {
        border: 1px solid #ddd;
        border-radius: 3px;
        padding: 8px;
    }
    .form-group {
        margin-bottom: 15px;
    }
    .error {
        color: #dc3545;
        margin-top: 5px;
    }
    .message {
        padding: 10px;
        margin: 10px 0;
        border-radius: 3px;
    }
    .success {
        background: #d4edda;
        color: #155724;
    }
    .error {
        background: #f8d7da;
        color: #721c24;
    }
  </style>
</head>
<body>
 

                <!-- Form -->
                <form method="post" action="createrequest" class="space-y-6">
                    <!-- Request Type -->
                    <div>
                        <label class="block font-medium mb-1">Request Type <span class="text-red-500">*</span></label>
                        <select name="requestTypeId" required class="w-full border border-gray-300 rounded p-2" onchange="toggleStockCheck()">
                            <option value="">-- Select a type --</option>
                            <c:forEach var="type" items="${requestTypes}">
                                 <c:if test="${type.requestTypeId != 2}">
                                    <option value="${type.requestTypeId}">${type.requestTypeName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- Project -->
                    <div>
                        <label class="block font-medium mb-1">Project</label>
                        <select name="projectId" class="w-full border border-gray-300 rounded p-2">
                            <option value="">-- None --</option>
                            <c:forEach var="p" items="${projects}">
                                <option value="${p.projectId}">${p.projectName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- Category & SubCategory Filters -->
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block font-medium mb-1">Category</label>
                            <select id="categoryFilter" class="w-full border border-gray-300 rounded p-2">
                                <option value="">All Categories</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}">${cat.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label class="block font-medium mb-1">Sub‑Category</label>
                            <select id="subCategoryFilter" class="w-full border border-gray-300 rounded p-2">
                                <option value="">All Sub‑Categories</option>
                                <c:forEach var="sc" items="${subCategories}">
                                    <option value="${sc.subCategoryId}" data-cat="${sc.categoryId}">
                                        ${sc.subCategoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Material List -->
                    <div id="materialList" class="space-y-4">
                        <div class="material-item grid grid-cols-1 md:grid-cols-12 gap-4 items-end">
                            <div class="md:col-span-6">
                                <label class="block font-medium mb-1">Material <span class="text-red-500">*</span></label>
                                <select name="materialId" class="form-select w-full border border-gray-300 rounded p-2" required>
                                    <option value="">-- Select material --</option>
                                    <c:forEach var="m" items="${materials}">
                                        <option value="${m.materialId}"
                                                data-sub="${m.subCategoryId}"
                                                data-stock="${m.quantity}">
                                            ${m.materialName} (In stock: ${m.quantity})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="md:col-span-4">
                                <label class="block font-medium mb-1">Quantity</label>
                                <input type="number" name="quantity" class="w-full border border-gray-300 rounded p-2 quantity-input" min="1" required placeholder="Enter quantity" oninput="validateQuantity(this)">
                                <small class="text-gray-500 stock-info"></small>
                            </div>
                            <div class="md:col-span-2">
                                <button type="button" class="remove-material hidden text-white bg-red-500 hover:bg-red-600 rounded px-3 py-2 text-sm w-full">Remove</button>
                            </div>
                        </div>
                    </div>

                    <!-- Add Material Button -->
                    <button type="button" onclick="addMaterial()" class="bg-gray-200 text-gray-800 hover:bg-gray-300 rounded px-4 py-2 text-sm">
                        + Add another material
                    </button>

                    <!-- Note -->
                    <div>
                        <label class="block font-medium mb-1 mt-3">Note (optional)</label>
                        <textarea name="note" rows="3" class="w-full border border-gray-300 rounded p-2" placeholder="Reason for request..."></textarea>
                    </div>

                    <!-- Buttons -->
                    <div class="flex justify-between items-center mt-4">
                        <a href="javascript:history.back()" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded">Back</a>
                        <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded" onclick="return validateForm()">Submit Request</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Filter panel -->
    <fieldset class="filter-panel">
      <legend>Filter Materials</legend>
      <label>Category:
        <select id="catId">
          <option class="text-center" value="">--All--</option>
          <c:forEach var="c" items="${categories}">
            <option value="${c.categoryId}">${c.categoryName}</option>
          </c:forEach>
        </select>
      </label>
      <label>SubCategory:
        <select id="subCatId">
          <option class="text-center" value="">--All--</option>
        </select>
      </label>
      <label>Material:
        <select id="materialId">
          <option class="text-center" value="">--Select--</option>
        </select>
      </label>
      <button type="button" id="btnFilter">Reset Filter</button>
    </fieldset>

    <!-- Stock / MinQuantity / Quantity -->
    <div style="display: flex; gap: 15px; align-items: center; margin: 20px 0;">
      <div>
        <label for="stock" style="display: block; margin-bottom: 5px;">Stock:</label>
        <input type="text" id="stock" readonly style="background: #f5f5f5;" />
      </div>
      <div>
        <label for="minQty" style="display: block; margin-bottom: 5px;">Min Quantity:</label>
        <input type="text" id="minQty" readonly style="background: #f5f5f5;" />
      </div>
      <div>
        <label for="quantity" style="display: block; margin-bottom: 5px;">Quantity:</label>
        <input type="number" id="quantity" min="1" value="1" style="width: 100px;" />
      </div>
      <div style="align-self: flex-end;">
        <button type="button" id="btnAdd">Add Material</button>
      </div>
    </div>

    <!-- Selected Items -->
    <h4>Selected Materials</h4>
    <div class="scroll-box">
      <table id="tblItems">
        <thead>
          <tr>
            <th>STT</th>
            <th>Image</th>
            <th>Name</th>
            <th>Quantity</th>
            <th style="min-width: 150px;">Action</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>

    <!-- Note and Create -->
    <div style="margin: 20px 0;">
      <label for="note" style="display: block; margin-bottom: 5px;">Note:</label>
      <textarea name="note" id="note" rows="3" style="width:100%; resize: vertical;"></textarea>
    </div>

    <div style="margin-top: 20px; text-align: center;">
      <button type="submit" id="btnCreate" style="min-width: 150px;">Create Request</button>
    </div>
  </form>
    </div>
  <script>
    // Show error message if request failed
    <c:if test="${not empty error}">
      $(document).ready(function() {
        setTimeout(function() {
          $('.error').fadeOut('slow');
        }, 5000);
      });
    </c:if>
    
    // Show success message
    <c:if test="${not empty message}">
      $(document).ready(function() {
        setTimeout(function() {
          $('.success').fadeOut('slow');
        }, 3000);
      });
    </c:if>
  </script>
</body>
</html>
