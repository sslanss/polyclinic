<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</head>
<body>
<h1>В поликлинике ведут прием специалисты следующего профиля:</h1>
<ul>
    <c:forEach items="${specialitiesList}" var="speciality">
        <li>
            <a href="${pageContext.request.contextPath}/specialities/${speciality.specialityId}/doctors">
                    ${speciality.specialityName} </a>
        </li>
    </c:forEach>
</ul>
</body>
</html>
