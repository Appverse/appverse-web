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
package org.appverse.web.framework.backend.api.helpers.log;

import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jInit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		String log4jLocation = config.getInitParameter("log4j-init-file");
		if (log4jLocation == null) {
			failbackConfiguration();
		}else{
			configureAndWatchByGettingDOMURL(log4jLocation);
		}
		super.init(config);
	}
	
	
	private void configureAndWatchByGettingDOMURL(String log4jLocation){
		URL url = this.getClass().getResource(
				log4jLocation.substring(log4jLocation.indexOf(':') + 1));
		if (url != null) {
			try {
				DOMConfigurator.configureAndWatch(url.getPath());
			} catch (Exception e) {
				configureAndWatchByGettingPropertyURL(log4jLocation);
			}
		} else {
			failbackConfiguration();
		}		
	}	
	
	private void configureAndWatchByGettingPropertyURL(String log4jLocation){
		URL url = this.getClass().getResource(
				log4jLocation.substring(log4jLocation.indexOf(':') + 1));
		if (url != null) {
			try {
				PropertyConfigurator.configureAndWatch(url.getPath());
			} catch (Exception e) {
				failbackConfiguration();
			}
		} else {
			failbackConfiguration();
		}		
	}
	
	private void failbackConfiguration(){
		BasicConfigurator.configure();
	}
}