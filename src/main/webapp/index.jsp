<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="me.zuif.hw18.request.Request" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Homework18</title>
</head>

<body>
<%ArrayList<Request> repository = (ArrayList<Request>) request.getAttribute("repo");%>
<table>
    <tr>
        <th>ip</th>
        <th>user-agent</th>
        <th>time</th>
    </tr>
    <% if(repository != null) {for (Request repoRequest : repository) { %>
    <tr>
        <td <% if (repoRequest.getIp().equals(request.getRemoteAddr())) { %> style="font-weight:1000" <% } %>>
            <%out.println(repoRequest.getIp()); %></td>
        <td <% if (repoRequest.getIp().equals(request.getRemoteAddr())) { %> style="font-weight:1000" <% } %>>
            <%out.println(repoRequest.getUserAgent()); %></td>
        <td><%
            out.println(repoRequest.getCreatedTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))); %></td>
    </tr>
    <%}}%>
</table>
</body>
</html>