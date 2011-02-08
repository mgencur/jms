<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Messaging example</title>
    </head>

    <body>
       
        <c:out value="${messagingClient.message}"/>
        
    </body>
</html> 
