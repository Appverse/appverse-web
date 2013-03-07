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
package org.appverse.web.framework.backend.ws.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubHelper {

	private static Logger logger = LoggerFactory.getLogger(StubHelper.class);

	public static void configureEndpoint(String endpointPropertiesFile, String timeoutPropertyName,
			ServiceClient _serviceClient) {
		Properties endpointsProperties = new Properties();
		InputStream endPointsInputStream = StubHelper.class.getResourceAsStream(endpointPropertiesFile);
		try {
			endpointsProperties.load(endPointsInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String accountTimeoutString = (String) endpointsProperties.get(timeoutPropertyName);
		try {
			long accountTimeout = new Long(accountTimeoutString) * 1000;
			_serviceClient.getOptions().setTimeOutInMilliSeconds(accountTimeout);
		} catch (NumberFormatException e) {
			logger.equals("Error login axis account service timeout");
		}
		String endpointProxyEnabled = (String) endpointsProperties.get("endpoint.proxy.enabled");
		if (endpointProxyEnabled != null && endpointProxyEnabled.equals("true")) {
			HttpTransportProperties.ProxyProperties proxyProperties = new HttpTransportProperties.ProxyProperties();
			String endpointProxyHost = endpointsProperties.getProperty("endpoint.proxy.host");
			proxyProperties.setProxyName(endpointProxyHost);
			int endpointProxyPort = new Integer(endpointsProperties.getProperty("endpoint.proxy.port"));
			proxyProperties.setProxyPort(endpointProxyPort);
			_serviceClient.getOptions().setProperty(HTTPConstants.PROXY, proxyProperties);
		}
		if (endpointsProperties.getProperty("endpoint.ignore_SSL_errors") != null
				&& endpointsProperties.getProperty("endpoint.ignore_SSL_errors").equals("true")) {
			// Create a trust manager that does not validate certificate
			// chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };

			// Install the all-trusting trust manager
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			}
		}

		ConfigurationContext configurationContext = _serviceClient.getServiceContext().getConfigurationContext();
		MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(50);
		multiThreadedHttpConnectionManager.setParams(params);
		HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager);
		configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);

	}

	public static String getEndPointName(String endpointPropertiesFile, String endPointName) {
		Properties endpointsProperties = new Properties();
		InputStream endPointsInputStream = StubHelper.class.getResourceAsStream(endpointPropertiesFile);
		try {
			endpointsProperties.load(endPointsInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) endpointsProperties.get(endPointName);
	}

}
