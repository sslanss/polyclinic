<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 13.05.2024
  Time: 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Профиль</title>
</head>
<body>
<h1>Информация о специалисте:</h1>
    <p>Имя: ${doctor.fullName}</p>
    <p>Специализация: ${doctor.speciality}</p>
<button type="submit"><a href = "${pageContext.request.contextPath}/appointment">Записаться к врачу</a></button>
</body>
</html>
