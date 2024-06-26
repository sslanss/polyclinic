<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 15.05.2024
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="data.dto.PatientRecordResultDto" %>
<html>
<head>
    <title>Запись</title>
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
            font-weight: normal;
        }
        p {
            font-size: 16px;
        }
        .doctor-info {
            font-weight: bold;
            margin-top: 10px;
        }
        .message {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <%
        PatientRecordResultDto record = (PatientRecordResultDto) request.getAttribute("patientRecord");

        if (record != null) {
    %>
    <h2>Вы записаны на прием.</h2>
    <p class="doctor-info">Вас примет: <%= record.doctorFullName()%></p>
    <p class="doctor-info">Дата приема: <%= record.dateTime().toLocalDate() %></p>
    <p class="doctor-info">Время приема: <%= record.dateTime().toLocalTime() %></p>
    <p class="message">Пожалуйста, не задерживайтесь. Если не успеваете, отмените запись в
        <a href="<%= request.getContextPath() %>/patient_profile">личном кабинете.</a></p>
    <%
    } else {
    %>
    <p>На выбранное вами время уже нельзя записаться. Пожалуйста, запишетесь на свободное.</p>
    <%
        }
    %>
</div>
</body>
</html>

