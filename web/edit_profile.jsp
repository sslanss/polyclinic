<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 15.05.2024
  Time: 23:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактирование профиля</title>
</head>
<body>
<form action="edit_profile" method="post">
    <h2>Пожалуйста, заполните информацию о себе.</h2>
    <div>
        <label for="fullName">ФИО:</label>
        <input type="text" id="fullName" name="fullName" required><br><br>

        <label for="birthDate">Дата рождения:</label>
        <input type="date" id="birthDate" name="birthDate" required><br><br>

        <label for="gender">Пол:</label>
        <select id="gender" name="gender" required>
            <option value="male">Мужской</option>
            <option value="female">Женский</option>
        </select><br><br>

        <label for="phoneNumber">Номер телефона:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" required><br><br>
    </div>

    <button type="submit">Сохранить</button>
</form>
</body>
</html>
