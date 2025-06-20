<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="p-6">
    <h2 class="text-2xl font-bold mb-4">My Requests</h2>

    <!-- Filter -->
    <form class="mb-4" method="get" action="my-request">
        <label for="statusFilter" class="block mb-1 font-medium">Status:</label>
        <select id="statusFilter" name="status" class="form-select w-64 p-2 rounded border border-gray-300" onchange="this.form.submit()">
            <option value="" ${empty param.status ? "selected" : ""}>All</option>
            <option value="Pending" ${param.status == 'Pending' ? "selected" : ""}>Pending</option>
            <option value="Approved" ${param.status == 'Approved' ? "selected" : ""}>Approved</option>
            <option value="Rejected" ${param.status == 'Rejected' ? "selected" : ""}>Rejected</option>
        </select>
    </form>

    <!-- Request table -->
    <c:if test="${empty myRequestList}">
        <div class="text-gray-500">You haven't submitted any requests yet.</div>
    </c:if>

    <c:if test="${not empty myRequestList}">
        <div class="overflow-auto rounded shadow">
            <table class="min-w-full bg-white text-sm border border-gray-200">
                <thead class="bg-gray-100 text-gray-600 font-semibold">
                    <tr>
                        <th class="p-3 text-left">#</th>
                        <th class="p-3 text-left">Type</th>
                        <th class="p-3 text-left">Status</th>
                        <th class="p-3 text-left">Date</th>
                        <th class="p-3 text-left">Note</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="req" items="${myRequestList}" varStatus="loop">
                        <tr class="border-t hover:bg-gray-50">
                            <td class="p-3">${loop.index + 1}</td>
                            <td class="p-3">${req.requestTypeName}</td>
                            <td class="p-3">
                                <span class="px-2 py-1 rounded-full text-xs font-semibold
                                      ${req.status == 'Pending' ? 'bg-yellow-200 text-yellow-800' :
                                        req.status == 'Approved' ? 'bg-green-200 text-green-800' :
                                        req.status == 'Rejected' ? 'bg-red-200 text-red-800' : 'bg-gray-200'}">
                                          ${req.status}
                                      </span>
                                </td>
                                <td class="p-3">${req.requestDate}</td>
                                <td class="p-3">${req.note}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
