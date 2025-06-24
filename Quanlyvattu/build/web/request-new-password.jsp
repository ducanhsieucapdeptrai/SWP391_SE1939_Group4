<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Request New Password</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

        <style>
            body {
                background: linear-gradient(135deg, #f5f9ff, #e1ecf4);
                font-family: 'Segoe UI', sans-serif;
            }
            .card {
                border-radius: 16px;
                padding: 2rem;
                box-shadow: 0 0 30px rgba(0,0,0,0.05);
            }
            .btn-primary {
                background-color: #4a90e2;
                border-color: #4a90e2;
            }
            .btn-primary:hover {
                background-color: #357ab8;
                border-color: #357ab8;
            }
            .form-label {
                font-weight: 500;
            }
            .form-control, .form-select {
                border-radius: 8px;
            }
        </style>
    </head>
    <body>

        <div class="container d-flex justify-content-center align-items-center min-vh-100">
            <div class="card col-md-6 col-lg-5 bg-white">
                <div class="text-center mb-4">
                    <h3 class="mb-2">Request New Password</h3>
                    <p class="text-muted">Please provide the information below</p>

                    <!-- 🔔 Hiển thị thông báo -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success" role="alert">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>
                </div>

                <form action="request-new-password" method="post">
                    <div class="mb-3">
                        <label class="form-label">Full Name <span class="text-danger">*</span></label>
                        <input type="text" name="fullname" class="form-control" placeholder="Enter your full name" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                        <input type="tel" name="phone" class="form-control" placeholder="Enter your phone number" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email Address <span class="text-danger">*</span></label>
                        <input type="email" name="email" class="form-control" placeholder="Enter your login email" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Role <span class="text-danger">*</span></label>
                        <select name="roleId" class="form-select" required>
                            <option value="">Select your role</option>
                            <c:forEach var="role" items="${roles}">
                                <option value="${role.roleId}">${role.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="text-center">
                        <button type="submit" class="btn btn-primary px-4">Send Request</button>
                    </div>
                    <div class="mt-3 text-center">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-secondary">
                            Back to Login
                        </a>
                    </div>
                </form>
            </div>
        </div>

    </body>
</html>
