<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 15.05.2024
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторизация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            max-width: 600px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #f9f9f9;
        }
        h2 {
            color: #4CAF50;
        }
        label {
            font-size: 16px;
            display: block;
            margin-top: 10px;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 5px;
        }
        .error-message {
            color: red;
            background-color: #f2dede;
            padding: 10px;
            border: 1px solid red;
            border-radius: 5px;
            margin-top: 10px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Для продолжения необходимо авторизоваться</h2>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div class="error-message"><%= errorMessage %></div>
    <%
        }
    %>
    <form action="login" method="post">
        <label for="insurancePolicyNumber">Номер страхового полиса (ОМС):</label>
        <input type="text" id="insurancePolicyNumber" name="insurancePolicyNumber" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>

        <button class="button" type="submit">Войти</button>
    </form>
</div>
</body>
</html>

