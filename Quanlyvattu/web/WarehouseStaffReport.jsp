<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<!-- WarehouseStaffReport Content -->
<div class="max-w-7xl mx-auto">
    <!-- Header with Back Button and Title -->
    <div class="mb-6">
        <a href="javascript:history.back()" class="inline-flex items-center text-blue-600 hover:text-blue-800 mb-4">
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
    
    <!-- Related Import Records -->
    <div class="bg-white rounded-lg shadow-md mb-6">
        <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">Related ${requestInfo.requestTypeName} Records</h2>
        </div>
        <div class="p-6">
            <c:choose>
                <c:when test="${not empty relatedImports}">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Import ID</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Import Date</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Imported By</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Import Type</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Notes</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="importItem" items="${relatedImports}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${importItem.importId}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty importItem.importDate}">
                                                    <fmt:formatDate value="${importItem.importDate}" pattern="dd/MM/yyyy" />
                                                </c:when>
                                                <c:otherwise>
                                                    No date available
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty importItem.importedByName}">
                                                    ${importItem.importedByName}
                                                </c:when>
                                                <c:otherwise>
                                                    ID: ${importItem.importedBy}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty importItem.importTypeName}">
                                                    ${importItem.importTypeName}
                                                </c:when>
                                                <c:otherwise>
                                                    Not specified
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty importItem.note}">
                                                    ${importItem.note}
                                                </c:when>
                                                <c:otherwise>
                                                    No notes
                                                </c:otherwise>
                                            </c:choose>
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
                        <p class="text-gray-500">No related import records found.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <!-- Related  Details -->
    <div class="bg-white rounded-lg shadow-md mb-6">
        <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">Related  ${requestInfo.requestTypeName} Details</h2>
        </div>
        <div class="p-6">
            <c:choose>
                <c:when test="${not empty relatedImportDetails}">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">No.</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Import ID</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Material Name</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Import Quantity</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Unit Price</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:set var="totalAmount" value="0" />
                                <c:forEach var="detail" items="${relatedImportDetails}" varStatus="status">
                                    <c:set var="lineTotal" value="${detail.quantity * detail.price}" />
                                    <c:set var="totalAmount" value="${totalAmount + lineTotal}" />
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${status.count}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${detail.importId}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <c:choose>
                                                <c:when test="${not empty detail.materialName}">
                                                    ${detail.materialName}
                                                </c:when>
                                                <c:otherwise>
                                                    ID: ${detail.materialId}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <fmt:formatNumber value="${detail.quantity}" />
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="₫" />
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <fmt:formatNumber value="${lineTotal}" type="currency" currencySymbol="₫" />
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr class="bg-gray-100 font-semibold">
                                    <td colspan="5" class="px-6 py-4 text-sm text-gray-900">Grand Total:</td>  
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₫" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-12">
                        <i class="fas fa-inbox text-4xl text-gray-400 mb-4"></i>
                        <p class="text-gray-500">No related import details available.</p>
                    </div>
                </c:otherwise>
            </c:choose>
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
    document.getElementById('sendToModal').addEventListener('click', function(e) {
        if (e.target.id === 'sendToModal') {
            closeSendToModal();
        }
    });
    
    // Handle form submission
    document.getElementById('sendToForm').onsubmit = function(e) {
        var recipient = document.getElementById('recipient').value;
        if (!recipient) {
            alert('Please select a recipient!');
            e.preventDefault();
            return false;
        }
        
        // Show confirmation
        var recipientText = '';
        switch(recipient) {
            case 'director': recipientText = 'Director'; break;
            case 'staff': recipientText = 'Company Staff'; break;
            case 'warehouse_manager': recipientText = 'Warehouse Manager'; break;
        }
        
        if (!confirm('Are you sure you want to send the report to ' + recipientText + '?')) {
            e.preventDefault();
            return false;
        }
    }
</script>
