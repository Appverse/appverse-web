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
package org.appverse.web.framework.backend.persistence.services.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Cache service configuration.
 * 
 * <DL>
 * <LI><B>dBusJMSTopic.properties</B>: cache service configuration.
 * <DL>
 * <BR>
 * <LI>user: connection user name
 * <LI>password: connection user password
 * <LI>tcf: topic connection factory JNDI name
 * <LI>topic: topic JNDI name
 * </DL>
 * <LI><B>dBusJMSContext.properties</B>: dBusJMS JNDI initial context
 * properties.
 * </DL>
 */
public class ELJMSConfig {

	private static Logger logger = LoggerFactory
			.getLogger(ELJMSConfig.class);

	/** Cache properties filename. */
	private static final String TOPIC_PROPERTIES_FILENAME = "dBusJMSTopic.properties";

	/** JMS context properties file name. */
	private static final String CONTEXT_PROPERTIES_FILENAME = "dBusJMSContext.properties";

	/** JMS Topic JNDI property name. */
	private static final String JNDI_TOPIC = "topic";

	/** JMS Topic Connection Factory JNDI property name. */
	private static final String JNDI_TCF = "tcf";

	/** JMS user. */
	private static final String CONNECTION_USER = "user";

	/** JMS password. */
	private static final String CONNECTION_PASSWORD = "password";

	/** Connection user name. */
	private String user;

	/** Connection user password. */
	private String password;

	/** Topic connection factory JNDI name. */
	private String tcf;

	/** Topic JNDI name. */
	private String topic;

	/** JNDI context properties. */
	private Properties context;

	/**
	 * Constructor.
	 */
	public ELJMSConfig() {
		user = null;
		password = null;
		tcf = null;
		topic = null;
		context = null;
	}

	/**
	 * Returns the JNDI initial context properties.
	 * 
	 * @return JNDI initial context properties
	 */
	public Properties getContextProperties() {
		return context;
	}

	/**
	 * Returns the connection user password.
	 * 
	 * @return Connection user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the topic JNDI name.
	 * 
	 * @return Topic JNDI name
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Returns the topic connection factory JNDI name.
	 * 
	 * @return Topic connection factory JNDI name
	 */
	public String getTopicConnectionFactory() {
		return tcf;
	}

	/**
	 * Returns the connection user name.
	 * 
	 * @return Connection user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Loads cache configuration. If there is an error loading the configuration
	 * the cache is disabled.
	 */
	public void loadConfig() {

		InputStream is = null;

		try {

			is = getClass().getClassLoader().getResourceAsStream(
					TOPIC_PROPERTIES_FILENAME);
			Properties cacheProperties = new Properties();
			cacheProperties.load(is);
			is.close();
			is = null;

			logger.debug("cache.properties=" + cacheProperties);

			user = cacheProperties.getProperty(CONNECTION_USER);
			logger.debug("cache.properties[user] = " + user);
			password = cacheProperties.getProperty(CONNECTION_PASSWORD);
			logger.debug("cache.properties[password] = " + password);
			tcf = cacheProperties.getProperty(JNDI_TCF);
			logger.debug("cache.properties[tcf] = " + tcf);
			topic = cacheProperties.getProperty(JNDI_TOPIC);
			logger.debug("cache.properties[topic] = " + topic);

			is = getClass().getClassLoader().getResourceAsStream(
					CONTEXT_PROPERTIES_FILENAME);
			context = new Properties();
			context.load(is);
			is.close();
			is = null;
			logger.debug("context.properties=" + context);

		} catch (IOException ioEx) {
			logger.error("Error loading cache configuration", ioEx);
		} catch (Throwable th) {
			logger.error("Unexpected error loading cache configuration", th);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					// ignore errors
				}
			}
		}

	}
}