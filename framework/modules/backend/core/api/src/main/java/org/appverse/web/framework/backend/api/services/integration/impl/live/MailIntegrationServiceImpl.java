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
package org.appverse.web.framework.backend.api.services.integration.impl.live;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AttachmentDTO;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.api.services.integration.MailIntegrationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@SuppressWarnings("rawtypes")
@Service("mailIntegrationService")
public class MailIntegrationServiceImpl extends AbstractIntegrationService
		implements MailIntegrationService {

	@AutowiredLogger
	private static Logger logger;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	private static final String INVALID_ADDRESSES = "Invalid Addresses";
	private static final String ENCODING = "UTF-8";

	@Override
	public void sendMail(String from, String to, String subject,
			String templateLocation, Map model) throws Exception {
		sendMail(from, new String[] { to }, null, subject, templateLocation,
				model);
	}

	@Override
	public void sendMail(String from, String to, String subject,
			String templateLocation, Map model,
			ArrayList<AttachmentDTO> attachments) throws Exception {
		sendMail(from, new String[] { to }, null, subject, templateLocation,
				model, attachments);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void sendMail(String from, String[] to, String[] copyTo,
			String subject, String templateLocation, Map model)
			throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		if (copyTo != null) {
			helper.setCc(copyTo);
		}
		helper.setSubject(subject);
		String text = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,ENCODING ,templateLocation, model);
		helper.setText(text, true);
		try {
			this.mailSender.send(message);
		} catch (MailSendException sendMailException) {
			Exception[] messageExceptions = sendMailException
					.getMessageExceptions();
			for (Exception messageException : messageExceptions) {
				if (messageException instanceof SendFailedException) {
					SendFailedException sendFailedException = (SendFailedException) messageException;
					if (sendFailedException.getMessage().equals(
							INVALID_ADDRESSES)) {
						Address[] invalidAddresses = sendFailedException
								.getInvalidAddresses();
						List<String> invalidAddressList = new ArrayList<String>();
						for (Address invalidAddress : invalidAddresses) {
							String invalidAddressString = invalidAddress
									.toString();
							invalidAddressList.add(invalidAddressString);
						}
						List<String> validToAdressesList = new ArrayList<String>();
						if (to != null && to.length > 0) {
							for (String address : to) {
								if (!invalidAddressList.contains(address)) {
									validToAdressesList.add(address);
								}
							}
						}

						List<String> validCopyToAdressesList = new ArrayList<String>();
						if (copyTo != null && copyTo.length > 0) {
							for (String address : copyTo) {
								if (!invalidAddressList.contains(address)) {
									validCopyToAdressesList.add(address);
								}
							}
						}

						String[] validToAdressesArray = new String[validToAdressesList
								.size()];
						validToAdressesList.toArray(validToAdressesArray);
						helper.setTo(validToAdressesList
								.toArray(validToAdressesArray));

						String[] validCopyToAdressesArray = new String[validCopyToAdressesList
								.size()];
						validCopyToAdressesList
								.toArray(validCopyToAdressesArray);
						helper.setCc(validCopyToAdressesList
								.toArray(validCopyToAdressesArray));
						for (String invalidAddress : invalidAddressList) {
							logger.error("Mail not sended to " + invalidAddress
									+ "due its a invalid address");
						}
						this.mailSender.send(message);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendMail(String from, String[] to, String[] copyTo,
			String subject, String templateLocation, Map model,
			ArrayList<AttachmentDTO> attachments) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		if (copyTo != null) {
			helper.setCc(copyTo);
		}
		helper.setSubject(subject);
		String text = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,ENCODING, templateLocation, model);
		helper.setText(text, true);
		for (AttachmentDTO attachmentDTO : attachments) {
			helper.addAttachment(attachmentDTO.getAttachmentName(),
					attachmentDTO.getAttachment());
		}
		try {
			this.mailSender.send(message);
		} catch (MailSendException sendMailException) {
			Exception[] messageExceptions = sendMailException
					.getMessageExceptions();
			for (Exception messageException : messageExceptions) {
				if (messageException instanceof SendFailedException) {
					SendFailedException sendFailedException = (SendFailedException) messageException;
					if (sendFailedException.getMessage().equals(
							INVALID_ADDRESSES)) {
						Address[] invalidAddresses = sendFailedException
								.getInvalidAddresses();
						List<String> invalidAddressList = new ArrayList<String>();
						for (Address invalidAddress : invalidAddresses) {
							String invalidAddressString = invalidAddress
									.toString();
							invalidAddressList.add(invalidAddressString);
						}
						List<String> validToAdressesList = new ArrayList<String>();
						if (to != null && to.length > 0) {
							for (String address : to) {
								if (!invalidAddressList.contains(address)) {
									validToAdressesList.add(address);
								}
							}
						}

						List<String> validCopyToAdressesList = new ArrayList<String>();
						if (copyTo != null && copyTo.length > 0) {
							for (String address : copyTo) {
								if (!invalidAddressList.contains(address)) {
									validCopyToAdressesList.add(address);
								}
							}
						}

						String[] validToAdressesArray = new String[validToAdressesList
								.size()];
						validToAdressesList.toArray(validToAdressesArray);
						helper.setTo(validToAdressesList
								.toArray(validToAdressesArray));

						String[] validCopyToAdressesArray = new String[validCopyToAdressesList
								.size()];
						validCopyToAdressesList
								.toArray(validCopyToAdressesArray);
						helper.setCc(validCopyToAdressesList
								.toArray(validCopyToAdressesArray));
						for (String invalidAddress : invalidAddressList) {
							logger.error("Mail not sended to " + invalidAddress
									+ "due its a invalid address");
						}
						this.mailSender.send(message);
					}
				}
			}
		}
	}
}
