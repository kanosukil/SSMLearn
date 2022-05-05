<%--
  Created by IntelliJ IDEA.
  User: VHBin
  Date: 2021/11/18
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>乱码</title>
    <meta charset="UTF-8">
</head>
<body>
    <%
        String id = request.getParameter("id");
        String pwd = request.getParameter("pwd");
        out.println("id:"+id+"<br>");
        out.print("pwd:"+pwd);
    %>
<%--
    没有Filter将会中文乱码
    --%>
</body>
</html>
