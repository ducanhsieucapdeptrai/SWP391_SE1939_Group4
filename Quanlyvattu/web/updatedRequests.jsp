<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="text-2xl font-bold mb-4">Confirmed Material Requests</h2>

<table class="w-full table-auto border-collapse bg-white shadow-md">
    <thead class="bg-gray-200">
        <tr>
            <th class="border border-gray-300 px-3 py-2">No.</th>
            <th class="border border-gray-300 px-3 py-2">Requested By</th>
            <th class="border border-gray-300 px-3 py-2">Date</th>
            <th class="border border-gray-300 px-3 py-2">Request Type</th>
            <th class="border border-gray-300 px-3 py-2">Note</th>
            <th class="border border-gray-300 px-3 py-2">Status</th>
            <th class="border border-gray-300 px-3 py-2">Approved By</th>
            <th class="border border-gray-300 px-3 py-2">Approval Note</th>
            <th class="border border-gray-300 px-3 py-2">Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="r" items="${requestList}" varStatus="loop">
            <tr class="hover:bg-gray-100">
                <td class="border border-gray-300 px-3 py-2">${loop.index + 1}</td>
                <td class="border border-gray-300 px-3 py-2">${r.requestedByName}</td>
                <td class="border border-gray-300 px-3 py-2">${r.requestDate}</td>
                <td class="border border-gray-300 px-3 py-2">
                    ${r.requestTypeName}
                    <c:if test="${r.requestTypeName == 'Export'}">(<i>${r.exportTypeName}</i>)</c:if>
                    <c:if test="${r.requestTypeName == 'Import'}">(<i>${r.importTypeName}</i>)</c:if>
                </td>
                <td class="border border-gray-300 px-3 py-2">${r.note}</td>
                <td class="border border-gray-300 px-3 py-2">${r.statusDescription}</td>
                <td class="border border-gray-300 px-3 py-2">${r.approvedByName}</td>
                <td class="border border-gray-300 px-3 py-2">${r.approvalNote}</td>
                <td class="border border-gray-300 px-3 py-2">
                    <a href="request-detail?id=${r.requestId}" class="text-blue-600 hover:underline">Detail</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
