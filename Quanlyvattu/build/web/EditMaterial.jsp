<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Material</title>
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
            padding: 30px;
            border-radius: 0 0 10px 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin: 30px;
        }

        .detail-grid img {
            max-width: 100%;
            border-radius: 10px;
        }

        .detail-grid .info label {
            display: block;
            margin: 15px 0 5px;
            font-weight: bold;
        }

        .detail-grid .info input,
        .detail-grid .info textarea,
        .detail-grid .info select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        .actions {
            text-align: center;
            margin: 20px;
        }

        .actions button {
            background-color: #7b2ff7;
            color: white;
            border: none;
            padding: 10px 25px;
            font-size: 16px;
            border-radius: 6px;
            cursor: pointer;
            margin: 0 10px;
        }

        .actions button:hover {
            background-color: #5e23b5;
        }

        .actions .cancel-btn {
            background-color: #6c757d;
        }

        .actions .cancel-btn:hover {
            background-color: #545b62;
        }

        .error {
            color: red;
            text-align: center;
            margin: 10px;
            padding: 10px;
            background-color: #ffe6e6;
            border: 1px solid #ff0000;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Edit Material</h1>
    </div>

    <!-- Display error message if exists -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Fixed form action to match servlet mapping -->
    <form action="updatematerial" method="post">
        <div class="detail-grid">
            <!-- Material Image -->
            <div>
                <img src="${material.image}" alt="Material Image" />
               
            </div>

            <!-- Material Info Form -->
            <div class="info">
                <!-- Hidden field for material ID -->
                <input type="hidden" name="materialId" value="${material.materialId}" />

                <label for="materialName">Material Name</label>
                <input type="text" id="materialName" name="materialName" value="${material.materialName}" required />

                <label for="subCategoryId">Sub Category ID</label>
                <input type="number" id="subCategoryId" name="subCategoryId" value="${material.subCategoryId}" required />

                <label for="statusId">Status ID</label>
                <input type="number" id="statusId" name="statusId" value="${material.statusId}" required />

                <label for="quantity">Quantity</label>
                <input type="number" id="quantity" name="quantity" value="${material.quantity}" min="0" required />

                <label for="minQuantity">Minimum Quantity</label>
                <input type="number" id="minQuantity" name="minQuantity" value="${material.minQuantity}" min="0" required />

                <label for="price">Price</label>
                <input type="number" id="price" name="price" value="${material.price}" min="0" step="0.01" required />

                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4">${material.description}</textarea>
            </div>
        </div>

        <div class="actions">
            <button type="submit">Save Changes</button>
            <button type="button" class="cancel-btn" onclick="window.history.back()">Cancel</button>
        </div>
    </form>
</body>
</html>