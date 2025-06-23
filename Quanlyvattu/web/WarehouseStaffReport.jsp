<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="https://cdn.tailwindcss.com"></script>
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<!-- WarehouseStaffReport Content -->
<div class="max-w-7xl mx-auto">
    <!-- Header with Back Button and Title -->
    <div class="mb-6">
        <a href="approvedrequests" class="inline-flex items-center text-blue-600 hover:text-blue-800 mb-4">
            <i class="fas fa-arrow-left mr-2"></i> Back
        </a>
        <h1 class="text-3xl font-bold text-gray-900">Warehouse Staff Report</h1>
    </div>

    <!-- Action Buttons -->
    <div class="mb-6 flex flex-wrap gap-4">
        <a href="RequestUpdateServlet?requestId=${requestId} " class="inline-flex items-center px-4 py-2 bg-green-600 text-white font-medium rounded-lg hover:bg-green-700 transition duration-300">
            <i class="fas fa-edit mr-2"></i> Update Request
        </a>
        <button type="button" onclick="showSendToModal()" class="inline-flex items-center px-4 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition duration-300">
            <i class="fas fa-paper-plane mr-2"></i> Send To
        </button>
    </div>

    <!-- Request Information -->
    <div class="bg-white rounded-lg shadow-md mb-6">
        <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">Approved Request Information</h2>
        </div>
        <div class="p-6">
            <div class="overflow-x-auto">
                <table class="min-w-full">
                    <tbody class="divide-y divide-gray-200">
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900 w-48">Request ID:</td>
                            <td class="py-3 text-sm text-gray-700">${requestInfo.requestId}</td>
                        </tr>
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900">Request Type:</td>
                            <td class="py-3 text-sm text-gray-700">
                                <c:choose>
                                    <c:when test="${not empty requestInfo.requestTypeName}">
                                        ${requestInfo.requestTypeName}
                                    </c:when>
                                    <c:otherwise>
                                        Not specified
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900">Requested By:</td>
                            <td class="py-3 text-sm text-gray-700">
                                <c:choose>
                                    <c:when test="${not empty requestInfo.requestedByName}">
                                        ${requestInfo.requestedByName}
                                    </c:when>
                                    <c:otherwise>
                                        ID: ${requestInfo.requestedBy}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900">Request Date:</td>
                            <td class="py-3 text-sm text-gray-700">
                                <c:choose>
                                    <c:when test="${not empty requestInfo.requestDate}">
                                        <fmt:formatDate value="${requestInfo.requestDate}" pattern="dd/MM/yyyy" />
                                    </c:when>
                                    <c:otherwise>
                                        No information available
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900">Status:</td>
                            <td class="py-3">
                                <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium
                                      <c:choose>
                                          <c:when test="${requestInfo.status == 'Pending'}">bg-yellow-100 text-yellow-800</c:when>
                                          <c:when test="${requestInfo.status == 'Approved'}">bg-green-100 text-green-800</c:when>
                                          <c:when test="${requestInfo.status == 'Rejected'}">bg-red-100 text-red-800</c:when>
                                          <c:otherwise>bg-gray-100 text-gray-800</c:otherwise>
                                      </c:choose>">
                                    <c:choose>
                                        <c:when test="${not empty requestInfo.status}">
                                            ${requestInfo.status}
                                        </c:when>
                                        <c:otherwise>
                                            Not specified
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                        </tr>
                        <c:if test="${not empty requestInfo.approvedByName}">
                            <tr>
                                <td class="py-3 text-sm font-medium text-gray-900">Approved By:</td>
                                <td class="py-3 text-sm text-gray-700">${requestInfo.approvedByName}</td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty requestInfo.approvedDate}">
                            <tr>
                                <td class="py-3 text-sm font-medium text-gray-900">Approval Date:</td>
                                <td class="py-3 text-sm text-gray-700">
                                    <fmt:formatDate value="${requestInfo.approvedDate}" pattern="dd/MM/yyyy" />
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="py-3 text-sm font-medium text-gray-900">Notes:</td>
                            <td class="py-3 text-sm text-gray-700">
                                <c:choose>
                                    <c:when test="${not empty requestInfo.note}">
                                        ${requestInfo.note}
                                    </c:when>
                                    <c:otherwise>
                                        No notes
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:if test="${not empty requestInfo.approvalNote}">
                            <tr>
                                <td class="py-3 text-sm font-medium text-gray-900">Approval Notes:</td>
                                <td class="py-3 text-sm text-gray-700">${requestInfo.approvalNote}</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Requested Materials Details -->
    <div class="bg-white rounded-lg shadow-md mb-6">
        <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">Requested Materials Details</h2>
        </div>
        <div class="p-6">
            <c:choose>
                <c:when test="${not empty requestDetails}">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">No.</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Material ID</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Material Name</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Requested Quantity</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="detail" items="${requestDetails}" varStatus="status">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${status.count}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${detail.materialId}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty detail.materialName}">
                                                    ${detail.materialName}
                                                </c:when>
                                                <c:otherwise>
                                                    No name available
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <fmt:formatNumber value="${detail.quantity}" />
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-12">
                        <i class="fas fa-inbox text-4xl text-gray-400 mb-4"></i>
                        <p class="text-gray-500">No material details available.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    

<!-- ACTUAL RECORDS GROUPED BY IMPORT/EXPORT DATE -->
<div class="bg-white rounded-lg shadow-md mb-6">
  <div class="px-6 py-4 border-b border-gray-200">
    <h2 class="text-xl font-semibold text-gray-900">
      Actual ${requestInfo.requestTypeName} Records
    </h2>
  </div>
  <div class="p-6 overflow-x-auto">
    <c:set var="grandTotal" value="0"/>
    
    <c:forEach var="entry" items="${fn:contains(requestInfo.requestTypeName, 'Import') ? relatedImports : relatedExports}" varStatus="loop">
      <div class="mb-6 border border-gray-200 rounded-md shadow-sm">
        <div class="bg-orange-50 px-6 py-3 font-semibold text-gray-800 border-b border-orange-300 rounded-t-md">
          ${requestInfo.requestTypeName} ${loop.count} — 
          Date: <fmt:formatDate value="${entry.importDate != null ? entry.importDate : entry.exportDate}" pattern="dd/MM/yyyy" /> — 
          Type: ${entry.importTypeName != null ? entry.importTypeName : entry.exportTypeName}
        </div>

        <table class="min-w-full table-auto divide-y divide-gray-200">
          <thead class="bg-gray-100">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">No.</th>
              <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Material Name</th>
              <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Actual Quantity</th>
              <c:if test="${fn:contains(requestInfo.requestTypeName, 'Import')}">
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Unit Price</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Total</th>
              </c:if>
              <th class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Note</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <c:set var="index" value="1"/>
            <c:forEach var="detail" items="${fn:contains(requestInfo.requestTypeName, 'Import') ? relatedImportDetails : relatedExportDetails}">
              <c:if test="${(entry.importId != null && detail.importId == entry.importId) || (entry.exportId != null && detail.exportId == entry.exportId)}">
                <c:set var="lineTotal" value="${fn:contains(requestInfo.requestTypeName, 'Import') ? detail.quantity * detail.price : 0}" />
                <c:if test="${fn:contains(requestInfo.requestTypeName, 'Import')}">
                  <c:set var="grandTotal" value="${grandTotal + lineTotal}" />
                </c:if>
                <tr class="hover:bg-gray-50">
                  <td class="px-6 py-4 text-sm text-gray-800">${index}</td>
                  <td class="px-6 py-4 text-sm text-gray-800">${detail.materialName}</td>
                  <td class="px-6 py-4 text-sm text-gray-800">${detail.quantity}</td>
                  <c:if test="${fn:contains(requestInfo.requestTypeName, 'Import')}">
                    <td class="px-6 py-4 text-sm text-gray-800">
                      <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="₫"/>
                    </td>
                    <td class="px-6 py-4 text-sm text-gray-800">
                      <fmt:formatNumber value="${lineTotal}" type="currency" currencySymbol="₫"/>
                    </td>
                  </c:if>
                  <td class="px-6 py-4 text-sm text-gray-800">
                    <c:out value="${entry.note != null ? entry.note : '-'}"/>
                  </td>
                </tr>
                <c:set var="index" value="${index + 1}" />
              </c:if>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:forEach>

    <c:if test="${fn:contains(requestInfo.requestTypeName, 'Import')}">
      <div class="text-right mt-4 text-gray-900 font-semibold">
        Grand Total: <fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/>
      </div>
    </c:if>
  </div>
</div>


</div>

<!-- SendTo Modal -->
<div id="sendToModal" class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-lg bg-white">
        <div class="flex justify-between items-center pb-3 border-b">
            <h3 class="text-lg font-semibold text-gray-900">Send Report To</h3>
            <button onclick="closeSendToModal()" class="text-gray-400 hover:text-gray-600">
                <i class="fas fa-times text-xl"></i>
            </button>
        </div>

        <form id="sendToForm" action="sendreport" method="post" class="mt-4">
            <input type="hidden" name="requestId" value="${requestInfo.requestId}">

            <div class="mb-4">
                <label for="recipient" class="block text-sm font-medium text-gray-700 mb-2">Select Recipient:</label>
                <select name="recipient" id="recipient" required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                    <option value="">-- Select Recipient --</option>
                    <option value="director">Director</option>
                    <option value="staff">Company Staff</option>
                    <option value="warehouse_manager">Warehouse Manager</option>
                </select>
            </div>

            <div class="mb-6">
                <label for="message" class="block text-sm font-medium text-gray-700 mb-2">Message Content:</label>
                <textarea name="message" id="message" rows="4"
                          placeholder="Enter notification message (optional)..."
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-vertical"></textarea>
            </div>

            <div class="flex justify-end space-x-3">
                <button type="button" onclick="closeSendToModal()" 
                        class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition duration-300">
                    Cancel
                </button>
                <button type="submit" 
                        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition duration-300">
                    <i class="fas fa-paper-plane mr-2"></i> Send Notification
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    function showSendToModal() {
        document.getElementById('sendToModal').classList.remove('hidden');
    }

    function closeSendToModal() {
        document.getElementById('sendToModal').classList.add('hidden');
        // Reset form
        document.getElementById('sendToForm').reset();
    }

    // Close modal when clicking outside of it
    document.getElementById('sendToModal').addEventListener('click', function (e) {
        if (e.target.id === 'sendToModal') {
            closeSendToModal();
        }
    });

    // Handle form submission
    document.getElementById('sendToForm').onsubmit = function (e) {
        var recipient = document.getElementById('recipient').value;
        if (!recipient) {
            alert('Please select a recipient!');
            e.preventDefault();
            return false;
        }

        // Show confirmation
        var recipientText = '';
        switch (recipient) {
            case 'director':
                recipientText = 'Director';
                break;
            case 'staff':
                recipientText = 'Company Staff';
                break;
            case 'warehouse_manager':
                recipientText = 'Warehouse Manager';
                break;
        }

        if (!confirm('Are you sure you want to send the report to ' + recipientText + '?')) {
            e.preventDefault();
            return false;
        }
    }
</script>
