<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="bg-white rounded-lg shadow p-6">
    <h1 class="text-2xl font-semibold mb-4 text-blue-800">Danh sách yêu cầu vật tư</h1>
    <table class="min-w-full bg-white border border-gray-300">
        <thead class="bg-blue-100">
            <tr>
                <th class="px-4 py-2 border">Mã yêu cầu</th>
                <th class="px-4 py-2 border">Người yêu cầu</th>
                <th class="px-4 py-2 border">Loại yêu cầu</th>
                <th class="px-4 py-2 border">Trạng thái</th>
                <th class="px-4 py-2 border">Ngày yêu cầu</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="req" items="${requestList}">
            <tr class="hover:bg-gray-50">
                <td class="px-4 py-2 border">${req.requestId}</td>
                <td class="px-4 py-2 border">${req.requestedByName}</td>
                <td class="px-4 py-2 border">${req.requestTypeName}</td>
                <td class="px-4 py-2 border">${req.statusDescription}</td>
                <td class="px-4 py-2 border">${req.requestDate}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
