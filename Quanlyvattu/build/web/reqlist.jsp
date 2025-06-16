<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Request List</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-100 p-0 m-0">

        <div class="p-6">
            <h1 class="text-2xl font-bold mb-6">Request List</h1>

            <form action="reqlist" method="get" class="mb-4 space-x-4">
                <label>Request Type:
                    <select name="requestType">
                        <option value="">All</option>
                        <c:forEach var="type" items="${requestTypes}">
                            <option value="${type}" ${type == filterType ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>Status:
                    <select name="status">
                        <option value="">All</option>
                        <option value="Pending" ${filterStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Approved" ${filterStatus == 'Approved' ? 'selected' : ''}>Approved</option>
                        <option value="Rejected" ${filterStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
                    </select>
                </label>

                <label>Request By:
                    <input type="text" name="requestedBy" value="${filterRequestedBy != null ? filterRequestedBy : ''}" />
                </label>

                <label>Created At:
                    <input type="date" name="requestDate"
                           value="${filterRequestDate != null ? filterRequestDate : ''}" />
                </label>


                <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Lọc</button>
            </form>

            <div class="mb-4">
                <a href="createrequest.jsp" class="inline-block px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
                    + Create Request
                </a>
            </div>
            <div class="mb-4">
                <form action="reqlist" method="get">
                    <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                        Show All Requests
                    </button>
                </form>
            </div>

            <!-- BẢNG -->
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
                                <c:if test="${r.requestTypeName == 'Export'}">
                                    (<i>${r.exportTypeName}</i>)
                                </c:if>
                                <c:if test="${r.requestTypeName == 'Import'}">
                                    (<i>${r.importTypeName}</i>)
                                </c:if>
                            </td>
                            <td class="border border-gray-300 px-3 py-2">${r.note}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.status}</td>

                            <td class="border border-gray-300 px-3 py-2">${r.approvedByName}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.approvalNote}</td>
                            <td class="border border-gray-300 px-3 py-2">
                                <a href="requestdetail?id=${r.requestId}" class="text-blue-600 hover:underline">Detail</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>    
        </div>

    </body>
</html>
