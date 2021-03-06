<%-- 
    Document   : threads
    Created on : 05-mar-2021, 8:00:50
    Author     : dw2
--%>

<%@page import="beans.ForumThread"%>
<%@page import="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${param.cat != null}">
                <%
                    try {
                        Integer cat = Integer.parseInt(request.getParameter("cat"));
                        Integer pag = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 0;
                        Collection<ForumThread> threads = dao.DaoRetro.getThreads(cat, pag);
                        out.print("<ul>");
                        for (ForumThread thread : threads) {
                %>
            <li><a href='thread.jsp?thread=<%=thread.getId()%>'><%=thread.getTitle()%></a></li>
                <%
                    }
                    out.print("</ul>");
                } catch (Exception ex) {
                %>
            <p style="color:red">
                PÁGINA NO ENCONTRADA
            </p>
            <%
                }

            %>
        </c:when>
        <c:otherwise>
            <p style="color:red">
                PÁGINA NO ENCONTRADA
            </p>
        </c:otherwise>
    </c:choose>
</body>
</html>
