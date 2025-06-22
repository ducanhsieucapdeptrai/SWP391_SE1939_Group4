<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Purchase Request List</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100 font-sans">

        <div class="max-w-7xl mx-auto p-6">
            <h1 class="text-3xl font-bold text-blue-800 mb-6 border-b pb-2">Purchase Request List</h1>

            <!-- FILTER FORM -->
            <form method="get" action="purchase-request-list" class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6 bg-white p-4 rounded shadow">
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">Status</label>
                    <select name="status" class="w-full border px-3 py-2 rounded">
                        <option value="">All</option>
                        <option value="Pending" ${filterStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Approved" ${filterStatus == 'Approved' ? 'selected' : ''}>Approved</option>
                        <option value="Rejected" ${filterStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
                    </select>
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">Created Date</label>
                    <input type="date" name="createdDate" value="${filterDate}" class="w-full border px-3 py-2 rounded">
                </div>
                <div class="flex items-end col-span-1 md:col-span-2">
                    <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded w-full md:w-auto">
                        Filter
                    </button>
                </div>
            </form>

            <!-- TABLE -->
            <div class="overflow-auto bg-white rounded shadow">
                <table class="min-w-full table-auto text-sm text-left text-gray-700">
                    <thead class="bg-blue-600 text-white">
                        <tr>
                            <th class="px-4 py-3">No</th>
                            <th class="px-4 py-3">Request ID</th>
                            <th class="px-4 py-3">Created By</th>
                            <th class="px-4 py-3">Created Date</th>
                            <th class="px-4 py-3">Total</th>
                            <th class="px-4 py-3">Status</th>
                            <th class="px-4 py-3">Note</th>
                            <th class="px-4 py-3">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="po" items="${poList}" varStatus="i">
                            <tr class="purchase-request-row border-t hover:bg-gray-50 transition">
                                <td class="px-4 py-2">${i.index + 1}</td>
                                <td class="px-4 py-2">${po.requestId}</td>
                                <td class="px-4 py-2">${po.createdByName}</td>
                                <td class="px-4 py-2">
                                    <fmt:formatDate value="${po.createdDate}" pattern="yyyy-MM-dd HH:mm" />
                                </td>
                                <td class="px-4 py-2 text-right text-green-700 font-semibold">
                                    <fmt:formatNumber value="${po.totalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> VND
                                </td>
                                <td class="px-4 py-2">
                                    <span class="px-2 py-1 rounded-full text-xs font-semibold
                                          ${po.status == 'Pending' ? 'bg-yellow-100 text-yellow-700' : ''}
                                          ${po.status == 'Approved' ? 'bg-green-100 text-green-700' : ''}
                                          ${po.status == 'Rejected' ? 'bg-red-100 text-red-700' : ''}">
                                        ${po.status}
                                    </span>
                                </td>
                                <td class="px-4 py-2">${po.note}</td>
                                <td class="px-4 py-2">
                                    <a href="${pageContext.request.contextPath}/purchase-order-detail?id=${po.poId}"
                                       class="text-blue-600 hover:underline font-medium">Detail</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- PHÂN TRANG -->
            <div class="mt-4 flex justify-center gap-2" id="pagination"></div>
        </div>

        <!-- SCRIPT PHÂN TRANG -->
        <script>
            const rowsPerPage = 5;
            const rows = document.querySelectorAll(".purchase-request-row");
            const pagination = document.getElementById("pagination");
            let currentPage = 1;

            function renderPagination(totalPages) {
                pagination.innerHTML = "";

                const createBtn = (label, page, disabled = false, isActive = false) => {
                    const btn = document.createElement("button");
                    btn.innerText = label;
                    btn.className = "px-3 py-1 border rounded " +
                            (disabled ? "text-gray-400 cursor-not-allowed" :
                                    (isActive ? "bg-blue-600 text-white" : "bg-white text-gray-700 hover:bg-gray-100"));
                    if (!disabled)
                        btn.onclick = () => showPage(page);
                    return btn;
                };

                pagination.appendChild(createBtn("<<", 1, currentPage === 1));
                pagination.appendChild(createBtn("<", currentPage - 1, currentPage === 1));

                for (let i = 1; i <= totalPages; i++) {
                    pagination.appendChild(createBtn(i, i, false, i === currentPage));
                }

                pagination.appendChild(createBtn(">", currentPage + 1, currentPage === totalPages));
                pagination.appendChild(createBtn(">>", totalPages, currentPage === totalPages));
            }

            function showPage(page) {
                const totalPages = Math.ceil(rows.length / rowsPerPage);
                if (page < 1)
                    page = 1;
                if (page > totalPages)
                    page = totalPages;
                currentPage = page;

                const start = (page - 1) * rowsPerPage;
                const end = start + rowsPerPage;

                rows.forEach((row, index) => {
                    row.style.display = (index >= start && index < end) ? "" : "none";
                });

                renderPagination(totalPages);
            }

            if (rows.length > 0)
                showPage(1);
        </script>

    </body>
</html>
