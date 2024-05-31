<%@ page import="utils.validation.validators.ErrorField" %>
<%@ page import="data.dto.ErrorFieldsDto" %><%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 15.05.2024
  Time: 16:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h2 {
            color: #4CAF50;
            text-align: center;
        }
        .error-message {
            background-color: #f2f2f2;
            color: #333;
            border: 1px solid #ddd;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        form {
            max-width: 500px;
            margin: 0 auto;
            background-color: #f9f9f9;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        input[type="text"], input[type="password"], input[type="date"], select {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h2>Регистрация</h2>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
%>
<div class="error-message">
    <p><%= errorMessage %></p>
    <%
        ErrorFieldsDto errorFields = (ErrorFieldsDto) request.getAttribute("errorFields");

        if (errorFields != null && !errorFields.errorFields().isEmpty()) {
            for (ErrorField field : errorFields.errorFields()) {
    %>
    <p><%= field.fieldName() %>: <%= field.errorMessage() %></p>
    <%
                }
            }
        }
    %>
</div>

<form action="registration" method="post">
    <div>
        <label for="insurancePolicyNumber">Номер страхового полиса (ОМС):</label>
        <input type="text" id="insurancePolicyNumber" name="insurancePolicyNumber" required>

        <label for="fullName">ФИО:</label>
        <input type="text" id="fullName" name="fullName" required>

        <label for="dateOfBirth">Дата рождения:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" required>

        <label for="gender">Пол:</label>
        <select id="gender" name="gender">
            <option value="male">Мужской</option>
            <option value="female">Женский</option>
            <option value="none" selected>Не выбран</option>
        </select>

        <label for="phoneNumber">Номер телефона:</label>
        <input type="text" id="phoneNumber" name="phoneNumber">

        <label for="address">Адрес:</label>
        <input type="text" id="address" name="address">

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>
    </div>

    <button type="submit">Зарегистрироваться</button>
</form>
</body>
</html>


