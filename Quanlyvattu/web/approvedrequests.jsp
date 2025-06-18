<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Approved Requests</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-100 p-0 m-0">

        <div class="p-6">
            <h1 class="text-2xl font-bold mb-6">Approved Requests</h1>

            <button onclick="history.back()" class="btn btn-outline-secondary back-button">
                <i class="fas fa-arrow-left me-2"></i>
                Back
            </button>

            <!-- FORM FILTER (bỏ phần status) -->
            <form action="reqlist" method="get" class="mb-6 flex flex-wrap gap-4 items-end">
                <input type="hidden" name="status" value="Approved" />

                <div>
                    <label class="block font-semibold mb-1">Request Type:</label>
                    <select name="requestType" class="px-3 py-2 border rounded w-48">
                        <option value="">All</option>
                        <c:forEach var="type" items="${requestTypes}">
                            <option value="${type}" ${type == filterType ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label class="block font-semibold mb-1">Request By:</label>
                    <input type="text" name="requestedBy" value="${filterRequestedBy != null ? filterRequestedBy : ''}" class="px-3 py-2 border rounded w-48" />
                </div>

                <div>
                    <label class="block font-semibold mb-1">Created At:</label>
                    <input type="date" name="requestDate" value="${filterRequestDate != null ? filterRequestDate : ''}" class="px-3 py-2 border rounded w-48" />
                </div>

                <div class="self-end">
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Find</button>
                </div>
            </form>

            <!-- TABLE -->
            <table class="w-full table-auto border-collapse bg-white shadow-md">
                <thead class="bg-gray-200">
                    <tr>
                        <th class="border border-gray-300 px-3 py-2">No.</th>
                        <th class="border border-gray-300 px-3 py-2">Requested By</th>
                        <th class="border border-gray-300 px-3 py-2">Date</th>
                        <th class="border border-gray-300 px-3 py-2">Request Type</th>
                        <th class="border border-gray-300 px-3 py-2">Note</th>
                        <th class="border border-gray-300 px-3 py-2">Approved By</th>
                        <th class="border border-gray-300 px-3 py-2">Approval Note</th>
                        <th class="border border-gray-300 px-3 py-2">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${approvedRequestList}" varStatus="loop">
                        <tr class="hover:bg-gray-100">
                            <td class="border border-gray-300 px-3 py-2">${loop.index + 1}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.requestedByName}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.requestDate}</td>
                            <td class="border border-gray-300 px-3 py-2">
                                ${r.requestTypeName}
                                <c:if test="${r.requestTypeName == 'Export'}">
                                    (<i>${r.exportTypeName}</i>)
                                </c:if>
                                <c:if test="${r.requestTypeName == 'Import'}">
                                    (<i>${r.importTypeName}</i>)
                                </c:if>
                            </td>
                            <td class="border border-gray-300 px-3 py-2">${r.note}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.approvedByName}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.approvalNote}</td>
                            <td class="border border-gray-300 px-3 py-2">
                                <a href="${pageContext.request.contextPath}/request-detail?id=${r.requestId}" class="text-blue-600 hover:underline">Detail</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
