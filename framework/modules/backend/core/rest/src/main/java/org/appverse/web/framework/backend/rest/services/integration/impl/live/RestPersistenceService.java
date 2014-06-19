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

package org.appverse.web.framework.backend.rest.services.integration.impl.live;

import org.apache.commons.lang.StringUtils;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.api.services.integration.ServiceUnavailableException;
import org.appverse.web.framework.backend.rest.exceptions.RestWebAppException;
import org.appverse.web.framework.backend.rest.managers.RestCachingManager;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;
import org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides integration with Rest services 
 * 
 */
public abstract class RestPersistenceService<T extends AbstractIntegrationBean> extends
		AbstractIntegrationService<T>
		implements IRestPersistenceService<T> {

	public static final String MAX_RECORDS_PARAM_NAME = "maxRecords";
	public static final String OFFSET_PARAM_NAME = "offset";

	@Autowired(required = false)
	RestCachingManager restCachingManager;

	@AutowiredLogger
	private static Logger logger;

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieve(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long)
	 */
	@Override
	public T retrieve(final WebTarget webClient, final String idName, final Long id)
			throws Exception {
		return retrieve(webClient, idName, id, null, null);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieve(javax.ws.rs.client.WebTarget, java.util.Map, java.util.Map)
	 */
	@Override
	public T retrieve(final WebTarget webClient, final Map<String, Object> pathParams,
			final Map<String, Object> queryParams)
			throws Exception {
		return this.retrieve(webClient, null, null, pathParams, queryParams);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieve(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public T retrieve(WebTarget webClient, final String idName, final Long id,
                      final Map<String, Object> pathParams,
                      final Map<String, Object> queryParams)
            throws Exception {
        return retrieve(webClient, idName, id, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieve(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map)
     */
    @Override
	public T retrieve(WebTarget webClient, final String idName, final Long id,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		GenericType<T> genericType = new GenericType<T>(getClassP()) {
		};

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }

		Method methodGet = Builder.class.getMethod("get", GenericType.class);

		T object = null;
		if (restCachingManager != null)
			object = restCachingManager.manageRestCaching(builder, methodGet, genericType, this,
					getKey(webClient));
		else
			object = builder.get(genericType);

		return object;
	}

    // Binary method
	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveInputStream(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
	 */
    @Override
    public InputStream retrieveInputStream(WebTarget webClient, final String idName, final Long id,
                                           final Map<String, Object> pathParams, final Map<String, Object> queryParams)
            throws Exception {
        return retrieveInputStream(webClient, idName, id, pathParams, queryParams, null);
    }

	// Binary method
	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveInputStream(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map, java.util.Map)
	 */
	@Override
	public InputStream retrieveInputStream(WebTarget webClient, final String idName, final Long id,
			final Map<String, Object> pathParams, final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {
		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = webClient.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }

		Method methodGet = Builder.class.getMethod("get");

		Response object = null;
		if (restCachingManager != null)
			object = restCachingManager.manageRestCaching(builder, methodGet, null, this,
					getKey(webClient));
		else
			object = builder.get();

		return (InputStream) object.getEntity();
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long)
	 */
	@Override
	public List<T> retrieveList(final WebTarget webClient, final String idName, final Long id)
			throws Exception {
		return retrieveList(webClient, idName, id, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public List<T> retrieveList(WebTarget webClient, final String idName, final Long id,
                                final Map<String, Object> pathParams, final Map<String, Object> queryParams)
            throws Exception {
        return retrieveList(webClient, idName, id, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public List<T> retrieveList(WebTarget webClient, final String idName, final Long id,
			final Map<String, Object> pathParams, final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		GenericType<List<T>> genericType = new GenericType<List<T>>(getTypeListP()) {
		};

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }

		Method methodGet = Builder.class.getMethod("get", GenericType.class);

		List<T> objects = null;
		if (restCachingManager != null)
			objects = restCachingManager.manageRestCaching(builder, methodGet, genericType,
					this,
					getKey(webClient));
		else
			objects = builder.get(genericType);

		return objects;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gft.riaframework.backend.services.integration.IRestPersistenceService
	 * #retrieveList()
	 */
	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget)
	 */
	@Override
	public List<T> retrieveList(final WebTarget webClient) throws Exception {
		return retrieveList(webClient, (Map<String, Object>) null, (Map<String, Object>) null);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.util.Map, java.util.Map)
	 */
	@Override
	public List<T> retrieveList(final WebTarget webClient, final Map<String, Object> pathParams,
			final Map<String, Object> queryParams)
			throws Exception {
		return retrieveList(webClient, (String) null, (Long) null, pathParams, queryParams);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.lang.String, java.util.List)
	 */
	@Override
	public List<T> retrieveList(final WebTarget webClient, final String idsName,
			final List<Long> ids)
			throws Exception {
		return retrieveList(webClient, idsName, ids, null, null);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrieveList(javax.ws.rs.client.WebTarget, java.lang.String, java.util.List, java.util.Map, java.util.Map)
	 */
	@Override
	public List<T> retrieveList(WebTarget webClient, final String idsName,
			final List<Long> ids, final Map<String, Object> pathParams,
			final Map<String, Object> queryParams) throws Exception {

		String idsString = StringUtils.join(ids, ',');
		webClient = webClient.resolveTemplate(idsName, idsString);
		return retrieveList(webClient, pathParams, queryParams);

	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrievePagedQuery(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter)
	 */
	@Override
	public IntegrationPaginatedResult<T> retrievePagedQuery(final WebTarget webClient,
			final IntegrationPaginatedDataFilter filter)
			throws Exception {

		return retrievePagedQuery(webClient, filter, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrievePagedQuery(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter, java.util.Map, java.util.Map)
     */
    @Override
    public IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
                                                            final IntegrationPaginatedDataFilter filter, final Map<String, Object> pathParams,
                                                            final Map<String, Object> queryParams)
            throws Exception {
        return retrievePagedQuery(webClient, filter, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#retrievePagedQuery(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
			final IntegrationPaginatedDataFilter filter, final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		Builder builder = null;
		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		webClient = webClient.resolveTemplate(getOffsetParamName(), filter.getOffset());
		webClient = webClient.resolveTemplate(getMaxRecordsParamName(), filter.getLimit());

		builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }

        Method methodGet = this.getClass().getMethod("retrieveAndUnmarshall", WebTarget.class,
				Builder.class);

		IntegrationPaginatedResult<T> result = null;
		if (restCachingManager != null)
			result = restCachingManager.manageRestCaching(builder,
					webClient, methodGet, this,
					getKey(webClient));
		else
			result = retrieveAndUnmarshall(webClient, builder);

		return result;
	}

	/**
	 * Join in the same method retrieving and unmarshalling
	 * 
	 * @param webClient
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public IntegrationPaginatedResult<T> retrieveAndUnmarshall(final WebTarget webClient,
			final Builder builder) throws Exception
	{
		Response resp = builder.get();

		IntegrationPaginatedResult<T> result = new IntegrationPaginatedResult<T>(
				Collections.<T> emptyList(), 0, 0);

        if (Status.SERVICE_UNAVAILABLE.getStatusCode() == resp.getStatus()) {
            logger.error("Problem with call {" + webClient.getUri()
                    + "} . Response status: " + resp.getStatus());
            //Appverse integration exception
            throw new ServiceUnavailableException();
        }
        else if (Status.Family.SUCCESSFUL == resp.getStatusInfo().getFamily()) {
            result = mapPagedResult(resp);
        }
        else if (Status.NOT_MODIFIED.getStatusCode() != resp.getStatus()) {
            // Any other status will be considered an error (except NOT_MODIFIED)
            logger.error("Error with call {" + webClient.getUri()
                    + "} . Response status: " + resp.getStatus());
            throw new RestWebAppException("Exception calling URI: " +
                    webClient.getUri().toString() + ". Status code is: " + resp.getStatus(), resp.getStatus());
        }
		return result;
	}

	// DELETE METHODS

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#deleteStatusReturn(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long)
	 */
	@Override
	public StatusResult deleteStatusReturn(final WebTarget webClient, final String idName,
			final Long id)
			throws Exception
	{
		return deleteStatusReturn(webClient, idName, id, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#deleteStatusReturn(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public StatusResult deleteStatusReturn(WebTarget webClient, final String idName, final Long id,
                                           final Map<String, Object> pathParams,
                                           final Map<String, Object> queryParams) throws Exception
    {
        return deleteStatusReturn(webClient, idName, id, pathParams, queryParams, null);
    }


    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#deleteStatusReturn(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public StatusResult deleteStatusReturn(WebTarget webClient, final String idName, final Long id,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties) throws Exception
	{
		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

        Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		Response resp = builder.delete();
		return getStatusResult(resp);

	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#delete(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long)
	 */
	@Override
	public T delete(final WebTarget webClient, final String idName, final Long id) throws Exception
	{
		return delete(webClient, idName, id, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#delete(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public T delete(WebTarget webClient, final String idName, final Long id,
                    final Map<String, Object> pathParams,
                    final Map<String, Object> queryParams) throws Exception
    {
        return delete(webClient, idName, id, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#delete(javax.ws.rs.client.WebTarget, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public T delete(WebTarget webClient, final String idName, final Long id,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties) throws Exception
	{
		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		GenericType<T> genericType = new GenericType<T>(getClassP()) {
		};

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

        Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		return builder.delete(genericType);
	}

	//INSERT METHODS

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insert(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public T insert(final WebTarget webClient, final T object) throws Exception {
		return insert(webClient, object, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insert(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.util.Map, java.util.Map)
     */
    @Override
    public T insert(WebTarget webClient, final T object,
                    final Map<String, Object> pathParams,
                    final Map<String, Object> queryParams) throws Exception {
        return insert(webClient, object, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insert(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.util.Map, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public T insert(WebTarget webClient, final T object,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties) throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		GenericType<T> genericType = new GenericType<T>(getClassP()) {
		};

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		return builder.post(Entity.entity(object, acceptMediaType()), genericType);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insert(javax.ws.rs.client.WebTarget, java.io.InputStream, java.util.Map, java.util.Map)
     */
    @Override
    public Response insert(WebTarget webClient, final InputStream object,
                           final Map<String, Object> pathParams,
                           final Map<String, Object> queryParams) throws Exception {
        return insert(webClient, object, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insert(javax.ws.rs.client.WebTarget, java.io.InputStream, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public Response insert(WebTarget webClient, final InputStream object,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties) throws Exception {
		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		return builder.post(Entity.entity(object, MediaType.APPLICATION_OCTET_STREAM_TYPE));
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insertStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public StatusResult insertStatusReturn(final WebTarget webClient, final T object)
			throws Exception {
		return insertStatusReturn(webClient, object, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insertStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.util.Map, java.util.Map)
     */
    @Override
    public StatusResult insertStatusReturn(WebTarget webClient, final T object,
                                           final Map<String, Object> pathParams,
                                           final Map<String, Object> queryParams) throws Exception {
        return insertStatusReturn(webClient, object, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#insertStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public StatusResult insertStatusReturn(WebTarget webClient, final T object,
			final Map<String, Object> pathParams,
			final Map<String, Object> queryParams, final Map<String, Object> builderProperties) throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		Response resp = builder.post(Entity.entity(object, acceptMediaType()));
		return getStatusResult(resp);
	}

	// UPDATE Methods

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#update(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long)
	 */
	@Override
	public T update(final WebTarget webClient, final T object, final String idName, final Long id)
			throws Exception {
		// TODO Auto-generated method stub
		return update(webClient, object, idName, id, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#update(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public T update(WebTarget webClient, final T object, final String idName, final Long id,
                    final Map<String, Object> pathParams, final Map<String, Object> queryParams)
            throws Exception {
        return update(webClient, object, idName, id, pathParams, queryParams, null);
    }


    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#update(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public T update(WebTarget webClient, final T object, final String idName, final Long id,
			final Map<String, Object> pathParams, final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		GenericType<T> genericType = new GenericType<T>(getClassP()) {
		};

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		return builder.put(Entity.entity(object, acceptMediaType()), genericType);
	}

    @Override
    public Response update(WebTarget webClient, final InputStream object, final String idName,
                           final Long id,
                           final Map<String, Object> pathParams, final Map<String, Object> queryParams)
            throws Exception {
        return update(webClient, object, idName, id, pathParams, queryParams, null);
    }

	@Override
	public Response update(WebTarget webClient, final InputStream object, final String idName,
			final Long id,
			final Map<String, Object> pathParams, final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {
		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		return builder.put(Entity.entity(object, MediaType.APPLICATION_OCTET_STREAM_TYPE));
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#updateStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long)
	 */
	@Override
	public StatusResult updateStatusReturn(final WebTarget webClient, final T object,
			final String idName, final Long id)
			throws Exception {
		return updateStatusReturn(webClient, object, idName, id, null, null);
	}

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#updateStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long, java.util.Map, java.util.Map)
     */
    @Override
    public StatusResult updateStatusReturn(WebTarget webClient, final T object,
                                           final String idName, final Long id,
                                           final Map<String, Object> pathParams, final Map<String, Object> queryParams)
            throws Exception {
        return updateStatusReturn(webClient, object, idName, id, pathParams, queryParams, null);
    }

    /* (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#updateStatusReturn(javax.ws.rs.client.WebTarget, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, java.lang.String, java.lang.Long, java.util.Map, java.util.Map, java.util.Map)
     */
	@Override
	public StatusResult updateStatusReturn(WebTarget webClient, final T object,
			final String idName, final Long id,
			final Map<String, Object> pathParams, final Map<String, Object> queryParams, final Map<String, Object> builderProperties)
			throws Exception {

		if (queryParams != null)
			webClient = applyQuery(webClient, queryParams);

		if (id != null && idName != null)
			webClient = webClient.resolveTemplate(idName, id);

		if (pathParams != null)
			webClient = webClient.resolveTemplates(pathParams);

		Builder builder = acceptMediaType(webClient.request());
        if (builderProperties != null){
            addBuilderProperties(builder, builderProperties);
        }
		Response resp = builder.put(Entity.entity(object, acceptMediaType()));
		return getStatusResult(resp);

	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#applyQuery(javax.ws.rs.client.WebTarget, java.util.Map)
	 */
	@Override
	public WebTarget applyQuery(WebTarget webClient, final Map<String, Object> queryParams)
			throws Exception {

		Iterator<String> it = queryParams.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			webClient = webClient.queryParam(key, queryParams.get(key));
		}
		return webClient;
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#mapPagedResult(javax.ws.rs.core.Response)
	 */
	@Override
	public IntegrationPaginatedResult<T> mapPagedResult(final Response response) throws Exception {
		throw new UnsupportedOperationException("You must overwrite 'mapPagedResult' method");
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#getOffsetParamName()
	 */
	@Override
	public String getOffsetParamName() {
		return OFFSET_PARAM_NAME;
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#getMaxRecordsParamName()
	 */
	@Override
	public String getMaxRecordsParamName() {
		return MAX_RECORDS_PARAM_NAME;
	}

	/**
	 * Use reflection to get the actual generic type
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getClassP() throws Exception {
		Method method = this.getClass().getMethod("getTypeSafeList");

		Type returnType = method.getGenericReturnType();
		Class<T> classP = null;

		if (returnType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) returnType;
			Type[] typeArguments = type.getActualTypeArguments();
			for (Type typeArgument : typeArguments) {
				classP = (Class<T>) typeArgument;
				System.out.println("typeArgClass = " + classP);
			}
		}
		return classP;

	}

	/**
	 * Use reflection to get the actual generic list type
	 * 
	 * @return
	 * @throws Exception
	 */
	protected Type getTypeListP() throws Exception {
		return this.getClass().getMethod("getTypeSafeList").getGenericReturnType();
	}

	/**
	 * @param builder
	 * @return
	 */
	protected Invocation.Builder acceptMediaType(final Invocation.Builder builder)
	{
		return builder.accept(acceptMediaType());
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService#acceptMediaType()
	 */
	@Override
	public String acceptMediaType()
	{
		return MediaType.APPLICATION_JSON;
	}

    /**
     * @param builder
     * @param properties
     * @return void
     */
    protected void addBuilderProperties(Invocation.Builder builder, Map<String, Object> builderProperties){
        if (builderProperties != null){
            for (Map.Entry<String, Object> entry : builderProperties.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                builder.property(key, value);
            }
        }
    }

	/**
	 * Analyse response to get result code and message
	 * 
	 * @param resp
	 * @return
	 */
	protected StatusResult getStatusResult(final Response resp) throws Exception
	{
		StatusResult result = new StatusResult();
		result.setStatus(resp.getStatus());
		result.setLocation(resp.getLocation());
		try
		{
			String output = resp.readEntity(String.class);
			if (StringUtils.isNotBlank(output))
				result.setMessage(output);
			else
				result.setMessage(resp.getStatusInfo().getReasonPhrase());
		} catch (Exception e) {
		}
		return result;
	}

	protected String getKey(final WebTarget webClient)
	{
		StringBuilder sb = new StringBuilder(webClient.getUri().getPath());
		String query = webClient.getUri().getQuery();
		if (query != null && !query.isEmpty())
			sb.append(query);
		return sb.toString();
	}

}
