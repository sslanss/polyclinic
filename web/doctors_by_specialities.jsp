<%@ page import="data.dto.DoctorListItemDto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 12.05.2024
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Список специалистов</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1 {
            color: #4CAF50;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        ul li {
            background-color: #f2f2f2;
            margin: 5px 0;
            padding: 10px;
            border-radius: 5px;
        }
        a {
            text-decoration: none;
            font-size: 16px;
            color: #333;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Для записи доступны следующие специалисты:</h1>
<ul>
    <%
        List<DoctorListItemDto> doctorsList = (List<DoctorListItemDto>) request.getAttribute("doctorsBySpecialityList");
        if (doctorsList != null && !doctorsList.isEmpty()) {
            for (DoctorListItemDto doctor : doctorsList) {
    %>
    <li>
        <a href="<%= request.getContextPath() %>/doctor?id=<%= doctor.doctorId() %>">
            <%= doctor.doctorFullName() %>
        </a>
    </li>
    <%
        }
    } else {
    %>
    <li>Нет доступных специалистов для записи.</li>
    <%
        }
    %>
</ul>
</body>
</html>


