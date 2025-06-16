<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Request Update</title>
    <style>
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #ffff99;
        }
        .complete-btn {
            background-color: #90ee90;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            font-weight: bold;
        }
        .success-message {
            color: green;
            text-align: center;
            margin: 10px;
        }
        .error-message {
            color: red;
            text-align: center;
            margin: 10px;
        }
    </style>
</head>
<body>
    <h2>Request on <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) %></h2>
    <h3>Request Quantity</h3>
    <% 
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if ("true".equals(success)) {
    %>
        <div class="success-message">Request updated successfully!</div>
    <% } else if ("true".equals(error)) { %>
        <div class="error-message">Error updating request. Please try again.</div>
    <% } %>
    <form action="<%= request.getContextPath() %>/updateRequestServlet" method="post">
        <table>
            <tr>
                <th>Update Date</th>
                <th>Seq</th>
                <th>Material Name</th>
                <th>Requested Quantity</th>
                <th>Actual Received Quantity</th>
            </tr>
            <tr>
                <td>update day 15</td>
                <td>1</td>
                <td>Hammer</td>
                <td>5</td>
                <td><input type="number" name="quantity_15_1" value="3" min="0" required></td>
            </tr>
            <tr>
                <td></td>
                <td>2</td>
                <td>Hammer</td>
                <td>101</td>
                <td><input type="number" name="quantity_15_2" value="20" min="0" required></td>
            </tr>
            <tr>
                <td>update day 16</td>
                <td>1</td>
                <td>Hammer</td>
                <td>5</td>
                <td><input type="number" name="quantity_16_1" value="5" min="0" required></td>
            </tr>
            <tr>
                <td></td>
                <td>2</td>
                <td>Hammer</td>
                <td>10</td>
                <td><input type="number" name="quantity_16_2" value="20" min="0" required></td>
            </tr>
        </table>
        <div style="text-align: center; margin-top: 20px;">
            <input type="submit" value="Complete Request" class="complete-btn">
        </div>
    </form>
</body>
</html>