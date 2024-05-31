<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 15.05.2024
  Time: 23:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="data.dto.PatientProfileDto" %>
<%@ page import="data.dto.PatientAppointmentListItemDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="data.dto.PatientRecordResultDto" %>
<html>
<head>
    <title>Профиль</title>
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
        }
        p {
            font-size: 16px;
            margin: 10px 0;
        }
        .label {
            font-weight: bold;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            font-family: Arial, sans-serif;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
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
    </style>
</head>
<body>
<div class="container">
    <h2>Ваш профиль:</h2>
    <%
        PatientProfileDto patient = (PatientProfileDto) request.getSession().getAttribute("patient");

        if (patient != null) {
    %>
    <p><span class="label">ФИО:</span> <%= patient.fullName() %></p>
    <p><span class="label">Номер СНИЛС:</span> <%= patient.insurancePolicyNumber() %></p>
    <p><span class="label">Дата рождения:</span> <%= patient.dateOfBirth() %></p>
    <p><span class="label">Пол:</span>
        <%= patient.gender() != null ? patient.gender() : "не указан" %></p>
    <p><span class="label">Номер телефона: (+7) </span>
        <%= patient.phoneNumber() != null ? patient.phoneNumber() : "не указан" %></p>
    <p><span class="label">Адрес:</span>
        <%= patient.address() != null ? patient.address() : "не указан" %></p>

    <%
        PatientRecordResultDto removedRecord = (PatientRecordResultDto) request.getAttribute("removedRecord");

        if (removedRecord != null) {
    %>
    <p> Ваша запись на <%= removedRecord.dateTime().toLocalDate()%>
        в <%= removedRecord.dateTime().toLocalTime()%> была отменена. </p>
    <%
        }
    %>

    <h2>Ближайшие записи на прием:</h2>
    <%
        List<PatientAppointmentListItemDto> patientRecords = (List<PatientAppointmentListItemDto>) request.getAttribute("patientRecords");

        if (patientRecords != null && !patientRecords.isEmpty()) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    %>
    <table>
        <thead>
        <tr>
            <th>Дата и время</th>
            <th>Врач</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (PatientAppointmentListItemDto record : patientRecords) {
        %>
        <tr>
            <td><%= record.dateTime().format(dateFormatter) %> в
                <%= record.dateTime().format(timeFormatter) %></td>
            <td><%= record.doctorFullName() %></td>
            <td>
                <form action="<%= request.getContextPath() %>/patient_profile" method="post">
                    <input type="hidden" name="removedRecordId" value="<%= record.recordId() %>" />
                    <button type="submit" class="button">Отменить запись</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
    } else {
    %>
    <p>В ближайшее время вы не записывались ни к какому врачу.</p>
    <%
            }
        }
    %>
</div>
</body>
</html>


