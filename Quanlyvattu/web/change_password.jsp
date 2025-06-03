<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Change Password</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f2f2f2;
            }
            .form-container {
                width: 400px;
                margin: 80px auto;
                padding: 30px;
                background-color: #fff;
                border: 1px solid #ccc;
                border-radius: 6px;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h4 class="text-center mb-4">Change Password</h4>
            <form method="post" action="change-password">
                <div class="mb-3">
                    <label for="email">Email address:</label>
                    <input type="email" class="form-control" id="email" name="email" value="${param.email}" required>
                </div>
                <div class="mb-3">
                    <label for="currentPassword">Current password:</label>
                    <input type="password" class="form-control" id="currentPassword" name="oldPassword" required>
                </div>
                <div class="mb-3">
                    <label for="newPassword">New password:</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword">Confirm new password:</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Change Password</button>
                    <a href="homepage.jsp" class="btn btn-secondary">Back</a>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger mt-3">${error}</div>
                </c:if>

                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success mt-3">${sessionScope.message}</div>
                    <c:remove var="message" scope="session" />
                </c:if>

            </form>
        </div>
    </body>
</html>


