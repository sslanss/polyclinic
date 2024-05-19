<%@ page import="data.dto.DoctorProfileDto" %><%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 13.05.2024
  Time: 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter, java.util.*" %>
<%@ page import="data.dto.DoctorFreeTimeForRecordDto" %>
<html>
<head>
    <title>Профиль</title>
    <style>
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
        .button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 5px;
        }
        .button a {
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
<%
    DoctorProfileDto doctor = (DoctorProfileDto) request.getAttribute("doctor");
    List<DoctorFreeTimeForRecordDto> freeTimeSlots = (List<DoctorFreeTimeForRecordDto>) request.getAttribute("freeTime");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<h1>Информация о специалисте:</h1>
<p>Имя: <%= doctor.getFullName() %></p>
<p>Специализация: <%= doctor.getSpeciality() %></p>
<button class="button"><a href="<%= request.getContextPath() %>/record?doctorId=<%= doctor.getDoctorId() %>">Записаться к врачу</a></button>

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
        for (DoctorFreeTimeForRecordDto freeTimeSlot : freeTimeSlots) {
            String date = freeTimeSlot.freeDateTime().format(dateFormatter);
            String time = freeTimeSlot.freeDateTime().format(timeFormatter);
            String dateTimeParam = freeTimeSlot.freeDateTime().format(DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm"));
    %>
    <tr class="available">
        <td><a href="<%= request.getContextPath() %>/record?doctorId=<%= doctor.getDoctorId() %>&dateTime=<%=
        dateTimeParam %>"><%= date %></a></td>
        <td><a href="<%= request.getContextPath() %>/record?doctorId=<%= doctor.getDoctorId() %>&dateTime=<%=
        dateTimeParam %>"><%= time %></a></td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>
