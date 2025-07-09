<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Actual Records Detail - Request #${requestInfo.requestId}</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 20px;
                background-color: #f5f5f5;
            }
            .container {
                max-width: 1200px;
                margin: auto;
            }
            h3 {
                margin-bottom: 0;
            }
            .card {
                background-color: white;
                border: 1px solid #ddd;
                border-radius: 4px;
                margin-bottom: 20px;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }
            .card-header {
                background-color: #007bff;
                color: white;
                padding: 10px 15px;
                font-weight: bold;
            }
            .card-body {
                padding: 15px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            table th, table td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: left;
            }
            table th {
                background-color: #f1f1f1;
            }
            .text-muted {
                color: #6c757d;
            }
            .group-header {
                background-color: #e9ecef;
                padding: 10px;
                font-weight: bold;
            }
            .pagination {
                display: flex;
                justify-content: center;
                list-style: none;
                padding-left: 0;
            }
            .pagination li {
                margin: 0 5px;
            }
            .pagination a {
                padding: 6px 12px;
                border: 1px solid #ccc;
                text-decoration: none;
                color: #007bff;
            }
            .pagination .active a {
                background-color: #007bff;
                color: white;
                border-color: #007bff;
            }
            .pagination .disabled a {
                color: #ccc;
                pointer-events: none;
            }
            .btn {
                display: inline-block;
                padding: 6px 12px;
                background-color: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 4px;
            }
            .btn-secondary {
                background-color: #6c757d;
            }
            .btn-success {
                background-color: #28a745;
            }
            .btn-sm {
                padding: 4px 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h3>Task Detail - Actual Report</h3>
                    <p class="text-muted">Request ID: ${requestInfo.requestId}</p>
                </div>
                <div>
                    <a href="javascript:history.back()" class="btn btn-secondary btn-sm">&larr; Back</a>
                </div>
            </div>

            <!-- Request Info & Summary -->
            <div style="display: flex; gap: 20px;">
                <div style="flex: 1;">
                    <div class="card">
                        <div class="card-header">Request Information</div>
                        <div class="card-body">
                            <p><strong>Type:</strong> ${requestInfo.requestTypeName}</p>
                            <p><strong>Requested By:</strong> ${requestInfo.requestedByName}</p>
                            <p><strong>Date:</strong> <fmt:formatDate value="${requestInfo.requestDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                            <p><strong>Status:</strong> ${requestInfo.status}</p>
                            <c:if test="${not empty requestInfo.note}">
                                <p><strong>Note:</strong> ${requestInfo.note}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header">Summary</div>
                        <div class="card-body">
                            <div style="display: flex; justify-content: space-around;">
                                <div>
                                    <h5>${fn:length(requestDetails)}</h5>
                                    <p>Items</p>
                                </div>
                                <div>
                                    <c:set var="totalQty" value="0"/>
                                    <c:forEach var="d" items="${requestDetails}">
                                        <c:set var="totalQty" value="${totalQty + d.quantity}"/>
                                    </c:forEach>
                                    <h5>${totalQty}</h5>
                                    <p>Total Qty</p>
                                </div>
                                <div>
                                    <c:set var="totalValue" value="0"/>
                                    <c:forEach var="d" items="${requestDetails}">
                                        <c:set var="totalValue" value="${totalValue + d.totalValue}"/>
                                    </c:forEach>
                                    <h5><fmt:formatNumber value="${totalValue}" pattern="#\,##0"/>₫</h5>
                                    <p>Total Value</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Request Details -->
                <div style="flex: 1.5;">
                    <div class="card">
                        <div class="card-header">Request Details</div>
                        <div class="card-body" style="max-height: 400px; overflow-y: auto;">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Image</th>
                                        <th>Material</th>
                                        <th>Qty</th>
                                        <th>Price</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${requestDetails}">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty d.image}">
                                                        <img src="${pageContext.request.contextPath}/assets/images/materials/${d.image}" width="40" height="40" style="object-fit:cover;"/>
                                                    </c:when>
                                                    <c:otherwise><span class="text-muted">N/A</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${d.materialName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.price}" pattern="#\,##0"/>₫</td>
                                            <td><strong><fmt:formatNumber value="${d.totalValue}" pattern="#\,##0"/>₫</strong></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Actual Records -->
            <div class="card">
                <div class="card-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>Actual Records</span>
                    <a href="RequestUpdateServlet?requestId=${requestInfo.requestId}" class="btn btn-success btn-sm">Update Records</a>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not hasRecords}">
                            <div class="text-muted" style="text-align:center; padding: 20px;">No import/export records yet.</div>
                        </c:when>
                        <c:otherwise>
                            <c:set var="prevDate" value=""/>
                            <c:forEach var="d" items="${actualDetails}" varStatus="st">
                                <c:set var="rec" value="${actualLists[st.index]}"/>
                                <c:choose>
                                    <c:when test="${not empty rec.importDate}">
                                        <fmt:formatDate var="recDate" value="${rec.importDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatDate var="recDate" value="${rec.exportDate}" pattern="dd/MM/yyyy"/>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${st.index == 0 || recDate != prevDate}">
                                    <c:if test="${st.index != 0}"></tbody></table></div></c:if>
                                <div class="group-header">${recDate}</div>
                                <div class="table-responsive">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Material</th>
                                                <th>Requested</th>
                                                <th>Actual</th>
                                                <th>Note</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </c:if>
                                        <tr>
                                            <td>${st.index + 1}</td>
                                            <td>${d.materialName}</td>
                                            <td>${requestDetails[st.index].quantity}</td>
                                            <td>${d.quantity}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty rec.note}">
                                                        ${rec.note}
                                                    </c:when>
                                                    <c:otherwise><span class="text-muted">—</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <c:set var="prevDate" value="${recDate}"/>
                                    </c:forEach>
                                </tbody></table></div>

                        <!-- Pagination -->
                        <ul class="pagination">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a href="?requestId=${requestInfo.requestId}&page=${currentPage-1}">&laquo; Prev</a>
                            </li>
                            <c:forEach var="p" begin="${startPage}" end="${endPage}">
                                <li class="page-item ${p == currentPage ? 'active' : ''}">
                                    <a href="?requestId=${requestInfo.requestId}&page=${p}">${p}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a href="?requestId=${requestInfo.requestId}&page=${currentPage+1}">Next &raquo;</a>
                            </li>
                        </ul>
                        <div class="text-muted text-center">
                            Showing ${startIndex+1}–${endIndex} of ${totalRecords}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>
