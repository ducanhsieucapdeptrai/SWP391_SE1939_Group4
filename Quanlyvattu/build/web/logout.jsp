<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>Material Management - Logout</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <!-- Bootstrap CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <style>
        :root {
            --primary-color: #4a90e2;
            --secondary-color: #357ab8;
            --success-color: #28a745;
            --danger-color: #dc3545;
            --warning-color: #ffc107;
            --info-color: #17a2b8;
            --light-color: #f8f9fa;
            --dark-color: #343a40;
            --gradient-bg: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --card-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.07);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: var(--gradient-bg);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        /* Animated background particles */
        .particles {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: 0;
        }

        .particle {
            position: absolute;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: float 6s ease-in-out infinite;
        }

        .particle:nth-child(1) { width: 8px; height: 8px; left: 10%; animation-delay: 0s; }
        .particle:nth-child(2) { width: 12px; height: 12px; left: 20%; animation-delay: 1s; }
        .particle:nth-child(3) { width: 6px; height: 6px; left: 30%; animation-delay: 2s; }
        .particle:nth-child(4) { width: 10px; height: 10px; left: 40%; animation-delay: 3s; }
        .particle:nth-child(5) { width: 14px; height: 14px; left: 50%; animation-delay: 4s; }
        .particle:nth-child(6) { width: 8px; height: 8px; left: 60%; animation-delay: 5s; }
        .particle:nth-child(7) { width: 16px; height: 16px; left: 70%; animation-delay: 0.5s; }
        .particle:nth-child(8) { width: 6px; height: 6px; left: 80%; animation-delay: 1.5s; }
        .particle:nth-child(9) { width: 10px; height: 10px; left: 90%; animation-delay: 2.5s; }

        @keyframes float {
            0%, 100% { transform: translateY(100vh) rotate(0deg); opacity: 0; }
            10%, 90% { opacity: 1; }
            50% { transform: translateY(-100px) rotate(180deg); }
        }

        .logout-container {
            position: relative;
            z-index: 10;
            max-width: 550px;
            width: 90%;
        }

        .logout-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 24px;
            box-shadow: var(--card-shadow);
            border: 1px solid rgba(255, 255, 255, 0.2);
            padding: 3rem 2.5rem;
            text-align: center;
            position: relative;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        .logout-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
            transition: left 0.5s;
        }

        .logout-card:hover::before {
            left: 100%;
        }

        .logout-icon {
            width: 120px;
            height: 120px;
            margin: 0 auto 2rem;
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: white;
            box-shadow: 0 10px 30px rgba(74, 144, 226, 0.3);
            animation: pulse 2s infinite;
            position: relative;
        }

        .logout-icon::after {
            content: '';
            position: absolute;
            width: 140px;
            height: 140px;
            border: 2px solid var(--primary-color);
            border-radius: 50%;
            opacity: 0.3;
            animation: ring 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }

        @keyframes ring {
            0% { transform: scale(0.8); opacity: 0.7; }
            100% { transform: scale(1.2); opacity: 0; }
        }

        .logout-title {
            font-size: 2.2rem;
            font-weight: 700;
            color: var(--dark-color);
            margin-bottom: 1rem;
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .logout-message {
            font-size: 1.1rem;
            color: #6c757d;
            margin-bottom: 2.5rem;
            line-height: 1.6;
        }

        .user-info {
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            border-radius: 16px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-left: 4px solid var(--primary-color);
        }

        /* User Avatar Section */
        .user-avatar-section {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .user-avatar {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid var(--primary-color);
            box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
            transition: transform 0.3s ease;
            background-color: #f8f9fa;
        }

        .user-avatar:hover {
            transform: scale(1.05);
        }

        .user-avatar-placeholder {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            border: 3px solid var(--primary-color);
            box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.8rem;
            transition: transform 0.3s ease;
        }

        .user-avatar-placeholder:hover {
            transform: scale(1.05);
        }

        .user-details {
            flex: 1;
            text-align: left;
        }

        .user-name {
            font-weight: 600;
            color: var(--dark-color);
            margin-bottom: 0.25rem;
            font-size: 1.1rem;
        }

        .user-email {
            color: #6c757d;
            font-size: 0.95rem;
        }

        .logout-buttons {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .btn-logout {
            flex: 1;
            background: linear-gradient(135deg, var(--danger-color), #c82333);
            border: none;
            color: white;
            padding: 0.875rem 1.5rem;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1.05rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
            position: relative;
            overflow: hidden;
        }

        .btn-logout::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s;
        }

        .btn-logout:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 25px rgba(220, 53, 69, 0.4);
        }

        .btn-logout:hover::before {
            left: 100%;
        }

        .btn-cancel {
            flex: 1;
            background: white;
            border: 2px solid var(--primary-color);
            color: var(--primary-color);
            padding: 0.875rem 1.5rem;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1.05rem;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .btn-cancel::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: var(--primary-color);
            transition: left 0.3s;
            z-index: -1;
        }

        .btn-cancel:hover {
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 6px 25px rgba(74, 144, 226, 0.3);
        }

        .btn-cancel:hover::before {
            left: 0;
        }

        .session-info {
            margin-top: 2rem;
            padding: 1rem;
            background: rgba(74, 144, 226, 0.1);
            border-radius: 12px;
            border: 1px solid rgba(74, 144, 226, 0.2);
        }

        .session-info small {
            color: #6c757d;
            display: block;
            margin-bottom: 0.5rem;
        }

        .time-display {
            font-family: 'Courier New', monospace;
            font-weight: bold;
            color: var(--primary-color);
        }

        .loading-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.9);
            display: none;
            align-items: center;
            justify-content: center;
            border-radius: 24px;
            z-index: 1000;
        }

        .spinner {
            width: 50px;
            height: 50px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid var(--primary-color);
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .success-message {
            display: none;
            color: var(--success-color);
            font-weight: 600;
            margin-top: 1rem;
        }

        .debug-info {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
            font-family: 'Courier New', monospace;
            font-size: 0.85rem;
            color: #6c757d;
            display: none; /* Ẩn debug info trong production */
        }

        /* Confirmation Modal Styles */
        .modal-backdrop {
            background-color: rgba(0, 0, 0, 0.7) !important;
            backdrop-filter: blur(5px);
        }

        .modal-content {
            border: none;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            background: white;
            overflow: hidden;
        }

        .modal-header {
            background: linear-gradient(135deg, #ff6b6b, #ffd93d);
            color: white;
            border-bottom: none;
            padding: 2rem 2rem 1rem;
            text-align: center;
        }

        .modal-body {
            padding: 2rem;
            text-align: center;
        }

        .modal-footer {
            border-top: 1px solid #e9ecef;
            padding: 1rem 2rem 2rem;
            justify-content: center;
            gap: 1rem;
        }

        .confirm-modal-title {
            font-size: 1.5rem;
            font-weight: 700;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .confirm-modal-icon {
            font-size: 2rem;
            margin-bottom: 1rem;
        }

        .confirm-modal-message {
            font-size: 1.1rem;
            color: #6c757d;
            line-height: 1.6;
            margin-bottom: 0;
        }

        .btn-confirm-yes {
            background: linear-gradient(135deg, var(--danger-color), #c82333);
            border: none;
            color: white;
            padding: 0.75rem 2rem;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
            min-width: 100px;
        }

        .btn-confirm-yes:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 25px rgba(220, 53, 69, 0.4);
            color: white;
        }

        .btn-confirm-no {
            background: white;
            border: 2px solid #6c757d;
            color: #6c757d;
            padding: 0.75rem 2rem;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            min-width: 100px;
        }

        .btn-confirm-no:hover {
            background: #6c757d;
            color: white;
            transform: translateY(-2px);
        }

        @media (max-width: 576px) {
            .logout-card {
                padding: 2rem 1.5rem;
            }
            
            .logout-buttons {
                flex-direction: column;
            }
            
            .logout-title {
                font-size: 1.8rem;
            }
            
            .logout-icon {
                width: 100px;
                height: 100px;
                font-size: 2.5rem;
            }

            .user-avatar-section {
                flex-direction: column;
                text-align: center;
            }

            .user-details {
                text-align: center;
            }

            .user-avatar {
                width: 80px;
                height: 80px;
            }

            .user-avatar-placeholder {
                width: 80px;
                height: 80px;
                font-size: 2rem;
            }

            .modal-header, .modal-body, .modal-footer {
                padding-left: 1rem;
                padding-right: 1rem;
            }
        }
    </style>
</head>

<body>
    <div class="particles">
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
    </div>

    <div class="logout-container">
        <div class="logout-card">
            <div class="loading-overlay" id="loadingOverlay">
                <div class="spinner"></div>
            </div>

            <div class="logout-icon">
                <i class="fas fa-sign-out-alt"></i>
            </div>

            <h2 class="logout-title">Sign Out</h2>

            <!-- Debug Information (ẩn trong production) -->
            <div class="debug-info" id="debugInfo">
                <strong>Debug Information:</strong><br>
                currentUser: <c:out value="${currentUser}" default="null"/><br>
                currentUser.userImage: <c:out value="${currentUser.userImage}" default="null"/><br>
                currentUser.fullName: <c:out value="${currentUser.fullName}" default="null"/><br>
                currentUser.email: <c:out value="${currentUser.email}" default="null"/><br>
                userImage from session: <c:out value="${userImage}" default="null"/><br>
                userName from session: <c:out value="${userName}" default="null"/><br>
                userEmail from session: <c:out value="${userEmail}" default="null"/>
            </div>

            <!-- User Info with Avatar -->
            <div class="user-info">
                <div class="user-avatar-section">
                    <c:choose>
                        <c:when test="${not empty currentUser.userImage}">
                            <img src="${pageContext.request.contextPath}/assets/images/UserImage/${currentUser.userImage}" 
                                 alt="User Avatar" 
                                 class="user-avatar"
                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex'; console.log('Image load error: ${currentUser.userImage}');">
                            <div class="user-avatar-placeholder" style="display: none;">
                                <i class="fas fa-user"></i>
                            </div>
                        </c:when>
                        <c:when test="${not empty userImage}">
                            <img src="${pageContext.request.contextPath}/assets/images/UserImage/${userImage}" 
                                 alt="User Avatar" 
                                 class="user-avatar"
                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex'; console.log('Image load error: ${userImage}');">
                            <div class="user-avatar-placeholder" style="display: none;">
                                <i class="fas fa-user"></i>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="user-avatar-placeholder">
                                <i class="fas fa-user"></i>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="user-details">
                        <div class="user-name">
                            <c:choose>
                                <c:when test="${not empty currentUser.fullName}">
                                    <c:out value="${currentUser.fullName}"/>
                                </c:when>
                                <c:when test="${not empty userName}">
                                    <c:out value="${userName}"/>
                                </c:when>
                                <c:otherwise>
                                    Unknown User
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="user-email">
                            <c:choose>
                                <c:when test="${not empty currentUser.email}">
                                    <c:out value="${currentUser.email}"/>
                                </c:when>
                                <c:when test="${not empty userEmail}">
                                    <c:out value="${userEmail}"/>
                                </c:when>
                                <c:otherwise>
                                    unknown@example.com
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="logout-buttons">
                <button type="button" class="btn btn-cancel" onclick="goBack()">
                    <i class="fas fa-arrow-left me-2"></i>Stay Signed In
                </button>
                <button type="button" class="btn btn-logout" onclick="showConfirmation()">
                    <i class="fas fa-sign-out-alt me-2"></i>Sign Out
                </button>
            </div>

            <!-- Session Info -->
            <div class="session-info">
                <small>Current session:</small>
                <div class="time-display" id="currentTime">Loading...</div>
            </div>

            <div class="success-message" id="successMessage">
                <i class="fas fa-check-circle me-2"></i>Signing out successfully...
            </div>

            <!-- Toggle Debug Button (chỉ dành cho development) -->
            <button type="button" class="btn btn-sm btn-outline-secondary mt-3" onclick="toggleDebug()" style="display: none;" id="debugToggle">
                Show Debug Info
            </button>
        </div>
    </div>

    <!-- Confirmation Modal -->
    <div class="modal fade" id="confirmLogoutModal" tabindex="-1" aria-labelledby="confirmLogoutModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="confirm-modal-title" id="confirmLogoutModalLabel">
                        <i class="fas fa-exclamation-triangle confirm-modal-icon"></i>
                        Confirm Sign Out
                    </h5>
                </div>
                <div class="modal-body">
                    <p class="confirm-modal-message">Are you sure you want to sign out of your account?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-confirm-no" onclick="closeConfirmation()">
                        <i class="fas fa-times me-2"></i>No
                    </button>
                    <button type="button" class="btn btn-confirm-yes" onclick="confirmLogout()">
                        <i class="fas fa-check me-2"></i>Yes
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Hidden Form for Logout -->
    <form id="hiddenLogoutForm" method="post" action="${pageContext.request.contextPath}/logout" style="display: none;">
        <input type="hidden" name="action" value="confirm">
    </form>

    <!-- Bootstrap JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Development mode flag - set to false in production
        const isDevelopment = false; // Thay đổi thành true để bật debug mode
        let confirmModal;

        document.addEventListener('DOMContentLoaded', function() {
            updateCurrentTime();
            setInterval(updateCurrentTime, 1000);
            
            // Initialize confirmation modal
            confirmModal = new bootstrap.Modal(document.getElementById('confirmLogoutModal'));
            
            // Show debug toggle button in development mode
            if (isDevelopment) {
                document.getElementById('debugToggle').style.display = 'block';
            }
        });

        function updateCurrentTime() {
            const now = new Date();
            const timeString = now.toLocaleString('vi-VN', { 
                weekday: 'long',
                year: 'numeric',
                month: 'long', 
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            document.getElementById('currentTime').textContent = timeString;
        }

        function toggleDebug() {
            const debugInfo = document.getElementById('debugInfo');
            const toggleBtn = document.getElementById('debugToggle');
            
            if (debugInfo.style.display === 'none' || debugInfo.style.display === '') {
                debugInfo.style.display = 'block';
                toggleBtn.textContent = 'Hide Debug Info';
            } else {
                debugInfo.style.display = 'none';
                toggleBtn.textContent = 'Show Debug Info';
            }
        }

        function goBack() {
            // Go back to dashboard or previous page
            window.location.href = '${pageContext.request.contextPath}/dashboard';
        }

        function showConfirmation() {
            // Show confirmation modal
            confirmModal.show();
        }

        function closeConfirmation() {
            // Close confirmation modal
            confirmModal.hide();
        }

        function confirmLogout() {
            // Close modal and proceed with logout
            confirmModal.hide();
            
            // Show loading overlay
            document.getElementById('loadingOverlay').style.display = 'flex';
            document.getElementById('successMessage').style.display = 'block';
            
            // Submit the hidden form after a short delay
            setTimeout(function() {
                document.getElementById('hiddenLogoutForm').submit();
            }, 500);
        }
        
        // 3D hover effect for the card
        document.querySelector('.logout-card').addEventListener('mousemove', function(e) {
            const card = this;
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            
            const rotateX = (y - centerY) / 20;
            const rotateY = (centerX - x) / 20;
            
            card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        });

        document.querySelector('.logout-card').addEventListener('mouseleave', function() {
            this.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg)';
        });

        // Image error handling
        window.addEventListener('load', function() {
            const images = document.querySelectorAll('.user-avatar');
            images.forEach(function(img) {
                img.addEventListener('error', function() {
                    console.log('Failed to load image:', this.src);
                    this.style.display = 'none';
                    const placeholder = this.nextElementSibling;
                    if (placeholder && placeholder.classList.contains('user-avatar-placeholder')) {
                        placeholder.style.display = 'flex';
                    }
                });
            });
        });
    </script>
</body>
</html>