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
 

  <c:if test="${not empty error}">
    <div class="message error">${error}</div>
  </c:if>
  <c:if test="${not empty message}">
    <div class="message success">${message}</div>
  </c:if>
    
    <div class="border border-gray-200 rounded-lg shadow-sm bg-white p-6">
        
        
  <form id="frmRequest" action="createrequest" method="post">
    <!-- Request Type & SubType -->
    <div class="form-group" style="display: flex; gap: 20px; margin-bottom: 20px;">
      <div>
        <label style="display: block; margin-bottom: 5px;">Request Type:</label>
        <select id="typeId" name="typeId" required style="width: 200px;">
          <option class="text-center" value="">--Select Type--</option>
          <c:forEach var="t" items="${types}">
              <c:if test="${t.requestTypeId!=2&&t.requestTypeId!=4}">
            <option value="${t.requestTypeId}">${t.requestTypeName}</option>
              </c:if>
          </c:forEach>
        </select>
      </div>
      <div>
        <label style="display: block; margin-bottom: 5px;">SubType:</label>
        <select id="subTypeId" name="subTypeId" required style="width: 200px;">
          <option class="text-center" value="">--Select--</option>
        </select>
      </div>
        <div class="ml-auto">
            
        <a href="${pageContext.request.contextPath}/replit" style="text-decoration: none; color: black; ">&lt; Back</a>
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
