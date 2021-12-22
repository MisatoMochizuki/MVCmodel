
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bean.EmployeeBean"%>

<%

    EmployeeBean employeeBean = (EmployeeBean) request.getAttribute("EmployeeBean");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>検索結果</title>
</head>
<body>
    <div align="center">
        <!-- 検索が成功した場合の表示 -->
        <%
            if (employeeBean != null) {
        %>
        <table border="1">
            <tr>
                <th>社員名</th>
                <td><%=employeeBean.getName()%></td>
            </tr>
            <tr>
                <th>コメント</th>
                <td><%=employeeBean.getComment()%></td>
            </tr>
            <tr>
                <th>ログインタイム</th>
                <td><%=employeeBean.getLogin_Time()%></td>
            </tr>
        </table>

        <!--   それ以外の表示（エラーの場合）-->
        <% } else { %>
           <p>IDもしくはパスワードが間違っています</p>
        <% } %>
    </div>
</body>
</html>