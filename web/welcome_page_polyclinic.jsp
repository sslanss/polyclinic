<%--
  Created by IntelliJ IDEA.
  User: sidel
  Date: 13.05.2024
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>БУЗ ВО «Воронежская городская поликлиника N8»</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1, h2 {
            color: #4CAF50;
        }
        p, ul {
            font-size: 16px;
            color: #333;
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
            color: #4CAF50;
            text-decoration: none;
        }
        .contact-info {
            background-color: #c6d5c7;
            padding: 15px;
            border: 1px solid #b8d5bb;
            border-radius: 5px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h1>Добро пожаловать в Городскую поликлинику №8.</h1>
<p>Мы рады приветствовать вас на на сайте Воронежской городской поликлинники №8. Наша поликлиника предлагает широкий спектр медицинских услуг.</p>
<p>Запись к специалистам осуществляется через регистратуру, госуслуги, а также непосредственно
    <a href="${pageContext.request.contextPath}/specialities">на сайте</a>.</p>
<p>Пожалуйста, ознакомьтесь с нашим расписанием работы и контактными данными:</p>

<h2>График работы поликлиники:</h2>
<ul>
    <li>Понедельник-пятница: с 08-00 до 20-00</li>
    <li>Суббота: с 09-00 до 15-00</li>
    <li>Воскресенье: выходной</li>
</ul>

<h2>Вызов врача на дом:</h2>
<ul>
    <li>Понедельник-пятница: с 7-30 до 12-00</li>
    <li>Суббота: с 08-00 до 12-00</li>
</ul>

<h2>График работы регистратуры:</h2>
<ul>
    <li>Понедельник-пятница: с 07-30 до 20-00</li>
    <li>Суббота: с 8-30 до 15-00</li>
</ul>

<h2>График работы процедурного кабинета:</h2>
<ul>
    <li>Понедельник-пятница: с 07-30 до 20-00</li>
    <li>Суббота: с 9-00 до 15-00</li>
</ul>

<h2>График работы пункта неотложной медицинской помощи:</h2>
<ul>
    <li>Понедельник-пятница: с 8-00 до 20-00</li>
    <li>Суббота: с 8-00 до 15-00</li>
</ul>

<h2>Контактная информация:</h2>
<div class="contact-info">
    <p>Адрес: ул. Примерная, д. 123, г. Примерный</p>
    <p>Телефон: +7 (123) 456-78-90</p>
    <p>Email: info@polyclinic8.ru</p>
</div>
</body>
</html>
