<%@ page import="data.dto.DoctorProfileDto" %><%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 13.05.2024
  Time: 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter, java.util.*" %>
<%@ page import="data.dto.DoctorAvailableTimeDto" %>
<html>
<head>
    <title>Профиль</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1, h2 {
            color: #4CAF50;
        }
        .doctor-info {
            margin-bottom: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .doctor-info p {
            margin: 10px 0;
            font-size: 16px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            font-family: Arial, sans-serif;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .available {
            background-color: #dff0d8;
        }
    </style>
</head>
<body>
<%
    DoctorProfileDto doctor = (DoctorProfileDto) request.getAttribute("doctor");
    List<DoctorAvailableTimeDto> freeTimeSlots = (List<DoctorAvailableTimeDto>) request.getAttribute("freeTime");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<h1>Информация о специалисте</h1>
<div class="doctor-info">
    <p><strong>Имя:</strong> <%= doctor.fullName() %></p>
    <p><strong>Специализация:</strong> <%= doctor.speciality() %></p>
</div>

<h2>Доступное расписание</h2>
<table>
    <thead>
    <tr>
        <th>Дата</th>
        <th>Время</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (DoctorAvailableTimeDto freeTimeSlot : freeTimeSlots) {
            String date = freeTimeSlot.freeDateTime().format(dateFormatter);
            String time = freeTimeSlot.freeDateTime().format(timeFormatter);
            String dateTimeParam = freeTimeSlot.freeDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    %>
    <tr class="available">
        <td><a href="<%= request.getContextPath() %>/record?doctorId=<%= doctor.doctorId() %>&dateTime=<%= dateTimeParam %>"><%= date %></a></td>
        <td><a href="<%= request.getContextPath() %>/record?doctorId=<%= doctor.doctorId() %>&dateTime=<%= dateTimeParam %>"><%= time %></a></td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>
