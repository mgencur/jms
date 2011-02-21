<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<!DOCTYPE html>
<html>
<head>
   <title>Messaging example</title>
   <link rel="stylesheet" href="style/style.css" type="text/css" media="screen" />
</head>
<body>
<f:view>

	<div class="header">
	  <span id="title">StatusSender</span>
	</div>

	<div class="content"><h:form>
		<h:panelGrid columns="1">
			<h:outputText value="Name:" />
			<h:panelGroup>
				<h:inputText id="username" value="#{sendingClient.status.user}" size="20"
					required="true" requiredMessage="Please enter your name..." />&nbsp;
				<h:message for="username" errorClass="errorMessage" />
			</h:panelGroup>
			<h:outputText value="Status:" />
			<h:inputTextarea id="statusupdate"
				value="#{sendingClient.status.statusMessage}" rows="10"
				styleClass="enterstatus"
				validatorMessage="Maximum length of the status message is 160">
				<f:validateLength maximum="160" />
			</h:inputTextarea>
			<h:message for="statusupdate" errorClass="errorMessage" />
			<h:commandButton id="sendButton" value="Send" action="#{sendingClient.sendStatusUpdate}" />
		</h:panelGrid>
	</h:form></div>

</f:view>        
</body>
</html> 
