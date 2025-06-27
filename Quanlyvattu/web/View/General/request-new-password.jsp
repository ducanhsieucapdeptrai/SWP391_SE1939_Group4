<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Warehouse Manager</title>
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
            .form-control {
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

                    <!-- 🔔 Notification Messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success" role="alert">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:if>
                </div>

                <form action="request-new-password" method="post">
                    <div class="mb-3">
                        <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                        <input type="tel" name="phone" class="form-control"
                               placeholder="Enter your phone number"
                               pattern="^[0-9]{9,11}$"
                               title="Please enter a valid phone number with 9 to 11 digits"
                               required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email Address <span class="text-danger">*</span></label>
                        <input type="email" name="email" class="form-control"
                               placeholder="Enter your email address"
                               title="Please enter a valid email address"
                               required>
                    </div>

                    <div class="text-center">
                        <button type="submit" class="btn btn-primary w-50 mb-2">Send Request</button>
                    </div>
                    <div class="text-center">
                        <a href="${pageContext.request.contextPath}/View/General/login.jsp" class="btn btn-outline-primary w-50">
                            Back to Login
                        </a>
                    </div>
                </form>
            </div>
        </div>
        <script>
            window.onload = function () {
                const alerts = document.querySelectorAll('.alert');
                if (alerts.length > 0) {
                    setTimeout(() => {
                        alerts.forEach(alert => {
                            alert.style.transition = 'opacity 0.5s ease';
                            alert.style.opacity = '0';
                            setTimeout(() => {
                                alert.style.display = 'none';
                            }, 500);
                        });
                    }, 5000);
                }
            };
        </script>
    </body>
</html>
