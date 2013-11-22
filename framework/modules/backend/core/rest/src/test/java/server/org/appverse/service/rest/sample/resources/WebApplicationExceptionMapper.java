package server.org.appverse.service.rest.sample.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

	@Context
	private HttpHeaders headers;

	@Override
	public Response toResponse(final WebApplicationException e) {

		return Response.status(e.getResponse().getStatus())
				.entity(e.getMessage()).type(headers.getMediaType()).build();

	}
}
