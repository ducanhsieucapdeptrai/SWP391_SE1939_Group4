<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="p-6">
    <h2 class="text-2xl font-bold mb-4 text-center">My Requests</h2>

    <!-- Filter Form -->
    <form class="mb-4 flex flex-wrap gap-4 items-end" method="get" action="my-request">
        <!-- Type Filter -->
        <div>
            <label for="typeFilter" class="block mb-1 font-medium">Type Request:</label>
            <select id="typeFilter" name="type" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.type ? "selected" : ""}>All</option>
                <option value="Export" ${param.type == 'Export' ? "selected" : ""}>Export</option>
                <option value="Import" ${param.type == 'Import' ? "selected" : ""}>Import</option>
                <option value="Purchase" ${param.type == 'Purchase' ? "selected" : ""}>Purchase</option>
            </select>
        </div>

        <!-- Status Filter -->
        <div>
            <label for="statusFilter" class="block mb-1 font-medium">Status Request:</label>
            <select id="statusFilter" name="status" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.status ? "selected" : ""}>All</option>
                <option value="Pending" ${param.status == 'Pending' ? "selected" : ""}>Pending</option>
                <option value="Approved" ${param.status == 'Approved' ? "selected" : ""}>Approved</option>
                <option value="Rejected" ${param.status == 'Rejected' ? "selected" : ""}>Rejected</option>
            </select>
        </div>

        <!-- PO Status Filter -->
        <div>
            <label for="poStatusFilter" class="block mb-1 font-medium">Purchase Order Status:</label>
            <select id="poStatusFilter" name="poStatus" class="form-select w-48 p-2 rounded border border-gray-300">
                <option value="" ${empty param.poStatus ? "selected" : ""}>All</option>
                <option value="Pending" ${param.poStatus == 'Pending' ? "selected" : ""}>Pending</option>
                <option value="Approved" ${param.poStatus == 'Approved' ? "selected" : ""}>Approved</option>
                <option value="Rejected" ${param.poStatus == 'Rejected' ? "selected" : ""}>Rejected</option>
            </select>
        </div>

        <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded">
            Filter
        </button>
        <div class="flex items-end">
            <a href="my-request"
               class="text-center bg-red-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded w-full md:w-auto">
                Clear Filter
            </a>
        </div>
    </form>

    <c:if test="${empty myRequestList}">
        <div class="text-gray-500">You haven't submitted any requests yet.</div>
    </c:if>

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
                        <th class="p-3 text-left">PO Status</th>
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
                                        <c:when test="${req.poCount > 0}">
                                            <span class="px-2 py-1 rounded-full text-xs font-semibold
                                                  ${req.poStatus == 'Pending' ? 'bg-yellow-100 text-yellow-800' :
                                                    req.poStatus == 'Approved' ? 'bg-green-100 text-green-800' :
                                                    req.poStatus == 'Rejected' ? 'bg-red-100 text-red-800' :
                                                    'bg-gray-100 text-gray-600'}">
                                                      ${req.poStatus}
                                                  </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="bg-gray-100 text-gray-600 px-2 py-1 text-xs rounded-full italic">
                                                    Not Created
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="p-3">
                                        <c:choose>
                                            <c:when test="${req.status == 'Approved' && req.requestTypeName == 'Purchase' && !req.hasPO}">
                                                <form action="create-po" method="post">
                                                    <input type="hidden" name="requestId" value="${req.requestId}" />
                                                    <input type="hidden" name="submit" value="false" />
                                                    <button type="submit"
                                                            class="bg-blue-500 hover:bg-blue-600 text-white text-xs px-3 py-1 rounded">
                                                        Create Purchase Order
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:when test="${req.status == 'Approved' && req.requestTypeName == 'Purchase' && req.hasPO}">
                                                <span class="text-green-600 font-semibold text-xs">PO Created</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-gray-400 text-xs italic">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="p-3">
                                        <a href="request-detail?id=${req.requestId}" 
                                           class="text-blue-600 hover:underline text-sm">View</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="mt-4 flex justify-center gap-2">
                        <c:if test="${currentPage > 1}">
                            <a href="my-request?page=1&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}"
                               class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&laquo;</a>
                            <a href="my-request?page=${currentPage - 1}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}"
                               class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&lt;</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="my-request?page=${i}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}"
                               class="px-3 py-1 border rounded 
                               ${i == currentPage ? 'bg-blue-500 text-white' : 'bg-white text-gray-700 hover:bg-gray-100'}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="my-request?page=${currentPage + 1}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}"
                               class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&gt;</a>
                            <a href="my-request?page=${totalPages}&type=${param.type}&status=${param.status}&poStatus=${param.poStatus}"
                               class="px-3 py-1 border rounded bg-white text-gray-700 hover:bg-gray-100">&raquo;</a>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
