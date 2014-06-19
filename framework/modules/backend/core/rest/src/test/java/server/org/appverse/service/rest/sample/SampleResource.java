package server.org.appverse.service.rest.sample;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

@Path("samples")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface SampleResource {

	@GET
	@Path("file/{fileId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getFile(@PathParam("fileId") final Long fileId, @Context Request request);

	@POST
	@Path("file")
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response postFile(InputStream is,
			@Context Request request);

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
	@Path("paged/xml/{page}/{pageSize}")
	public server.org.appverse.service.rest.sample.xml.Page retrieveXMLPagedByFilter(
			@PathParam("page") final Long page,
			@PathParam("pageSize") final Long pageSize,
			@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value);

	@GET
	@Path("paged/json/{page}/{pageSize}")
	public Response retrieveJSONPagedByFilter(
			@PathParam("page") final Long page,
			@PathParam("pageSize") final Long pageSize,
			@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value, @Context Request request);

	@GET
	@Path("{sampleId}")
	public Response retrieveSample(@PathParam("sampleId") Long sampleId) throws Exception;

	@GET
	@Path("type/{fkId}")
	public List<SampleBean> retrieveSamples(@PathParam("fkId") Long fkId) throws Exception;

	@GET
	@Path("/multi/{sampleId_array:[\\d,(%2C)]+}")
	public Response retrieveSomeSamples(@PathParam("sampleId_array") final String ids)
			throws Exception;

	@GET
	public List<SampleBean> retrieveSamples() throws Exception;

	@GET
	@Path("filter")
	public Response retrieveByFilter(@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value, @Context Request request)
			throws Exception;

	@GET
	@Path("filterOne")
	public Response retrieveOneByFilter(@QueryParam("columnName") final String columnName,
			@QueryParam("value") final String value, @Context Request request)
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
