<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Request List</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body class="bg-gray-100 p-0 m-0">

        <c:if test="${param.success eq 'true'}">
            <script>
                window.addEventListener("DOMContentLoaded", () => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success',
                        text: 'The request has been processed successfully.',
                        timer: 3000,
                        showConfirmButton: false
                    });
                });
            </script>
        </c:if>

        <div class="p-6">
            <h1 class="text-2xl font-bold mb-6">Request List</h1>
            <c:if test="${filterType == 'Purchase'}">
                <div class="text-sm text-gray-600 mb-2 italic">
                    Currently showing: <strong>Purchase Requests</strong>
                </div>
            </c:if>

            <!-- FORM FILTER -->
            <form action="reqlist" method="get" class="mb-6 flex flex-wrap gap-4 items-end">
                <div>
                    <label class="block font-semibold mb-1">Request Type:</label>
                    <select name="requestType" class="px-3 py-2 border rounded w-48">
                        <option value="">All</option>
                        <c:forEach var="type" items="${requestTypes}">
                            <c:if test="${type != 'Import'}">
                                <option value="${type}" ${type == filterType ? 'selected' : ''}>${type}</option>
                            </c:if>
                        </c:forEach>
                    </select>

                </div>

                <div>
                    <label class="block font-semibold mb-1">Status:</label>
                    <select name="status" class="px-3 py-2 border rounded w-48">
                        <option value="">All</option>
                        <option value="Pending" ${filterStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Approved" ${filterStatus == 'Approved' ? 'selected' : ''}>Approved</option>
                        <option value="Rejected" ${filterStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
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

            <!-- NÚT CREATE + SHOW ALL TRÊN CÙNG 1 DÒNG -->
            <div class="flex items-center justify-between mb-4">
                <a href="${pageContext.request.contextPath}/createrequest" class="inline-block px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
                    + Create Request
                </a>
                    
                <a href="${pageContext.request.contextPath}/create-repair-request" class="inline-block px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
                    + Create Repair Request
                </a>

                <c:choose>
                    <c:when test="${filterType == 'Purchase'}">
                        <form action="reqlist" method="get">
                            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto">
                                Show Material Request List
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="reqlist" method="get">
                            <input type="hidden" name="requestType" value="Purchase" />
                            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto">
                                Show Purchase Requests
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>


                <form action="reqlist" method="get">
                    <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 ml-auto">
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
                                <c:if test="${r.requestTypeName == 'Export' || r.requestTypeName == 'Import'}">
                                    <c:if test="${not empty r.subTypeName}">
                                        (<i>${r.subTypeName}</i>)
                                    </c:if>
                                </c:if>
                            </td>

                            <td class="border border-gray-300 px-3 py-2">${r.note}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.status}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.approvedByName}</td>
                            <td class="border border-gray-300 px-3 py-2">${r.approvalNote}</td>
                            <td class="border border-gray-300 px-3 py-2">
                                <a href="${pageContext.request.contextPath}/request-detail?id=${r.requestId}" class="text-blue-600 hover:underline">Detail</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>

            </table>   

            <%
                int currentPage = (int) request.getAttribute("currentPage");
                int totalPage = (int) request.getAttribute("totalPage");
                int startPage = (int) request.getAttribute("startPage");
                int endPage = (int) request.getAttribute("endPage");
                String baseParams = (String) request.getAttribute("baseParams");
            %>

            <!-- Pagination -->
            <div class="flex justify-between items-center mt-4">
                <div class="text-sm text-gray-600">
                    Showing page <%= currentPage%> of <%= totalPage%>
                </div>
                <div class="space-x-1">
                    <% if (currentPage > 1) {%>
                    <a href="reqlist?page=<%= currentPage - 1%><%= baseParams%>" class="px-3 py-1 border rounded">«</a>
                    <% } else { %>
                    <span class="px-3 py-1 border rounded text-gray-400 cursor-not-allowed">«</span>
                    <% } %>

                    <% for (int i = startPage; i <= endPage; i++) { %>
                    <% if (i == currentPage) {%>
                    <span class="px-3 py-1 border rounded bg-blue-600 text-white"><%= i%></span>
                    <% } else {%>
                    <a href="reqlist?page=<%= i%><%= baseParams%>" class="px-3 py-1 border rounded hover:bg-gray-100"><%= i%></a>
                    <% } %>
                    <% } %>

                    <% if (currentPage < totalPage) {%>
                    <a href="reqlist?page=<%= currentPage + 1%><%= baseParams%>" class="px-3 py-1 border rounded">»</a>
                    <% } else { %>
                    <span class="px-3 py-1 border rounded text-gray-400 cursor-not-allowed">»</span>
                    <% }%>
                </div>
            </div>


        </div>

    </body>

    <script>
        <c:if test="${not empty sessionScope.successMessage}">
        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: '${sessionScope.successMessage}',
            showConfirmButton: false,
            timer: 2000
        });
            <c:remove var="successMessage" scope="session" />
        </c:if>
    </script>
</html>