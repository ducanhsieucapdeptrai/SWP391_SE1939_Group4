<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2 class="text-xl font-bold mb-4">Danh sách công việc được giao</h2>

<!-- Form lọc -->
<form method="get" action="mytasks" class="mb-4 flex gap-4 items-center">
    <label for="type">Loại yêu cầu:</label>
    <select name="type" id="type" class="border rounded px-2 py-1">
        <option value="">Tất cả</option>
        <c:forEach var="t" items="${requestTypes}">
            <option value="${t}" ${t == filterType ? 'selected' : ''}>${t}</option>
        </c:forEach>
    </select>

    <label for="requestDate">Ngày yêu cầu:</label>
    <input type="date" name="requestDate" id="requestDate" value="${filterRequestDate}" class="border rounded px-2 py-1"/>

    <button type="submit" class="bg-blue-500 text-white px-4 py-1 rounded hover:bg-blue-600">Lọc</button>
</form>

<!-- Bảng danh sách yêu cầu -->
<table class="min-w-full border border-gray-300 text-sm text-left">
    <thead class="bg-gray-100">
        <tr>
            <th class="border px-4 py-2">#</th>
            <th class="border px-4 py-2">Người yêu cầu</th>
            <th class="border px-4 py-2">Loại yêu cầu</th>
            <th class="border px-4 py-2">Ngày yêu cầu</th>
            <th class="border px-4 py-2">Ghi chú</th>
            <th class="border px-4 py-2">Xem chi tiết</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="r" items="${myTasks}" varStatus="status">
            <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                <td class="border px-4 py-2">${status.index + 1}</td>
                <td class="border px-4 py-2">${r.requestedByName}</td>
                <td class="border px-4 py-2">${r.requestTypeName}</td>
                <td class="border px-4 py-2">
                    <fmt:formatDate value="${r.requestDate}" pattern="yyyy-MM-dd" />
                </td>
                <td class="border px-4 py-2">${r.note}</td>
                <td class="border px-4 py-2">
                    <a href="approvedrequestdetail?requestId=${r.requestId}" class="text-blue-600 hover:underline">Chi tiết</a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty myTasks}">
            <tr>
                <td colspan="6" class="text-center text-gray-500 py-4">Không có công việc nào được giao.</td>
            </tr>
        </c:if>
    </tbody>
</table>
