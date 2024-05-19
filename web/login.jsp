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
</head>
<body>
<h2>Для продолжения необходимо авторизоваться.</h2>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
%>
<%= errorMessage %>
<%
    }
%><p></p>
<form action="login" method="post">
    <label for="insurancePolicyNumber">Номер страхового полиса (ОМС):</label>
    <input type="text" id="insurancePolicyNumber" name="insurancePolicyNumber" required><br><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br><br>

    <button type="submit">Войти</button>
</form>
</body>
</html>
