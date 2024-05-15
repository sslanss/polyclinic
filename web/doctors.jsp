<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 12.05.2024
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Список специалистов</title>
</head>
<body>
<h1>Для записи доступны следующие специалисты:</h1>
<ul>
    <c:forEach items="${doctorsList}" var="doctor">
        <li>${doctor.doctorFullName}</li>
    </c:forEach>
</ul>
</body>
</html>
