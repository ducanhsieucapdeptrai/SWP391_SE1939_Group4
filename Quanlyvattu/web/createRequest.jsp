<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- This page is included in layout.jsp, using Tailwind CSS from layout -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/request.js"></script>

<!-- Messages -->
<c:if test="${not empty error}">
  <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${message}</div>
</c:if>

<div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
  <!-- Debug info removed - issue resolved -->
  
  <!-- Header with Back button -->
  <div class="flex justify-between items-center mb-6">
    <h2 class="text-xl font-semibold text-gray-800">Create Request</h2>
    <a href="javascript:history.back()" class="text-blue-600 hover:text-blue-800 flex items-center">
      <i class="fas fa-arrow-left mr-2"></i> Back
    </a>
  </div>
  
  <form id="frmRequest" action="createrequest" method="post">
    <!-- Request Type & SubType -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Request Type:</label>
        <!-- Debug: Types count = ${fn:length(types)} -->
        <select id="typeId" name="typeId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="">--Select Type--</option>
          <c:forEach var="t" items="${types}">
            <c:if test="${t.requestTypeId!=2&&t.requestTypeId!=4}">
              <option value="${t.requestTypeId}">${t.requestTypeName}</option>
            </c:if>
          </c:forEach>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">SubType:</label>
        <select id="subTypeId" name="subTypeId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="">--Select--</option>
        </select>
      </div>
    </div>

    <!-- Filter Materials Panel -->
    <div class="bg-gray-50 border border-gray-200 rounded-lg p-4 mb-6">
      <h3 class="text-lg font-medium text-gray-800 mb-4">Filter Materials</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Category:</label>
          <select id="catId" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="">--All--</option>
            <c:forEach var="c" items="${categories}">
              <option value="${c.categoryId}">${c.categoryName}</option>
            </c:forEach>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">SubCategory:</label>
          <select id="subCatId" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="">--All--</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Material:</label>
          <select id="materialId" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="">--Select--</option>
          </select>
        </div>
      </div>
      <button type="button" id="btnFilter" class="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md">Reset Filter</button>
    </div>

    <!-- Stock Info & Quantity -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end mb-6">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Stock:</label>
        <input type="text" id="stock" readonly class="w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md">
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Min Quantity:</label>
        <input type="text" id="minQty" readonly class="w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md">
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Quantity:</label>
        <input type="number" id="quantity" min="1" value="1" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
      </div>
      <div>
        <button type="button" id="btnAdd" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md w-full">Add Material</button>
      </div>
    </div>

    <!-- Selected Materials Table -->
    <div class="mb-6">
      <h3 class="text-lg font-medium text-gray-800 mb-4">Selected Materials</h3>
      <div class="overflow-x-auto border border-gray-200 rounded-lg">
        <table id="tblItems" class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">STT</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Image</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <!-- Dynamic content will be added here -->
          </tbody>
        </table>
      </div>
    </div>
    <!-- Project -->
    <div class="mb-6">
      <label class="block text-sm font-medium text-gray-700 mb-2">Project:</label>
      <select name="projectId" id="projectId"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
        <option value="">-- No Project --</option>
        <c:forEach var="p" items="${projectList}">
          <option value="${p.projectId}">${p.projectName}</option>
        </c:forEach>
      </select>
    </div>
    <!-- Note -->
    <div class="mb-6">
      <label class="block text-sm font-medium text-gray-700 mb-2">Note:</label>
      <textarea name="note" id="note" rows="3" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 resize-vertical"></textarea>
    </div>

    <!-- Submit Button -->
    <div class="text-center">
      <button type="submit" id="btnCreate" class="bg-blue-500 hover:bg-blue-600 text-white px-8 py-3 rounded-md font-medium">Create Request</button>
    </div>
  </form>
</div>

<c:if test="${not empty error}">
<script>
$(document).ready(function() {
  // Show error message and auto-hide
  setTimeout(function() {
    $('.bg-red-100').fadeOut('slow');
  }, 5000);
});
</script>
</c:if>

<c:if test="${not empty message}">
<script>
$(document).ready(function() {
  // Reset form after showing success message
  if (typeof resetFormToInitialState === 'function') {
    resetFormToInitialState();
  }
  
  // Auto-hide success message
  setTimeout(function() {
    $('.bg-green-100').fadeOut('slow');
  }, 3000);
});
</script>
</c:if>
