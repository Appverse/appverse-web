package org.appverse.web.framework.backend.frontfacade.json.controllers.exceptions;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class BadRequestException extends WebApplicationException {
	private static final long serialVersionUID = 1L;
	private String error;

	public BadRequestException(String error) {
		super(Response.status(Status.BAD_REQUEST)/*
												 * .type(MediaType.
												 * APPLICATION_XHTML_XML)
												 */
		.entity(error).build());
	}

	public String getError() {
		return error;
	}
}
