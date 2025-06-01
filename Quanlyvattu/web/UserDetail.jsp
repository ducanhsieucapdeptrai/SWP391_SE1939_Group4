<%-- 
    Document   : UserDetail
    Created on : May 30, 2025
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Details</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 20px;
            }
            .user-detail-container {
                width: 60%;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            h1 {
                color: #333;
                margin-bottom: 20px;
            }
            .info-section {
                margin-bottom: 30px;
            }
            .info-section h2 {
                border-bottom: 1px solid #eee;
                padding-bottom: 10px;
                margin-bottom: 20px;
                color: #444;
            }
            .info-row {
                display: flex;
                margin-bottom: 15px;
            }
            .info-label {
                font-weight: bold;
                width: 150px;
                color: #555;
            }
            .info-value {
                flex: 1;
            }
            .status-active {
                background-color: #28a745;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                display: inline-block;
            }
            .status-inactive {
                background-color: #dc3545;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                display: inline-block;
            }
            .btn {
                display: inline-block;
                padding: 8px 15px;
                background-color: #6c757d;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                margin-right: 10px;
                font-size: 14px;
            }
            .btn:hover {
                background-color: #5a6268;
            }
            .btn-edit {
                background-color: #007bff;
            }
            .btn-edit:hover {
                background-color: #0069d9;
            }
        </style>
    </head>
    <body>
        <div class="user-detail-container">
            <h1>User Details</h1>

            <div class="info-section">
                <h2>Basic Information</h2>
                <div class="info-row">
                    <div class="info-label">Full Name:</div>
                    <div class="info-value">${user.fullName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Email:</div>
                    <div class="info-value">${user.email}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Phone:</div>
                    <div class="info-value">${user.phone}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Role:</div>
                    <div class="info-value">
                        <c:forEach items="${roleList}" var="role">
                            <c:if test="${role.roleId == user.roleId}">
                                ${role.roleName}
                            </c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="info-row">
                    <div class="info-label">Status:</div>
                    <div class="info-value">
                        <c:choose>
                            <c:when test="${user.isActive}">
                                <span class="status-active">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-inactive">Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div>
                <a href="userlist" class="btn">Back to List</a>
                <a href="user-edit?id=${user.userId}" class="btn btn-edit">Edit</a>
            </div>
        </div>
    </body>
</html>
