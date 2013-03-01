<%-- 
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this 
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the AppVerse Public License v2.0 
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 --%>

<%
	String loginTitle = "Login Title";
	String loginWelcome = "Welcome to Appverse Web GWT Showcase";
	String loginUser = "User";
	String loginPassword = "Password";
	String loginSignIn = "Login Sign In";
	String loginSubmit = "Login Submit";
	String loginErrorCredentials = "Login error credentials";
	String loginErrorSession = "Login error session";
%>

<html>
<head>
<title><%=loginTitle%></title>
<style type="text/css">
body {
	font-family: ProximaNovaSemibold, arial, verdana, sans-serif;
	font-size: 11px;
}

table.borderLogin {
	border-color: #0066FF;
	border-style: double;
	border-width: 1px;
	font-family: ProximaNovaSemibold, arial, verdana, sans-serif;
	font-size: 12px;
}

table.greeting {
	border-bottom: 1px dotted #0066FF;
	border-top: 1px dotted #0066FF;
	font-family: ProximaNovaSemibold, arial, verdana, sans-serif;
	font-size: 10px;
	height: 34px;
	margin-top: 7px;
	overflow: hidden;
	text-transform: uppercase;
	width: 924px;
	color: black;
}

td.header {
	background-color: #0066FF;
	border-bottom-color: #0066FF;
	color: white;
	font-size: 12px;
	font-weight: bold;
}

td.margin {
	padding-left: 15px;
}
</style>
</head>
<body onload='document.f.j_username.focus();'>
	<center>
		<table border="0" width="924px">
			<tr>
				<td align="center"><img src="logo_admin.png" border="0" /></td>
			</tr>
			<tr>
				<td>
					<br><br>
				</td>
			</tr>
			<tr>
				<td>
					<table class="greeting">
						<tr>
							<td><%=loginWelcome%></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<br><br>
				</td>
			</tr>			
			<tr>
				<td align="center"><br /> <span style="color: red"> <%
 	out.println("Use test user: 'admin' and test password 'admin' to log in. ");
 	Exception error = (Exception) request
 			.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
 	if (request.getParameter("error") != null)
 		out.println(loginErrorCredentials);
 	else if (request.getParameter("sessionExpired") != null)
 		out.println(loginErrorSession);
 	else if (error != null)
 		out.println(error.getMessage());
 %>
				</span>
					<form name='f' action='j_spring_security_check' method='POST'>
						<table width="400px" class="borderLogin">
							<tr height="30px">
								<td colspan="2" class="header" valign="center">&nbsp;<%=loginSignIn%></td>
							</tr>
							<tr height="40px">
								<td class="margin"><%=loginUser%>:</td>
								<td><input type='text' name='j_username' value=''></td>
							</tr>
							<tr height="40px">
								<td class="margin"><%=loginPassword%>:</td>
								<td><input type='password' name='j_password' /></td>
							</tr>
							<tr height="40px">
								<td colspan='2' align="center"><input name="submit"
									type="submit" value="<%=loginSubmit%>" /></td>
							</tr>
						</table>
					</form></td>
			</tr>
		</table>
	</center>
</body>
</html>