<%--
  Created by IntelliJ IDEA.
  User: VHBin
  Date: 2021/11/18
  Time: 17:25
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.bookstore.spring.entity.Book" %>

<html>
<head>
<%--    link 添加样式引用 --%>
    <link href="${pageContext.request.contextPath}/statics/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/statics/css/style.css" rel="stylesheet" />
    <title>实验九-SpringMVC网上书店</title>
</head>
<body onload="initAJAX()"> <%-- initAJAX()函数存在于commons.js之中--%>
<%--    script 添加js引用   --%>
    <script src="${pageContext.request.contextPath}/statics/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/statics/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/statics/js/commons.js"></script>

<%--    ajax 技术的使用  --%>
    <script language="JavaScript">
        function showBook(categoryID) {
            xmlHttp.open("GET", "${pageContext.request.contextPath}/book/" + categoryID, true);
            xmlHttp.onreadystatechange = function () {
                if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                    const data = xmlHttp.responseText;
                    const obj = JSON.parse(data);
                    let list = '';
                    for (const i in obj) {
                        const bookName = obj[i].name;
                        const bookDescription = obj[i].description;
                        list +=
                            '<div class="col-sm-9 col-md-3">' +
                                '<div class="thumbnail" >' +
                                    '<img src="${pageContext.request.contextPath}/statics/images/book.jpg" alt="略览图">' +
                                    '<div class="caption">' +
                                        '<h4 id="name">' + bookName + '</h4>' +
                                            '<p id="desc" class="desc" title="' + bookDescription + '">' + bookDescription + '</p>' +
                                            '<p>' +
                                                '<a href="#" class="btn btn-primary" role="button">加入购物车</a>' +
                                                '<a href="#" class="btn btn-default" role="button">查看详情</a>' +
                                            '</p>' +
                                    '</div>' +
                                '</div>' +
                            '</div>'
                    }
                    document.getElementById("book").innerHTML = list;
                }
            }
            xmlHttp.send();
        };
    </script>
    <%--整齐排版--%>
    <style type="text/css">
        .desc{
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
        }
    </style>
    <%-- 模态窗口 --%>
    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"></div>
                <div class="modal-body"></div>
                <div class="modal-footer"></div>
            </div>
        </div>
    </div>


<%--    项目布局规划
        使用 div 标签布局
            最上方显示书店相关信息 及用户相关操作
            下方分为两个区域:
                左 显示书籍类别信息
                右 显示对应左侧类别书籍的信息
    --%>
<%-- header --%>
    <div class="header">
        <div class="container">
            <div class="row">
                <div class="login span4">
                    <h1><a href="">欢迎来到<strong>我的</strong>书店</a>
                    <span class="red">.</span></h1>
                </div>
                <div class="links span8">
                    <a class="login" href="${pageContext.request.contextPath}/views/HTML/login.html" rel="tooltip" data-placement="bottom"
                       data-toggle="modal" data-target="#myModal">
<%--                        <img src="${pageContext.request.contextPath}/statics/images/login.png" alt="登录">--%>
                    </a>
                    <a class="register" href="" rel="tooltip" data-placement="bottom">
<%--                        <img src="${pageContext.request.contextPath}/statics/images/register.png" alt="注册">--%>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!--下方div控制左右-->
    <div class="row">
        <!--左侧div-->
        <div class="col-md-3" id="category">
            <ul class="nav nav-list" id="list">
                <li class="navbar-header">书籍类型</li><br>
                <c:forEach items="${category}" var="category">
                    <li>
                        <a href="javascript:showBook(${category.id})">${category.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <!--右侧div-->
        <div class="col-md-9" id="book">

        </div>
    </div>



</body>
</html>
