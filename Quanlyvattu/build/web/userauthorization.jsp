<%-- 
    Document   : userauthorization
    Created on : May 27, 2025, 10:34:15 PM
    Author     : anhdu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Authorization Matrix</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Times New Roman', serif;
        }
        
        .document-container {
            background-color: white;
            margin: 20px auto;
            padding: 40px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            max-width: 1200px;
            border: 1px solid #dee2e6;
        }
        
        .document-title {
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .module-section {
            margin-bottom: 40px;
            page-break-inside: avoid;
        }
        
        .module-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
            padding-bottom: 5px;
            border-bottom: 2px solid #333;
        }
        
        .permission-table {
            width: 100%;
            border-collapse: collapse;
            margin: 0;
            font-size: 13px;
        }
        
        .permission-table th,
        .permission-table td {
            border: 1px solid #333;
            padding: 8px 12px;
            text-align: left;
        }
        
        .permission-table th {
            background-color: #f1f1f1;
            font-weight: bold;
            text-align: center;
        }
        
        .function-column {
            background-color: #f8f9fa;
            font-weight: 500;
            width: 300px;
        }
        
        .role-header {
            writing-mode: horizontal-tb;
            text-align: center;
            min-width: 80px;
            background-color: #e9ecef;
        }
        
        .checkbox-cell {
            text-align: center;
            width: 80px;
        }
        
        .checkbox-cell input[type="checkbox"] {
            transform: scale(1.2);
        }
        
        .action-buttons {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
        }
        
        .back-link {
            position: absolute;
            top: 20px;
            left: 20px;
        }
        
        @media print {
            .back-link,
            .action-buttons {
                display: none;
            }
            
            .document-container {
                box-shadow: none;
                border: none;
                margin: 0;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <!-- Document Container -->
    <div class="document-container">
        <!-- Back Link -->
        
        
        <!-- Document Title -->
        <div class="document-title">
             <h1 class="text-primary">Authorization Matrix</h1>
            
        </div>
        
        <!-- Main Form -->
        <form method="post" action="user-matrix">
            
            <!-- Loop through each module -->
            <c:forEach var="module" items="${modules}">
                <div class="module-section">
                    
                    <!-- Module Title -->
                    <div class="module-title">
                        ${module.moduleName}
                    </div>
                    
                    <!-- Permission Matrix Table -->
                    <table class="permission-table">
                        
                        <!-- Table Header -->
                        <thead>
                            <tr>
                                <th class="function-column">Function</th>
                                <c:forEach var="role" items="${roles}">
                                    <th class="role-header">${role.roleName}</th>
                                </c:forEach>
                            </tr>
                        </thead>
                        
                        <!-- Table Body -->
                        <tbody>
                            <!-- Loop through functions for this module -->
                            <c:forEach var="func" items="${functions}">
                                <c:if test="${func.moduleId == module.moduleId}">
                                    <tr>
                                        <!-- Function Name -->
                                        <td class="function-column">${func.functionName}</td>
                                        
                                        <!-- Permission Checkboxes -->
                                        <c:forEach var="role" items="${roles}">
                                            <td class="checkbox-cell">
                                                <c:set var="permissionKey" value="${role.roleId}:${func.functionId}" />
                                                <input type="checkbox" 
                                                       name="perm" 
                                                       value="${permissionKey}"
                                                       <c:if test="${roleFunctionPairs.contains(permissionKey)}">checked</c:if>
                                                       <c:if test="${role.roleId == 1 && func.functionId == 34}">disabled</c:if> />
                                            </td>
                                        </c:forEach>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:forEach>
            
            <!-- Action Buttons -->
            <div class="action-buttons">
                <button type="submit" class="btn btn-primary btn-lg">Save Permissions</button>
            </div>
            
        </form>
    </div>
</body>
</html>
