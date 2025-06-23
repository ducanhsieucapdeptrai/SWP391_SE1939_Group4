<%-- 
    Document   : MaterialDetail.jsp
    Created on : Jun 4, 2025, 4:11:41 AM
    Author     : anhdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Material Details</title>
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                margin: 0;
                background: #f4f6fc;
            }

            .header {
                background: linear-gradient(90deg, #7b2ff7, #f107a3);
                color: white;
                padding: 30px;
                border-radius: 10px 10px 0 0;
            }

            .header h1 {
                margin: 0;
                font-size: 30px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .detail-grid {
                display: grid;
                grid-template-columns: 1fr 1.5fr;
                gap: 30px;
                background: white;
                border-radius: 0 0 10px 10px;
                padding: 40px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .image-box {
                background: #f5f5f5;
                border-radius: 10px;
                height: 300px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 50px;
                color: #ccc;
            }

            .info-box h2 {
                margin: 0;
                font-size: 26px;
                color: #2c3e50;
            }

            .tag-new {
                background: #ffeaa7;
                color: #d35400;
                font-weight: bold;
                padding: 3px 10px;
                border-radius: 5px;
                font-size: 13px;
                margin-left: 10px;
            }

            .info-table {
                margin-top: 20px;
            }

            .info-table p {
                margin: 10px 0;
                font-size: 16px;
            }

            .label {
                font-weight: bold;
                color: #555;
                width: 160px;
                display: inline-block;
            }

            .price {
                color: green;
                font-weight: bold;
                font-size: 18px;
            }

            .stock-dot {
                height: 10px;
                width: 10px;
                background-color: green;
                border-radius: 50%;
                display: inline-block;
                margin-right: 6px;
            }

            .btn-back {
                display: inline-block;
                margin-top: 20px;
                background-color: #636e72;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 8px;
            }

            .btn-edit {
                display: inline-block;
                margin-top: 20px;
                background-color: #0984e3;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 8px;
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>
                    <span>&#128230;</span> Material Details
                </h1>
                <p>Complete information about the selected material</p>
            </div>
            <div class="detail-grid">
                <div class="image-box">
                    <c:choose>
                        <c:when test="${not empty m.image}">
                            <img src="${pageContext.request.contextPath}/assets/images/materials/${m.image}" 
                                 alt="Material Image" 
                                 style="max-width: 100%; max-height: 100%;">
                        </c:when>
                        <c:otherwise>
                            <span>&#128247;</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="info-box">
                    <h2>${m.materialName}
                        <c:if test="${m.statusName eq 'New'}">
                            <span class="tag-new">&#9888; NEW</span>
                        </c:if>
                    </h2>
                    <div class="info-table">
                        <p><span class="label"># Material ID:</span> #${m.materialId}</p>
                        <p><span class="label">Category:</span> ${m.categoryName} → ${m.subCategoryName}</p>
                        <p><span class="label">Price:</span> <span class="price">
                                <fmt:formatNumber value="${m.price}" type="currency"  groupingUsed="true" currencySymbol=""/>
                                đ
                            </span></p>
                        <p><span class="label">Current Stock:</span> <span class="stock-dot"></span>${m.quantity} units</p>
                        <p><span class="label">Minimum Stock:</span> ${m.minQuantity} units</p>
                        <p><span class="label">Created Date:</span> <fmt:formatDate value="${m.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                        <p><span class="label">Last Updated:</span> <fmt:formatDate value="${m.updatedAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                        <p><span class="label">Status:</span> ${m.statusName}</p>
                    </div>

                    <!-- Nút Back và Edit -->
                    <div>
                        <a href="${pageContext.request.contextPath}/materiallist" class="btn-back">&larr; Back to Materials</a>
                        <a href="${pageContext.request.contextPath}/editmaterial?materialId=${m.materialId}" class="btn-edit">✏️ Edit</a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>