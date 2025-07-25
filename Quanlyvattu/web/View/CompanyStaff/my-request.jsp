<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="p-6">
    <h2 class="text-2xl font-bold mb-4 text-center">My Requests</h2>

    <!-- Filter Form -->
    <form class="mb-4 flex flex-wrap gap-4 items-end" method="get" action="my-request">
        <!-- Type -->
        <div>
            <label for="typeFilter" class="block mb-1 font-medium">Type Request:</label>
            <select id="typeFilter" name="type" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.type ? "selected" : ""}>All</option>
                <c:forEach var="typeName" items="${requestTypes}">
                    <option value="${typeName}" ${param.type == typeName ? "selected" : ""}>${typeName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Status -->
        <div>
            <label for="statusFilter" class="block mb-1 font-medium">Status Request:</label>
            <select id="statusFilter" name="status" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.status ? "selected" : ""}>All</option>
                <option value="Pending" ${param.status == 'Pending' ? "selected" : ""}>Pending</option>
                <option value="Approved" ${param.status == 'Approved' ? "selected" : ""}>Approved</option>
                <option value="Rejected" ${param.status == 'Rejected' ? "selected" : ""}>Rejected</option>
            </select>
        </div>

        <!-- PO Status -->
        <div>
            <label for="poStatusFilter" class="block mb-1 font-medium">Purchase Order Status:</label>
            <select id="poStatusFilter" name="poStatus" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.poStatus ? "selected" : ""}>All</option>
                <option value="Pending" ${param.poStatus == 'Pending' ? "selected" : ""}>Pending</option>
                <option value="Approved" ${param.poStatus == 'Approved' ? "selected" : ""}>Approved</option>
                <option value="Rejected" ${param.poStatus == 'Rejected' ? "selected" : ""}>Rejected</option>
                <option value="NotCreated" ${param.poStatus == 'NotCreated' ? "selected" : ""}>Not Created</option>
            </select>
        </div>

        <!-- Note Filter -->
        <div>
            <label for="noteFilter" class="block mb-1 font-medium">Note:</label>
            <input type="text"
                   id="noteFilter"
                   name="note"
                   value="${fn:escapeXml(param.note)}"
                   placeholder="e.g. Repair request for mixer"
                   class="form-input w-64 p-2 rounded border border-gray-300" />
        </div>

        <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">
            Filter
        </button>
        <a href="my-request" class="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded">
            Show All
        </a>
    </form>

    <!-- Create Request buttons -->
    <div class="flex items-center justify-between mb-4">
        <c:if test="${userRole == 'Company Staff'}">
            <a href="createrequest" class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded">
                + Create Request
            </a>
        </c:if>
        <c:if test="${userRole == 'Warehouse Manager'}">
            <a href="create-repair-request" class="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded">
                + Create Repair Request
            </a>
        </c:if>
    </div>

    <!-- If no requests -->
    <c:if test="${empty myRequestList}">
        <div class="text-gray-500">You haven't submitted any requests yet.</div>
    </c:if>

    <!-- If there are requests -->
    <c:if test="${not empty myRequestList}">
        <div class="overflow-auto rounded shadow">
            <table class="min-w-full bg-white text-sm border border-gray-200">
                <thead class="bg-gray-100 text-gray-600 font-semibold">
                    <tr>
                        <th class="p-3 text-left">No</th>
                        <th class="p-3 text-left">Type</th>
                        <th class="p-3 text-left">Status</th>
                        <th class="p-3 text-left">Date</th>
                        <th class="p-3 text-left">Note</th>
                        <th class="p-3 text-left">Director Note</th>
                        <th class="p-3 text-left">Order Status</th>
                        <th class="p-3 text-left">Action</th>
                        <th class="p-3 text-left">Detail</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="req" items="${myRequestList}" varStatus="loop">
                        <tr class="border-t hover:bg-gray-50">
                            <td class="p-3">${(currentPage - 1) * 5 + loop.index + 1}</td>
                            <td class="p-3">${req.requestTypeName}</td>
                            <td class="p-3">
                                <span class="px-2 py-1 rounded-full text-xs font-semibold
                                      ${req.status == 'Pending' ? 'bg-yellow-100 text-yellow-800' :
                                        req.status == 'Approved' ? 'bg-green-100 text-green-800' :
                                        req.status == 'Rejected' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-600'}">
                                          ${req.status}
                                      </span>
                                </td>
                                <td class="p-3">${req.requestDate}</td>
                                <td class="p-3">${req.note}</td>
                                <td class="p-3">${req.approvalNote}</td>
                                <td class="p-3">
                                    <c:choose>
                                        <c:when test="${req.requestTypeName == 'Purchase'}">
                                            <c:choose>
                                                <c:when test="${empty req.poStatus}">
                                                    <span class="px-2 py-1 rounded-full text-xs font-semibold bg-gray-100 text-gray-600">Not Created</span>
                                                </c:when>
                                                <c:when test="${req.poStatus == 'Pending'}">
                                                    <span class="px-2 py-1 rounded-full text-xs font-semibold bg-yellow-100 text-yellow-800">Pending</span>
                                                </c:when>
                                                <c:when test="${req.poStatus == 'Approved'}">
                                                    <span class="px-2 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-800">Approved</span>
                                                </c:when>
                                                <c:when test="${req.poStatus == 'Rejected'}">
                                                    <span class="px-2 py-1 rounded-full text-xs font-semibold bg-red-100 text-red-800">Rejected</span>
                                                </c:when>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${req.requestTypeName == 'Repair'}">
                                            <span class="px-2 py-1 rounded-full text-xs font-semibold
                                                  ${req.hasRO ? 'bg-yellow-100 text-yellow-800' : 'bg-gray-100 text-gray-600'}">
                                                ${req.hasRO ? 'RO Created' : 'Pending'}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-gray-500 italic text-xs">N/A</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="p-3">
                                    <c:if test="${req.requestTypeName == 'Purchase' && req.hasPO}">
                                        <a href="purchase-order-detail?id=${req.POId}" class="text-indigo-600 hover:underline text-sm">
                                            View PO
                                        </a>
                                    </c:if>
                                </td>
                                <td class="p-3">
                                    <a href="request-detail?id=${req.requestId}" class="text-blue-600 hover:underline text-sm">View</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Pagination -->
                <div class="mt-4 flex justify-center gap-2">
                    <c:if test="${currentPage > 1}">
                        <a href="my-request?page=1&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}&note=${param.note}" class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&laquo;</a>
                        <a href="my-request?page=${currentPage - 1}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}&note=${param.note}" class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&lt;</a>
                    </c:if>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="my-request?page=${i}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}&note=${param.note}"
                           class="px-3 py-1 border rounded
                           ${i == currentPage ? 'bg-blue-500 text-white' : 'bg-white text-gray-700 hover:bg-gray-100'}">
                            ${i}
                        </a>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="my-request?page=${currentPage + 1}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}&note=${param.note}" class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&gt;</a>
                        <a href="my-request?page=${totalPages}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}&note=${param.note}" class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&raquo;</a>
                    </c:if>
                </div>
            </div>
        </c:if>

        <!-- SweetAlert when RO created -->
        <c:if test="${param.roCreated == 'true'}">
            <script>
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Repair Order created successfully.',
                    timer: 2000,
                    showConfirmButton: false
                });
            </script>
        </c:if>
    </div>
