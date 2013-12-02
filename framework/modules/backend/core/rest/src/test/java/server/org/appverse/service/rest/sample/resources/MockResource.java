package server.org.appverse.service.rest.sample.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.org.appverse.service.rest.sample.SampleBean;
import server.org.appverse.service.rest.sample.SampleResource;
import server.org.appverse.service.rest.sample.Util;

@Path("samples")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MockResource implements SampleResource {

	private static Logger logger = LoggerFactory.getLogger(MockResource.class);

	@Context
	private UriInfo uriInfo;

	@Override
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SampleBean create(final SampleBean s, @Context final HttpServletResponse response)
	{
		logger.info(String.valueOf(s.id));
		logger.info(String.valueOf(s.foreignKey));
		logger.info(s.name);
		logger.info(s.surname);
		SampleBean sb = new SampleBean(s.id, s.foreignKey, s.name, s.surname);

		UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		uriBuilder = uriBuilder.path("{sampleId}");
		String uri = uriBuilder.build(sb.id).toString();
		logger.info("Sample bean added");
		logger.info("Header location:: " + uri);
		response.addHeader("Location", uri);

		return sb;

	}

	@Override
	@POST
	@Path("response")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createStatusResponse(final SampleBean s) {
		logger.info(String.valueOf(s.id));
		logger.info(String.valueOf(s.foreignKey));
		logger.info(s.name);
		logger.info(s.surname);
		SampleBean sb = new SampleBean(s.id, s.foreignKey, s.name, s.surname);

		UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		uriBuilder = uriBuilder.path("{sampleId}");
		String uri = uriBuilder.build(sb.id).toString();
		logger.info("Sample bean added");
		logger.info("Header location:: " + uri);

		return Response.status(Status.CREATED.getStatusCode()).header("Location", uri)
				.entity("sampleId::" + s.id + " has been created").build();

	}

	@Override
	public SampleBean update(final SampleBean s) {
		logger.info("resource updateds");
		logger.info(String.valueOf(s.id));
		logger.info(String.valueOf(s.foreignKey));
		logger.info(s.name);
		logger.info(s.surname);
		return s;
	}

	@Override
	public Response updateResponseStatus(final SampleBean s) {
		logger.info(String.valueOf(s.id));
		logger.info(String.valueOf(s.foreignKey));
		logger.info(s.name);
		logger.info(s.surname);

		logger.info("Sample bean updated");

		return Response.status(Status.OK.getStatusCode())
				.entity("sampleId::" + s.id + " has been updated").build();
	}

	@Override
	public server.org.appverse.service.rest.sample.xml.Page retrieveXMLPagedByFilter(Long numPage,
			Long pageSize, final String columnName, final String value)
	{
		server.org.appverse.service.rest.sample.xml.Page pageResult = new server.org.appverse.service.rest.sample.xml.Page();

		List<SampleBean> data = new ArrayList<SampleBean>();

		if (numPage == null)
			numPage = new Long(0);

		if (pageSize == null)
			pageSize = new Long(30);

		if (columnName != null && columnName.equals("name"))
		{
			SampleBean one = null;
			for (long i = numPage * pageSize; i < (numPage * pageSize) + pageSize; i++)
			{
				int tmp = Long.valueOf(i).intValue();
				one = new SampleBean(tmp, tmp, value + tmp, "test" + tmp);
				data.add(one);
			}

		}
		else if (columnName != null && columnName.equals("surname"))
		{
			SampleBean one = null;
			for (long i = numPage * pageSize; i < (numPage * pageSize) + pageSize; i++)
			{
				int tmp = Long.valueOf(i).intValue();
				one = new SampleBean(tmp, tmp, "test" + tmp, value + tmp);
				data.add(one);
			}
		}

		pageResult.setData(data);
		pageResult.setCurrentOffset(numPage);
		pageResult.setTotal(10000);

		return pageResult;
	}

	@Override
	public server.org.appverse.service.rest.sample.json.Page retrieveJSONPagedByFilter(
			Long numPage,
			Long pageSize, final String columnName, final String value)
	{
		server.org.appverse.service.rest.sample.json.Page pageResult = new server.org.appverse.service.rest.sample.json.Page();

		List<SampleBean> data = new ArrayList<SampleBean>();

		if (numPage == null)
			numPage = new Long(0);

		if (pageSize == null)
			pageSize = new Long(30);

		if (columnName != null && columnName.equals("name"))
		{
			SampleBean one = null;
			for (long i = numPage * pageSize; i < (numPage * pageSize) + pageSize; i++)
			{
				int tmp = Long.valueOf(i).intValue();
				one = new SampleBean(tmp, tmp, value + tmp, "test" + tmp);
				data.add(one);
			}

		}
		else if (columnName != null && columnName.equals("surname"))
		{
			SampleBean one = null;
			for (long i = numPage * pageSize; i < (numPage * pageSize) + pageSize; i++)
			{
				int tmp = Long.valueOf(i).intValue();
				one = new SampleBean(tmp, tmp, "test" + tmp, value + tmp);
				data.add(one);
			}
		}

		pageResult.setData(data);
		pageResult.setCurrentOffset(numPage);
		pageResult.setTotal(10000);

		return pageResult;
	}

	@Override
	public SampleBean retrieveSample(final Long sampleId) throws Exception {
		return new SampleBean(sampleId.intValue(), 34, "test", "test1");
	}

	@Override
	public List<SampleBean> retrieveSamples(final Long fkId) throws Exception {
		List<SampleBean> data = new ArrayList<SampleBean>();

		SampleBean one = new SampleBean(1, fkId.intValue(), "test1", "test1");
		SampleBean two = new SampleBean(2, fkId.intValue(), "test2", "test1");
		data.add(one);
		data.add(two);
		return data;
	}

	@Override
	public List<SampleBean> retrieveSomeSamples(final String ids) throws Exception {

		Integer[] keys = Util.parseAsLongArray(ids);

		List<SampleBean> data = new ArrayList<SampleBean>();

		SampleBean one = null;
		for (int i = 0; i < keys.length; i++)
		{
			one = new SampleBean(keys[i], 4, "test1", "test1");
			data.add(one);
		}

		return data;
	}

	@Override
	public List<SampleBean> retrieveSamples() throws Exception {

		List<SampleBean> data = new ArrayList<SampleBean>();

		SampleBean one = null;
		for (int i = 0; i < 10; i++)
		{
			one = new SampleBean(i, 4, "test1", "test1");
			data.add(one);
		}

		return data;
	}

	@Override
	public List<SampleBean> retrieveByFilter(final String columnName, final String value)
			throws Exception {

		List<SampleBean> data = new ArrayList<SampleBean>();

		if (columnName != null && columnName.equals("name"))
		{
			SampleBean one = new SampleBean(1, 3, value, "testaaaa");
			SampleBean two = new SampleBean(2, 3, value, "testbbb");
			SampleBean three = new SampleBean(3, 4, value, "testcc");
			data.add(one);
			data.add(two);
			data.add(three);
		}
		else if (columnName != null && columnName.equals("surname"))
		{
			SampleBean one = new SampleBean(1, 3, "name1", value);
			SampleBean two = new SampleBean(2, 3, "name2", value);
			data.add(one);
			data.add(two);

		}
		return data;
	}

	@Override
	public SampleBean retrieveOneByFilter(final String columnName, final String value)
			throws Exception {
		SampleBean one = null;
		if (columnName != null && columnName.equals("name"))
		{

			one = new SampleBean(1, 3, value, "testaaaa");
		}
		else if (columnName != null && columnName.equals("surname"))
		{
			one = new SampleBean(1, 3, "name1", value);

		}
		return one;
	}

	@Override
	public SampleBean deleteContact(final Long sampleId) {
		SampleBean s = new SampleBean();
		s.id = sampleId.intValue();
		logger.info("resource removed");
		return s;
	}

	@Override
	public Response deleteContactStatusResponse(final Long sampleId) {
		logger.info("resource removed with status return");
		return Response.status(200)
				.entity("sampleId::" + sampleId.toString() + " has been removed").build();
	}

	@Override
	public SampleBean deleteContactException(final Long sampleId) {
		//throw exception always for testing purpose
		if (sampleId != null)
			throw new WebApplicationException("access not allowed",
					Status.FORBIDDEN.getStatusCode());
		else
			throw new WebApplicationException(Response.Status.NOT_FOUND);

	}

}
