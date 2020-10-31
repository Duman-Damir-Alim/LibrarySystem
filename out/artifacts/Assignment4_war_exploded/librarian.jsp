<%--
  Created by IntelliJ IDEA.
  User: w2
  Date: 29.10.2020
  Time: 19:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/form.css">
</head>
<body>
<center><h1>Welcome! Enter the data</h1></center>
<div class="login-page">
    <div class="form">
        <form action="/LibServlet" method="post" class="login-form">
            <input type="text" name="username" placeholder="username"/>
            <input type="password" name="password" placeholder="password"/>
            <input type="submit" name="submit" value="Login">
        </form>
    </div>
</div>
</body>
</html>
