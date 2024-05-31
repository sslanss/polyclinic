<%@ page import="data.dto.SpecialityDto" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 13.05.2024
  Time: 22:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Специальности</title>
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Specialists</title>
</head>
<body>
<h1>В поликлинике ведут прием специалисты следующего профиля:</h1>
<ul>
    <%
        List<SpecialityDto> specialitiesList = (List<SpecialityDto>) request.getAttribute("specialitiesList");
        if (specialitiesList != null && !specialitiesList.isEmpty()) {
            for (SpecialityDto speciality : specialitiesList) {
    %>
    <li>
        <a href="<%= request.getContextPath() %>/doctors?specialityId=<%= speciality.specialityId() %>">
            <%= speciality.specialityName() %>
        </a>
    </li>
    <%
        }
    } else {
    %>
    <li>Список специалистов недоступен.</li>
    <%
        }
    %>
</ul>
</body>
</html>


