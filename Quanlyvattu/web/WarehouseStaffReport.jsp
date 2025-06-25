<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Actual Records Detail  - Request #${requestInfo.requestId}</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card-header {
                background-color: #0d6efd;
                color: white;
                font-weight: 500;
            }
            table th {
                background-color: #f1f1f1;
                font-weight: 500;
            }
            .badge-note {
                font-style: italic;
                color: #6c757d;
            }
            .group-header {
                font-weight: bold;
                background-color: #e9ecef;
                padding: 6px 10px;
            }
            .btn-update {
                background-color: #0d6efd;
                color: white;
            }
            .btn-update:hover {
                background-color: #0b5ed7;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid py-4">

            <!-- Header -->
            <div class="row mb-4">
                <div class="col-11">
                    <h3>Task Detail-Actual Report</h3>
                    <p class="text-muted">Request ID: ${requestInfo.requestId}</p>
                </div>
                <div class="col-1">
                    <a href="javascript:history.back()" class="btn btn-secondary btn-sm shadow-sm">&larr; Back</a>
                </div>
            </div>

            <!-- Request Info and Summary -->
            <div class="row mb-4">
                <div class="col-lg-5">
                    <div class="card mb-3">
                        <div class="card-header">Request Information</div>
                        <div class="card-body">
                            <p><strong>Type:</strong> ${requestInfo.requestTypeName}</p>
                            <p><strong>Requested By:</strong> ${requestInfo.requestedByName}</p>
                            <p><strong>Date:</strong>
                                <fmt:formatDate value="${requestInfo.requestDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                            <p><strong>Status:</strong> ${requestInfo.status}</p>
                            <c:if test="${not empty requestInfo.note}">
                                <p><strong>Note:</strong> ${requestInfo.note}</p>
                            </c:if>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">Summary</div>
                        <div class="card-body">
                            <div class="row text-center">
                                <div class="col">
                                    <h5>${fn:length(requestDetails)}</h5>
                                    <p>Items</p>
                                </div>
                                <div class="col">
                                    <c:set var="totalQty" value="0"/>
                                    <c:forEach var="d" items="${requestDetails}">
                                        <c:set var="totalQty" value="${totalQty + d.quantity}"/>
                                    </c:forEach>
                                    <h5>${totalQty}</h5>
                                    <p>Total Qty</p>
                                </div>
                                <div class="col">
                                    <c:set var="totalValue" value="0"/>
                                    <c:forEach var="d" items="${requestDetails}">
                                        <c:set var="totalValue" value="${totalValue + d.totalValue}"/>
                                    </c:forEach>
                                    <h5><fmt:formatNumber value="${totalValue}" pattern="#,##0"/>₫</h5>
                                    <p>Total Value</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Request Details -->
                <div class="col-lg-7">
                    <div class="card">
                        <div class="card-header">Request Details</div>
                        <div class="table-responsive" style="max-height: 400px;">
                            <table class="table table-striped mb-0">
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
                                                        <img src="${d.image}" width="40" height="40" style="object-fit:cover;"/>
                                                    </c:when>
                                                    <c:otherwise><span class="text-muted">N/A</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${d.materialName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.price}" pattern="#,##0"/>₫</td>
                                            <td><strong><fmt:formatNumber value="${d.totalValue}" pattern="#,##0"/>₫</strong></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Actual Records -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span>Actual Records</span>
                    <a href="RequestUpdateServlet?requestId=${requestInfo.requestId}" class="btn btn-success btn-sm fw-bold shadow-sm">Update Records</a>

                </div>
                <div class="card-body">

                    <c:choose>
                        <c:when test="${not hasRecords}">
                            <div class="text-center text-muted py-5">
                                <p>No import/export records yet.</p>
                            </div>
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
                                    <table class="table table-bordered">
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
                                                    <c:otherwise><span class="badge-note">—</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <c:set var="prevDate" value="${recDate}"/>
                                    </c:forEach>
                                </tbody></table></div>

                        <!-- Always show pagination -->
                        <nav>
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?requestId=${requestInfo.requestId}&page=${currentPage-1}">&laquo; Prev</a>
                                </li>
                                <c:forEach var="p" begin="${startPage}" end="${endPage}">
                                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?requestId=${requestInfo.requestId}&page=${p}">${p}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?requestId=${requestInfo.requestId}&page=${currentPage+1}">Next &raquo;</a>
                                </li>
                            </ul>
                        </nav>
                        <div class="text-center text-muted">
                            Showing ${startIndex+1}–${endIndex} of ${totalRecords}
                        </div>

                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
