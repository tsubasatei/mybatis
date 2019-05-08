<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>员工信息</title>
</head>
<body>
    <h2>员工列表</h2>
    <table>
        <tbody>
            <tr>
                <td>id</td>
                <td>lastName</td>
                <td>email</td>
                <td>gender</td>
            </tr>
            <c:forEach items="${emps}" var="emp" >
                <tr>
                    <td>${emp.id}</td>
                    <td>${emp.lastName}</td>
                    <td>${emp.email}</td>
                    <td>${emp.gender == 0? '女' : '男'}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>