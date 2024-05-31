<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 26.05.2024
  Time: 21:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Успешная регистрация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #4CAF50;
        }
        .message {
            background-color: #dff0d8;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 18px;
            color: #333;
        }
        .link-container {
            margin-top: 20px;
        }
        .link-container a {
            text-decoration: none;
            font-size: 16px;
            color: #4CAF50;
            border-bottom: 1px solid #4CAF50;
        }
        .link-container a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Успешная авторизация</h1>
<div class="message">
    Вы успешно авторизованы!
</div>
<div class="link-container">
    <p>
        <a href="<%= request.getContextPath() %>/specialities">К списку специалистов</a>
    </p>
    <p>
        <a href="<%= request.getContextPath() %>/patient_profile">Просмотреть ваш профиль</a>
    </p>
</div>
</body>
</html>

