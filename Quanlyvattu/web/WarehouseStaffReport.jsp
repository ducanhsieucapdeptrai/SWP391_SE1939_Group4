<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<style>
    th, td {
        padding: 0.5rem;
        vertical-align: middle;
    }
</style>

<div class="container mx-auto p-6 space-y-6">
    <!-- Header & Back -->
    <div class="flex items-center justify-between ">
        <h1 class="text-2xl font-bold">Request Detail – Actual Record</h1>
        <a href="${pageContext.request.contextPath}/tasklist" class="bg-gray-300 hover:bg-gray-400 text-black font-semibold py-2 px-4 rounded inline-block">← Back </a>
    </div>

    <!-- Request Information -->
    <section class="bg-white p-6 rounded shadow space-y-4 p-4 border-2 border-gray-300 rounded bg-gray-50 shadow-md mb-4">
        <h2 class="text-xl font-semibold">Request Information</h2>
        <table class="w-full text-sm border-collapse">
            <tr><th class="text-left w-48">ID</th><td>${request.requestId}</td></tr>
            <tr><th class="text-left">Requested By</th><td>${request.requestedByName}</td></tr>
            <tr><th class="text-left">Date</th><td><fmt:formatDate value="${request.requestDate}" pattern="dd-MM-yyyy HH:mm"/></td></tr>
            <tr><th class="text-left">Type</th><td>${request.requestType.requestTypeName}</td></tr>
            <tr><th class="text-left">Sub Type</th><td>${request.subTypeName}</td></tr>
            <tr><th class="text-left">Status</th><td>${request.statusDescription}</td></tr>
            <tr><th class="text-left">Note</th><td>${request.note}</td></tr>
        </table>
    </section>

    <!-- Request Details -->
    <section class="bg-white p-6 rounded shadow p-4 border-2 border-gray-300 rounded bg-gray-50 shadow-md mb-4">
        <h2 class="text-xl font-semibold mb-4">Request Details</h2>
        <c:choose>
            <c:when test="${empty requestDetails}">
                <p class="text-red-500">No details found.</p>
            </c:when>
            <c:otherwise>
                <div class="overflow-y-auto max-h-60 border rounded">
                    <table class="w-full text-sm table-auto border-collapse ">
                        <thead class="bg-gray-100 sticky top-0">
                            <tr>
                                <th class="text-center">Image</th>
                                <th class="text-left">Material</th>
                                <th class="text-center">ReqQuantity</th>
                                <th class="text-center">Actual Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalQty" value="0"/>
                            <c:set var="totalActual" value="0"/>
                            <c:forEach var="d" items="${requestDetails}">
                                <tr class="border-t">
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${not empty d.image}">
                                                <img src="${pageContext.request.contextPath}/assets/images/materials/${d.image}" alt="${d.materialName}" class="w-12 mx-auto"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/assets/images/materials/default.png" alt="No Image" class="w-12 mx-auto"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-left">${d.materialName}</td>
                                    <td class="text-center">${d.quantity}</td>
                                    <td class="text-center">${d.actualQuantity}</td>
                                </tr>
                                <c:set var="totalQty" value="${totalQty + d.quantity}"/>
                                <c:set var="totalActual" value="${totalActual + d.actualQuantity}"/>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- Summary -->
    <section class="p-4 border-2 border-gray-300 rounded bg-gray-50 shadow-md mb-4">
        <h2 class="font-semibold mb-2">Summary</h2>
        <p>Total ReQuantity: <strong>${totalQty}</strong> — ActualQuantity: <strong>${totalActual}</strong></p>
    </section>

    <section class="bg-white p-6 rounded shadow">
        <div class="flex items-center justify-between mb-4">
            <h2 class="text-xl font-semibold">Actual Progress</h2>
            <a href="RequestUpdateServlet?requestId=${request.requestId}"
               class="bg-green-500 text-white px-4 py-2 rounded">
                Create Task Slip
            </a>
        </div>


        <div class="flex flex-col items-center mb-4">

            <p class="text-gray-600 font-medium mb-2">
                Total slips: <strong>${totalSlips}</strong>
            </p>

            <div class="flex space-x-2">
                <button onclick="prevPage()" class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300"><</button>
                <button onclick="nextPage()" class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300">></button>
            </div>
        </div>


        <div id="pagesContainer">
            <c:set var="count" value="0"/>
            <c:set var="pageIndex" value="0"/>
            <c:forEach var="entry" items="${taskLogsByDate.entrySet()}">
                <c:forEach var="task" items="${entry.value}">
                    <c:if test="${count % 2 == 0}">
                        <c:if test="${count != 0}">
                        </div>
                    </c:if>

                    <div class="task-page" style="display: none;">
                    </c:if>

                    <!-- Task Box -->
                    <div class="p-4 border-2 border-gray-300 rounded bg-gray-50 shadow-md mb-4">
                        <div class="flex justify-between text-sm mb-2">
                            <span class="font-semibold">Process #${task.taskId} – ${task.requestTypeName}</span>
                            <span><fmt:formatDate value="${task.createdAt}" pattern="dd-MM-yyyy HH:mm"/></span>
                        </div>
                        <p class="text-sm mb-2"><strong>Staff:</strong> ${task.staffName}</p>
                        <div class="overflow-y-auto max-h-60 border rounded">
                            <table class="w-full text-sm table-auto border-collapse  " >
                                <thead class="bg-gray-100 sticky top-0">
                                    <tr>
                                        <th class="text-center w-12">STT</th>
                                        <th class="text-left">Material</th>
                                        <th class="text-center">Actual Quantity</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${task.slipDetails}" varStatus="index">
                                        <tr class="border-t">
                                            <td class="text-center">${index.index + 1}</td>
                                            <td class="text-left">${s.materialName}</td>
                                            <td class="text-center">${s.quantity}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <c:set var="count" value="${count + 1}"/>
                </c:forEach>
            </c:forEach>
        </div> <!-- close last .task-page -->
</div>
</section>
<script>
    let currentPage = 0;
    const pages = document.querySelectorAll('.task-page');

    function showPage(index) {
        pages.forEach((page, i) => {
            page.style.display = (i === index) ? 'block' : 'none';
        });
    }

    function nextPage() {
        if (currentPage < pages.length - 1) {
            currentPage++;
            showPage(currentPage);
        }
    }

    function prevPage() {
        if (currentPage > 0) {
            currentPage--;
            showPage(currentPage);
        }
    }

    // Show first page on load
    if (pages.length > 0)
        showPage(0);
</script>

</div>
