<%-- 
    Document   : login
    Created on : 02-mar-2021, 19:09:47
    Author     : ACER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Login</h1>
        <form method="POST" action="LoginServlet">
            <input type="text" placeholder="user" name="username" value="${param.username}"/>
            <br/>
            <input type="password" placeholder="pass" name="password"/>
            <br/>
            <input type="submit" name="submit" value="INICIAR SESIÓN"/>
        </form>
        <c:if test="${requestScope.error != null}">
            <p style="color: red">${requestScope.error}</p>
        </c:if>
    </body>
</html>
