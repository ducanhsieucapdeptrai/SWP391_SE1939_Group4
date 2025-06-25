<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

<style>
    .main-container { max-width: 1200px; margin: auto; padding: 20px; }
    .header-card { background-color: #1e3a8a; color: white; padding: 30px; border-radius: 15px; margin-bottom: 30px; }
    .content-card { background: white; padding: 30px; border-radius: 15px; box-shadow: 0 5px 20px rgba(0,0,0,0.08); }
    .form-actions { display: flex; justify-content: center; gap: 20px; margin-top: 30px; flex-wrap: wrap; }
    .btn-primary { background-color: #1e3a8a; color: white; border-radius: 25px; padding: 12px 30px; font-weight: 600; }
    .btn-danger { background-color: #dc2626; color: white; border-radius: 25px; padding: 12px 30px; font-weight: 600; }
    .btn-gray { background-color: #6b7280; color: white; border-radius: 25px; padding: 12px 30px; font-weight: 600; text-decoration: none; }
</style>

<div class="header-card">
    <h1 class="text-3xl font-bold flex items-center gap-2">
        <i class="fas fa-edit text-white text-3xl"></i> Request Update
    </h1>
    <p class="mt-1 text-white text-sm md:text-base opacity-90">Review and update request actual quantity</p>
</div>

<c:if test="${not empty message}">
    <div class="mb-6 p-4 rounded-lg flex items-center gap-3
        ${messageType eq 'success' ? 'bg-green-500 text-white' : messageType eq 'warning' ? 'bg-yellow-500 text-white' : 'bg-red-500 text-white'}">
        <i class="fas ${messageType eq 'success' ? 'fa-check-circle' : messageType eq 'warning' ? 'fa-exclamation-triangle' : 'fa-times-circle'} text-2xl"></i>
        <div><h3 class="font-semibold text-lg">${message}</h3></div>
    </div>
</c:if>

<div class="content-card">
    <form method="post" action="RequestUpdateServlet">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="totalItems" value="${fn:length(requestItems)}" />

        <table class="min-w-full text-sm table-auto mb-6">
            <thead class="bg-gray-200">
                <tr>
                    <th class="px-3 py-2">Type</th>
                    <th class="px-3 py-2">Material</th>
                    <th class="px-3 py-2">Requested Qty</th>
                    <th class="px-3 py-2">Actual Qty</th>
                    <th class="px-3 py-2">Stock</th>
                    <th class="px-3 py-2">Price (Dong)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${requestItems}" varStatus="i">
                    <tr class="hover:bg-gray-50">
                        <td class="px-3 py-2 text-center">${item.requestTypeName}</td>
                        <td class="px-3 py-2 font-semibold">${item.materialName}</td>
                        <td class="px-3 py-2 text-center">${item.quantity}</td>
                        <td class="px-3 py-2 text-center">
                            <c:choose>
                                <c:when test="${editMode}">
                                    <input type="number" name="actualQuantity_${i.index}" value="${item.actualQuantity}" min="0"
                                           class="border px-2 py-1 rounded w-20 text-center" />
                                </c:when>
                                <c:otherwise>
                                    <input type="number" value="${item.actualQuantity}" class="border px-2 py-1 w-20 text-center bg-gray-100" disabled />
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="materialId_${i.index}" value="${item.materialId}" />
                        </td>
                        <td class="px-3 py-2 text-center">${item.stockQuantity}</td>
                        <td class="px-3 py-2 text-center">${item.price}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="form-actions">
            <c:choose>
                <c:when test="${actionMode eq 'start'}">
                    <button type="submit" class="btn-primary" name="mode" value="start">Start Review</button>
                </c:when>

                <c:when test="${actionMode eq 'reviewing'}">
                    <button type="submit" class="btn-primary" name="mode" value="accept">Accept</button>
                    <label for="reject-modal-toggle" class="btn-danger cursor-pointer">Reject</label>
                </c:when>

                <c:when test="${actionMode eq 'done'}">
                    <c:choose>
                        <c:when test="${editMode}">
                            <button type="submit" class="btn-primary" name="mode" value="save">Save</button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn-primary" name="mode" value="edit">Edit</button>
                        </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>
            <a href="reqlist" class="btn-gray">Back</a>
        </div>
    </form>
</div>

<!-- Modal confirm reject -->
<input type="checkbox" id="reject-modal-toggle" class="hidden peer" />
<div class="fixed inset-0 bg-black bg-opacity-40 z-40 hidden peer-checked:block" aria-hidden="true"></div>
<div class="fixed inset-0 flex items-center justify-center z-50 hidden peer-checked:flex">
    <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <h2 class="text-lg font-semibold mb-4">Confirm Rejection</h2>
        <form method="post" action="RequestUpdateServlet">
            <input type="hidden" name="requestId" value="${requestId}" />
            <input type="hidden" name="mode" value="reject" />
            <textarea name="decisionNote" rows="3" placeholder="Enter reason for rejection"
                      class="w-full border border-gray-300 rounded px-3 py-2 mb-4" required></textarea>
            <div class="flex justify-end gap-4">
                <label for="reject-modal-toggle" class="btn-gray cursor-pointer">Cancel</label>
                <button type="submit" class="btn-danger">Confirm Reject</button>
            </div>
        </form>
    </div>
</div>