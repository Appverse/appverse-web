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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.persistence.exceptions.RemoteCommandManagerException;
import org.eclipse.persistence.internal.sessions.coordination.jms.JMSTopicRemoteConnection;
import org.eclipse.persistence.sessions.coordination.RemoteCommandManager;
import org.eclipse.persistence.sessions.coordination.jms.JMSTopicTransportManager;

/** Customised from EL Topic Manager. */
public class ELJMSTransportManager extends JMSTopicTransportManager {

	private class DBusJMSTopicConnectionFactory implements
			TopicConnectionFactory {

		private final TopicConnectionFactory factory;

		public DBusJMSTopicConnectionFactory(TopicConnectionFactory factory) {
			this.factory = factory;
		}

		@Override
		public Connection createConnection() throws JMSException {
			return factory.createConnection(config.getUser(),
					config.getPassword());
		}

		@Override
		public Connection createConnection(String arg0, String arg1)
				throws JMSException {
			return factory.createConnection(arg0, arg1);
		}

		@Override
		public TopicConnection createTopicConnection() throws JMSException {
			return factory.createTopicConnection(config.getUser(),
					config.getPassword());
		}

		@Override
		public TopicConnection createTopicConnection(String arg0, String arg1)
				throws JMSException {
			return factory.createTopicConnection(arg0, arg1);
		}
	}

	private final ELJMSConfig config;

	public ELJMSTransportManager(RemoteCommandManager rcm) {
		super(rcm);

		config = new ELJMSConfig();
		config.loadConfig();

		setTopicName(config.getTopic());
		setTopicConnectionFactoryName(config.getTopicConnectionFactory());
	}

	@Override
	protected JMSTopicRemoteConnection createConnection(
			boolean isLocalConnectionBeingCreated)
			throws RemoteCommandManagerException {
		Context remoteHostContext = null;

		// TODO send to logging system
		System.out.println("Creating dBusJMS connection...");
		System.out.println(" Initial Context Properties: "
				+ config.getContextProperties());
		System.out.println(" TopicConnectionFactory: "
				+ getTopicConnectionFactoryName());
		System.out.println(" Topic: " + getTopicName());
		System.out.println(" Local: " + isLocalConnectionBeingCreated);

		try {
			remoteHostContext = new InitialContext(
					config.getContextProperties());
			TopicConnectionFactory connectionFactory = getTopicConnectionFactory(remoteHostContext);
			Topic topic = getTopic(remoteHostContext);
			// TopicConnection topicConnection = connectionFactory
			// .createTopicConnection(config.getUser(), config
			// .getPassword());
			return new JMSTopicRemoteConnection(rcm,
					new DBusJMSTopicConnectionFactory(connectionFactory),
					topic, isLocalConnectionBeingCreated,
					reuseJMSTopicPublisher);
		} catch (Exception ex) {
			RemoteCommandManagerException rcmException;
			if (isLocalConnectionBeingCreated) {
				rcmException = RemoteCommandManagerException
						.errorCreatingLocalJMSConnection(config.getTopic(),
								config.getTopicConnectionFactory(),
								 ex);
			} else {
				rcmException = RemoteCommandManagerException
						.errorCreatingJMSConnection(config.getTopic(),
								config.getTopicConnectionFactory(),
								 ex);
			}
			throw rcmException;
		} finally {
			if (remoteHostContext != null) {
				try {
					remoteHostContext.close();
				} catch (NamingException namingException) {
					// ignore
				}
			}
		}
	}
}