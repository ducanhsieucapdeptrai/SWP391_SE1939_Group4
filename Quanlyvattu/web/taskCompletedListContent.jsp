<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="max-w-6xl mx-auto p-6">
    <h2 class="text-2xl font-bold mb-6 text-gray-800">List of Completed Tasks</h2>

    <div class="bg-white rounded-lg shadow border border-gray-200 overflow-x-auto">
        <table class="min-w-full text-sm text-left">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="px-4 py-2">ID</th>
                    <th class="px-4 py-2">Request Type</th>
                    <th class="px-4 py-2">Created Date</th>
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
                            <c:out value="${r.note}" default="-" />
                        </td>
                        <td class="px-4 py-2">
                            <a href="taskUpdate?requestId=${r.requestId}" 
                               class="inline-block bg-blue-600 text-white text-xs font-semibold px-3 py-1 rounded hover:bg-blue-700 transition">
                                Here
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty completedList}">
                    <tr>
                        <td colspan="5" class="text-center text-gray-500 py-4">
                            No completed tasks found.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>
