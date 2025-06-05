<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String rememberedEmail = "";
    boolean rememberChecked = false;
    jakarta.servlet.http.Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (jakarta.servlet.http.Cookie cookie : cookies) {
            if ("rememberedEmail".equals(cookie.getName())) {
                rememberedEmail = cookie.getValue();
                rememberChecked = true;
                break;
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>Material Management - Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap local -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

        <!-- Custom styles -->
        <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath}/assets/css/remixicon.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet" />

        <style>
            body {
                background: linear-gradient(135deg, #f2f6fc, #e3ebf6);
                font-family: 'Segoe UI', sans-serif;
            }

            .card {
                border-radius: 16px;
                padding: 2.5rem;
                min-width: 450px;
            }

            .btn-primary {
                background-color: #4a90e2;
                border-color: #4a90e2;
                font-size: 1.05rem;
                padding: 0.75rem;
            }

            .btn-primary:hover {
                background-color: #357ab8;
                border-color: #357ab8;
            }

            .btn-outline-danger {
                border-radius: 50px;
                padding: 8px 24px;
                font-size: 1rem;
            }

            .form-control {
                border-radius: 10px;
                font-size: 1.05rem;
                padding: 0.75rem 1rem;
            }

            .form-label {
                font-weight: 600;
                font-size: 1.05rem;
            }

            .form-check-label {
                font-size: 0.95rem;
            }

            .alert {
                font-size: 0.95rem;
                padding: 0.75rem 1rem;
                border-radius: 8px;
            }

            .small-link {
                font-size: 0.95rem;
            }

            .text-muted {
                font-size: 0.95rem;
            }
        </style>
    </head>

    <body>
        <div class="container d-flex justify-content-center align-items-center min-vh-100">
            <div class="card shadow-lg">
                <div class="text-center mb-4">
                    <img src="${pageContext.request.contextPath}/assets/images/logo.png" height="80" alt="logo">
                    <h4 class="mt-3 mb-1">Material Management System</h4>
                </div>

                <c:if test="${not empty errorMsg}">
                    <div id="errorMsg" class="alert alert-danger text-center">${errorMsg}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label">Email address <span class="text-danger">*</span></label>
                        <input type="email" class="form-control" name="email" placeholder="you@example.com"
                               value="<%= rememberedEmail%>" required />
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Password <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" name="password" placeholder="Enter password" required />
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="remember" id="remember"
                                   <%= rememberChecked ? "checked" : ""%> />
                            <label class="form-check-label" for="remember">Remember me</label>
                        </div>
                        <a href="${pageContext.request.contextPath}/request-new-password" class="small-link text-decoration-none">Request new password</a>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary">Sign in</button>
                    </div>
                </form>

                <div class="text-center mt-3">
                    <span class="text-muted">Or sign in with</span>
                    <div class="mt-2">
                        <a href="${pageContext.request.contextPath}/google-login"
                           class="btn btn-outline-danger d-flex align-items-center justify-content-center gap-2">
                            <img src="${pageContext.request.contextPath}/assets/images/google-logo.png" alt="Google" width="20" height="20">
                            <span>Sign in with Google</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script>
            window.onload = function () {
                const errorMsg = document.getElementById("errorMsg");
                if (errorMsg) {
                    setTimeout(() => {
                        errorMsg.style.transition = "opacity 0.5s ease";
                        errorMsg.style.opacity = "0";
                        setTimeout(() => {
                            errorMsg.style.display = "none";
                        }, 500);
                    }, 5000);
                }
            };
        </script>
    </body>
</html>
