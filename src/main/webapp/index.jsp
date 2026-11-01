<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.labtrack.util.PropertiesUtil" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>LabTrack - Home</title>
</head>
<body>
<%--<h1><%= "Hello World!" %>--%>
<%--</h1>--%>
<%--<br/>--%>
<%--<a href="hello-servlet">Hello Servlet</a>--%>


<h1><%= PropertiesUtil.get("app.name")%></h1>
<p>Versión: <%=PropertiesUtil.get("app.version")%></p>
<p>Entorno: <%=PropertiesUtil.get("app.environment")%></p>

</body>
</html>