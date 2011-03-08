<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<!DOCTYPE html>
<html>
<head>
   <title>Messaging example</title>
   <link rel="stylesheet" href="style/style.css" type="text/css" media="screen" />
   <meta http-equiv="refresh" content="20" />
</head>
<body>
<f:view>
<h:form >
   
   <div class="watchmenu">
		<div class="watchtitle">StatusWatcher</div>
		<div class="menuitem"><h:selectBooleanCheckbox id="changeFollowing" onchange="submit();" 
		     value="#{receivingClient.followAll}" 
		     valueChangeListener="#{receivingClient.changeFollowing}"/>Follow all</div>
		<div class="divider"></div>
		<div class="menuitem"><h:commandLink id="receive" value="Receive" action="#{receivingClient.receive}"/></div>
		<div class="divider"></div>
		<div class="menuitem"><h:commandLink id="history" value="History" action="#{receivingClient.history}"/></div>
	</div>
	
   <div class="watchcontent">
      <h:dataTable id="receivedStatuses" value="#{receivingClient.receivedStatuses}" var="status">
			<h:column>
			   <span class="username"><h:outputText value="#{status.username}"/></span>
			   <span class="statuslabel"> changed his/her status </span>
			   <span class="date"><h:outputText value=" #{status.friendlyDate}"/></span>
			   <span class="statuslabel"> to ...</span>
            <div class="statusupdate">
               <h:outputText value="#{status.statusMessage}"></h:outputText>
            </div>
			</h:column>
		</h:dataTable>
   </div>
   
</h:form>       
</f:view>        
</body>
</html> 
