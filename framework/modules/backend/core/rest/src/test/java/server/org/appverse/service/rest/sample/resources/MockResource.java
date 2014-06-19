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
package server.org.appverse.service.rest.sample.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.org.appverse.service.rest.sample.SampleBean;
import server.org.appverse.service.rest.sample.SampleResource;
import server.org.appverse.service.rest.sample.Util;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Path("samples")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class MockResource implements SampleResource {

	private static Logger logger = LoggerFactory.getLogger(MockResource.class);

	@Context
	private UriInfo uriInfo;

	@Override
	public Response postFile(final InputStream is, final Request request) {

		byte[] data = null;
		try
		{
			data = inputStreamToByteArray(is);

		} catch (IOException ioe)
		{
			logger.error("Error retrieving file", ioe);
			throw new WebApplicationException("Error",
					Status.INTERNAL_SERVER_ERROR.getStatusCode());

		}

		logger.info("file posted:::::::::::::");
		logger.info(new String(data));

		UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		String uri = uriBuilder.build().toString();
		logger.info("file added");
		logger.info("Header location:: " + uri);

		return Response.status(Status.CREATED.getStatusCode()).header("Location", uri)
				.entity("file:: has been created").build();

	}

	@Override
	public Response getFile(final Long fileId, final Request request) {
		InputStream in = this
				.getClass().getResourceAsStream(
						"MockResource.class");
		StringBuffer hash = new StringBuffer();
		byte[] data = null;
		try
		{
			data = inputStreamToByteArray(in);
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data);

			byte[] digest = md.digest();

			for (byte b : digest) {
				hash.append(String.format("%02x", b & 0xff));
			}

			System.out.println("original:" + data);
			System.out.println("digested(hex):" + hash.toString());

		} catch (Exception nsae)
		{
			logger.error("Error creating digest", nsae);
			throw new WebApplicationException("Error",
					Status.INTERNAL_SERVER_ERROR.getStatusCode());

		}

		EntityTag etag = new EntityTag(hash.toString());

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null) {
			//means the preconditions have been met and the cache is valid
			return builder.build();
		}

		builder = Response.ok(data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		builder.tag(etag);

		return builder.build();
	}

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
	public Response retrieveJSONPagedByFilter(
			Long numPage,
			Long pageSize, final String columnName, final String value, final Request request)

	{
		CacheControl cc = new CacheControl();
		cc.setMaxAge(1);

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

		String hash = Integer.toString(pageResult.hashCode());
		logger.info("page hash:: " + hash);
		EntityTag etag = new EntityTag(hash);

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null) {
			//means the preconditions have been met and the cache is valid
			//we just need to reset the cachecontrol max age (optional)
			builder.cacheControl(cc);
			return builder.build();
		}

		builder = Response.ok(pageResult);
		//reset cache control and eTag (mandatory)
		//both cache control and eTag and compatible
		builder.cacheControl(cc);
		builder.tag(etag);
		return builder.build();
	}

	@Override
	public Response retrieveSample(final Long sampleId) throws Exception {

		SampleBean sb = new SampleBean(sampleId.intValue(), 34, "test", "test1");

		CacheControl cc = new CacheControl();
		cc.setMaxAge(30000);

		ResponseBuilder builder = Response.ok(sb);
		builder.cacheControl(cc);
		return builder.build();
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
	public Response retrieveSomeSamples(final String ids) throws Exception {

		Integer[] keys = Util.parseAsLongArray(ids);

		List<SampleBean> data = new ArrayList<SampleBean>();

		SampleBean one = null;
		for (int i = 0; i < keys.length; i++)
		{
			one = new SampleBean(keys[i], 4, "test1", "test1");
			data.add(one);
		}

		CacheControl cc = new CacheControl();
		cc.setMaxAge(30000);

		GenericEntity<List<SampleBean>> entity =
				new GenericEntity<List<SampleBean>>(data) {
				};

		ResponseBuilder builder = Response.ok(entity);
		builder.cacheControl(cc);
		return builder.build();
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
	public Response retrieveByFilter(final String columnName, final String value,
			final Request request)
			throws Exception {

		//Create cache control header
		CacheControl cc = new CacheControl();
		//Set max age to one day
		cc.setMaxAge(86400);

		List<SampleBean> data = new ArrayList<SampleBean>();

		if (columnName != null && columnName.equals("name"))
		{
			SampleBean one = new SampleBean(1, 3, value, "testaaaa");
			SampleBean two = new SampleBean(2, 3, value, "testbbbb");
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
		String hash = Integer.toString(data.hashCode());
		logger.info("list hash:: " + hash);
		EntityTag etag = new EntityTag(hash);

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null) {
			//means the preconditions have been met and the cache is valid
			//we just need to reset the cachecontrol max age (optional)
			//builder.cacheControl(cc);
			return builder.build();
		}

		//preconditions are not met and the cache is invalid
		//need to send new value with reponse code 200 (OK)
		GenericEntity<List<SampleBean>> entity =
				new GenericEntity<List<SampleBean>>(data) {
				};

		builder = Response.ok(entity);
		//reset cache control and eTag (mandatory)
		//both cache control and eTag and compatible
		//builder.cacheControl(cc);
		builder.tag(etag);
		return builder.build();
	}

	@Override
	public Response retrieveOneByFilter(final String columnName, final String value,
			final Request request)
			throws Exception {

		//Create cache control header
		CacheControl cc = new CacheControl();
		//Set max age to one day
		cc.setMaxAge(1);

		SampleBean one = null;
		if (columnName != null && columnName.equals("name"))
		{

			one = new SampleBean(1, 3, value, "testaaaa");
		}
		else if (columnName != null && columnName.equals("surname"))
		{
			one = new SampleBean(1, 3, "name1", value);

		}

		String hash = Integer.toString(one.hashCode());
		logger.info("SampleBean hash:: " + hash);
		EntityTag etag = new EntityTag(hash);

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null) {
			//means the preconditions have been met and the cache is valid
			//we just need to reset the cachecontrol max age (optional)
			builder.cacheControl(cc);
			return builder.build();
		}

		builder = Response.ok(one);
		//reset cache control and eTag (mandatory)
		//both cache control and eTag and compatible
		builder.cacheControl(cc);
		builder.tag(etag);
		return builder.build();
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

	private byte[] inputStreamToByteArray(final InputStream is) throws IOException
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();

		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}

		byte[] data = baos.toByteArray();
		return data;
	}

}
