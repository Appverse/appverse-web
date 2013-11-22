package org.appverse.web.framework.backend.rest.exceptions;

import java.util.HashMap;

import org.appverse.web.framework.backend.api.services.integration.IntegrationException;

public class RestWebAppException extends IntegrationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1947121292176177735L;
	private final String reason;
	private final int statusCode;

	public RestWebAppException(final String reason, final int statusCode) {

		super(statusCode, RestWebAppException.getParams(reason, statusCode), reason);
		this.reason = reason;
		this.statusCode = statusCode;

	}

	public RestWebAppException(final String reason, final int statusCode, final Throwable t) {

		super(statusCode, RestWebAppException.getParams(reason, statusCode), reason, t);
		this.reason = reason;
		this.statusCode = statusCode;

	}

	public static HashMap<String, String> getParams(final String reason, final int statusCode)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("statusCode", String.valueOf(statusCode));
		map.put("reason", reason);
		return map;
	}

	public String getReason() {
		return reason;
	}

	public int getStatusCode() {
		return statusCode;
	}

	//Getters and setters
}
