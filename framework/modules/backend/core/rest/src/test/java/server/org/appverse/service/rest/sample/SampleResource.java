package server.org.appverse.service.rest.sample;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("samples")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface SampleResource {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SampleBean create(final SampleBean s, @Context HttpServletResponse response);

	@POST
	@Path("response")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createStatusResponse(final SampleBean s);

	@PUT
	@Path("{sampleId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SampleBean update(final SampleBean s);

	@PUT
	@Path("response/{sampleId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateResponseStatus(final SampleBean s);

	@GET
	@Path("{sampleId}")
	public SampleBean retrieveSample(@PathParam("sampleId") Long sampleId) throws Exception;

	@GET
	@Path("type/{fkId}")
	public List<SampleBean> retrieveSamples(@PathParam("fkId") Long fkId) throws Exception;

	@GET
	@Path("/multi/{sampleId_array:[\\d,(%2C)]+}")
	public List<SampleBean> retrieveSomeSamples(@PathParam("sampleId_array") final String ids)
			throws Exception;

	@GET
	public List<SampleBean> retrieveSamples() throws Exception;

	@GET
	@Path("filter")
	public List<SampleBean> retrieveByFilter(@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value)
			throws Exception;

	@GET
	@Path("filterOne")
	public SampleBean retrieveOneByFilter(@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value)
			throws Exception;

	@DELETE
	@Path("{sampleId}")
	public SampleBean deleteContact(@PathParam("sampleId") final Long sampleId);

	@DELETE
	@Path("/response/{sampleId}")
	public Response deleteContactStatusResponse(@PathParam("sampleId") final Long sampleId);

	@DELETE
	@Path("/exception/{sampleId}")
	SampleBean deleteContactException(@PathParam("sampleId") final Long sampleId);
}
