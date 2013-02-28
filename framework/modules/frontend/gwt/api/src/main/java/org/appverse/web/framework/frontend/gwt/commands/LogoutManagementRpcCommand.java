/*
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
 */
package org.appverse.web.framework.frontend.gwt.commands;

/*
 * LogoutManagementRpcCommandImpl integrates GWT logout with Spring Security.
 * The request builder will post a request to the default Spring Security logout URL "/j_spring_security_logout".
 * Spring Security mechanism will logout the user in the server. If this is done successfully, the GWT status it is 
 * cleared reloading the page. This is perfect as ensures that all data in front end it is cleared and the front end application
 * side is ready for another user to log-in.
 * The proper Spring security will be in charge to show the log-in page after the user has been logged out.
 * 
 * Spring security setup example:
 * 
 * - Spring Security filter in web.xml:
 * 
 * 	<filter>
 *		<filter-name>springSecurityFilterChain</filter-name>
 *		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
 *	</filter>
 * 
 * 	<filter-mapping>
 *		<filter-name>springSecurityFilterChain</filter-name>
 *		<url-pattern>/*</url-pattern>
 *	</filter-mapping>
 * 
 * - Spring security setup example:
 *
 *  <http auto-config='true'>
 *    <!-- http://blog.idm.fr/2010/09/spring-security-redirecting-to-faviconico.html -->
 *    <intercept-url pattern="/login.jsp*" access="IS_AUTHENTICATED_ANONYMOUSLY"/>
 *    <intercept-url pattern="/favicon.ico" access="IS_AUTHENTICATED_ANONYMOUSLY" />
 *    <intercept-url pattern="/**" access="ROLE_APP_ADMIN_CONSOLE_ACCESS" />
 *    <form-login login-page='/login.jsp'/>
 *    <logout logout-success-url="/login.jsp"/>    
 *  </http>	   
 */
public interface LogoutManagementRpcCommand {

	public void onLogout();

}