<%--
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
</head>
<body>
<h2>Регистрация</h2>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
%>
<%= errorMessage %>
<%
    }
%>
<form action="registration" method="post">
    <div>
        <label for="insurancePolicyNumber">Номер страхового полиса (ОМС):</label>
        <input type="text" id="insurancePolicyNumber" name="insurancePolicyNumber" required><br><br>

        <label for="fullName">ФИО:</label>
        <input type="text" id="fullName" name="fullName" required><br><br>

        <label for="dateOfBirth">Дата рождения:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" required><br><br>

        <label for="gender">Пол:</label>
        <select id="gender" name="gender">
            <option value="male">Мужской</option>
            <option value="female">Женский</option>
            <option value="none"selected>Не выбран</option>
        </select><br><br>

        <label for="phoneNumber">Номер телефона:</label>
        <input type="text" id="phoneNumber" name="phoneNumber"><br><br>

        <label for="address">Адрес:</label>
        <input type="text" id="address" name="address"><br><br>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required><br><br>
    </div>

    <button type="submit">Зарегистрироваться</button>
</form>
</body>
</html>
