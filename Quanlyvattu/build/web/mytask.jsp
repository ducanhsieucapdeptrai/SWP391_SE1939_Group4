<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>




<div class="p-6">
    <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold">My Task</h1>

        <div class="flex gap-4">
            <!-- Nút Back -->
            <button onclick="history.back()" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                <i class="fas fa-arrow-left mr-2"></i>
                Back
            </button>

        </div>
    </div>


    <!-- Wrapper chứa form filter + nút My Task -->
    <div class="mb-6 flex justify-between items-end flex-wrap gap-4">

        <!-- FORM FILTER (bỏ phần status) -->
        <form action="approvedrequests" method="get" class="flex flex-wrap gap-4 items-end">
            <input type="hidden" name="status" value="Approved" />

            <div>
                <label class="block font-semibold mb-1">Request Type:</label>
                <select name="type" id="type" class="border rounded px-2 py-1">
                    <option value="">All</option>
                    <c:forEach var="t" items="${requestTypes}">
                        <option value="${t}" ${t == filterType ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>



            <div>
                <label class="block font-semibold mb-1">Created At:</label>
                <input type="date" name="requestDate" id="requestDate" value="${filterRequestDate}" class="border rounded px-2 py-1"/>
            </div>

            <div class="self-end">
                <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Find</button>
            </div>
        </form>


        <!-- Bảng danh sách yêu cầu -->
        <table class="min-w-full border border-gray-300 text-sm text-left">
            <thead class="bg-gray-100">
                <tr>
                    <th class="border px-4 py-2">#</th>
                    <th class="border px-4 py-2">Requested By</th>
                    <th class="border px-4 py-2">Request Type</th>
                    <th class="border px-4 py-2">Date</th>
                    <th class="border px-4 py-2">Note</th>
                    <th class="border px-4 py-2">Action</th>
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
                            <!-- Sua lai nut detail -->
                            <a href="warehousereport?requestId=${r.requestId}" class="text-blue-600 hover:underline">Detail</a> 
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty myTasks}">
                    <tr>
                        <td colspan="6" class="text-center text-gray-500 py-4">No task ahead lmaoo.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
