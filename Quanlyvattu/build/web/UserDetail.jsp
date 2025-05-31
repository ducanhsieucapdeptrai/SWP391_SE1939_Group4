<%-- 
    Document   : UserDetail
    Created on : May 27, 2025, 10:15:07 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Detail</title>
        <style>
            .user-detail-form {
                width: 50%;
                margin: 20px auto;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-label {
                display: inline-block;
                width: 100px;
                font-weight: bold;
            }
            .form-value {
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <div class="user-detail-form">
            <h2>User Details</h2>
            <div class="form-group">
                <span class="form-label">ID:</span>
                <span class="form-value">${user.id}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Name:</span>
                <span class="form-value">${user.name}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Gender:</span>
                <span class="form-value">${user.gender}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Phone:</span>
                <span class="form-value">${user.phone}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Email:</span>
                <span class="form-value">${user.email}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Role:</span>
                <span class="form-value">${user.role}</span>
            </div>
            <div class="form-group">
                <span class="form-label">Status:</span>
                <span class="form-value">${user.isactive}</span>
            </div>
            <div class="form-group">
                <a href="javascript:history.back()">Back to List</a>
            </div>
        </div>
    </body>
</html>
