#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jsp/jstl/core">
 
<h:head>
	<title>Login Page</title>
</h:head>

<h:body onload='document.f.j_username.focus();'>
<h3><h:outputText value="Welcome to ${symbol_dollar}{newProjectName}"/></h3>

<form name='f' action='j_spring_security_check' method='post'>
 <table>
 	<tr><td colspan="2">Please use user: 'admin' and password: 'admin' to log in"</td></tr>
    <tr><td>User:</td><td><input type='text' name='j_username' value=''/></td></tr>
    <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
    <tr><td colspan='2'><input name="submit" type="submit"/></td></tr>
    <tr><td colspan='2'><input name="reset" type="reset"/></td></tr>
  </table>
</form>

</h:body>
</html>