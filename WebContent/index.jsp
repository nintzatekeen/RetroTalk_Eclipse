<%-- 
    Document   : index
    Created on : 20 feb. 2021, 18:06:02
    Author     : ACER
--%>

<%@page import="beans.Category"%>
<%@page import="java.util.List"%>
<%@page import="dao.DaoRetro"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <ul>
            <%
                List<Category> list = dao.DaoRetro.getCategories();
                for(Category c : list) {
            %>
            <li><a href='threads.jsp?cat=<%=c.getId()%>'><%=c.getName()%></a></li>
            <%
            }
            %>
        </ul>
    </body>
</html>
