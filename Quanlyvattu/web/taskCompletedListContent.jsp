<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="max-w-6xl mx-auto p-6">
    <h2 class="text-2xl font-bold mb-6 text-gray-800">List of Completed Tasks</h2>

    <!-- Filter Form -->
    <form method="get" action="completedTasks" class="mb-6 bg-gray-50 p-4 rounded-lg border flex flex-wrap gap-4">
        <input type="hidden" name="page" value="1" />
        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Request Type:</label>
            <select name="type" class="rounded border-gray-300 px-3 py-2">
                <option value="">All</option>
                <option value="Import" ${type == 'Import' ? 'selected' : ''}>Import</option>
                <option value="Export" ${type == 'Export' ? 'selected' : ''}>Export</option>
            </select>
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Requested By:</label>
            <input type="text" name="requestedBy" value="${requestedBy}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Created Date:</label>
            <input type="date" name="createdDate" value="${createdDate}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Finish Date:</label>
            <input type="date" name="finishDate" value="${finishDate}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Sort By:</label>
            <select name="sortBy" class="rounded border-gray-300 px-3 py-2">
                <option value="finishedDate_desc" ${sortBy == 'finishedDate_desc' ? 'selected' : ''}>Finished Date (Newest)</option>
                <option value="finishedDate_asc" ${sortBy == 'finishedDate_asc' ? 'selected' : ''}>Finished Date (Oldest)</option>
                <option value="requestDate_desc" ${sortBy == 'requestDate_desc' ? 'selected' : ''}>Created Date (Newest)</option>
                <option value="requestDate_asc" ${sortBy == 'requestDate_asc' ? 'selected' : ''}>Created Date (Oldest)</option>
                <option value="requestId_desc" ${sortBy == 'requestId_desc' ? 'selected' : ''}>Request ID (Descending)</option>
                <option value="requestId_asc" ${sortBy == 'requestId_asc' ? 'selected' : ''}>Request ID (Ascending)</option>
            </select>
        </div>

        <div class="flex items-end space-x-2">
            <button type="submit" class="bg-blue-600 text-white font-semibold px-4 py-2 rounded hover:bg-blue-700">
                Search
            </button>
            <a href="completedTasks" class="bg-gray-500 text-white font-semibold px-4 py-2 rounded hover:bg-gray-600">
                Clear Filter
            </a>
        </div>
    </form>

    <!-- Result Table -->
    <div class="bg-white rounded-lg shadow border border-gray-200 overflow-x-auto">
        <table class="min-w-full text-sm text-left">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="px-4 py-2">ID</th>
                    <th class="px-4 py-2">Request Type</th>
                    <th class="px-4 py-2">Created Date</th>
                    <th class="px-4 py-2">Finish Date</th>
                    <th class="px-4 py-2">Note</th>
                    <th class="px-4 py-2">Detail</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="r" items="${completedList}">
                    <tr class="border-t hover:bg-gray-50">
                        <td class="px-4 py-2 font-medium text-gray-800">${r.requestId}</td>
                        <td class="px-4 py-2 text-gray-700">${r.requestTypeName}</td>
                        <td class="px-4 py-2 text-gray-600">
                            <fmt:formatDate value="${r.requestDate}" pattern="dd/MM/yyyy HH:mm" />
                        </td>
                        <td class="px-4 py-2 text-gray-600">
                            <fmt:formatDate value="${r.finishedDate}" pattern="dd/MM/yyyy HH:mm" />
                        </td>
                        <td class="px-4 py-2 text-gray-600">
                            <c:out value="${r.note}" default="-" />
                        </td>
                        <td class="px-4 py-2">
                            <a href="warehousereport?requestId=${r.requestId}" 
                               class="inline-block bg-blue-600 text-white text-xs font-semibold px-3 py-1 rounded hover:bg-blue-700 transition">
                                Here
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty completedList}">
                    <tr>
                        <td colspan="6" class="text-center text-gray-500 py-4">
                            No completed tasks found.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <div class="mt-8 flex justify-between items-center text-sm text-gray-700">
        <div>
            Showing
            <c:out value="${(currentPage - 1) * pageSize + 1}" /> -
            <c:out value="${(currentPage - 1) * pageSize + fn:length(completedList)}" />
            of ${totalRecords} results
        </div>

        <div class="text-gray-800 font-medium">
            Page ${currentPage} of ${totalPages}
        </div>

        <div class="flex space-x-2">
            <!-- Prev -->
            <form method="get" action="completedTasks">
                <input type="hidden" name="page" value="${currentPage - 1}" />
                <input type="hidden" name="type" value="${type}" />
                <input type="hidden" name="requestedBy" value="${requestedBy}" />
                <input type="hidden" name="createdDate" value="${createdDate}" />
                <input type="hidden" name="finishDate" value="${finishDate}" />
                <input type="hidden" name="sortBy" value="${sortBy}" />
                <button
                    class="px-4 py-2 rounded-md border font-semibold
                           ${currentPage == 1 ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed' 
                                              : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}"
                    ${currentPage == 1 ? 'disabled' : ''}>
                    ? Prev
                </button>
            </form>

            <!-- Next -->
            <form method="get" action="completedTasks">
                <input type="hidden" name="page" value="${currentPage + 1}" />
                <input type="hidden" name="type" value="${type}" />
                <input type="hidden" name="requestedBy" value="${requestedBy}" />
                <input type="hidden" name="createdDate" value="${createdDate}" />
                <input type="hidden" name="finishDate" value="${finishDate}" />
                <input type="hidden" name="sortBy" value="${sortBy}" />
                <button
                    class="px-4 py-2 rounded-md border font-semibold
                           ${currentPage == totalPages ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed' 
                                                       : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}"
                    ${currentPage == totalPages ? 'disabled' : ''}>
                    Next ?
                </button>
            </form>
        </div>
    </div>
</div>
