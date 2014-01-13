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
package org.appverse.web.framework.backend.rest.managers.impl.live;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.RedirectionException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response.Status;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.rest.annotations.RestCaching;
import org.appverse.web.framework.backend.rest.helpers.cache.CacheEntry;
import org.appverse.web.framework.backend.rest.managers.RestCachingManager;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * This manager encapsulates the complexity of caching logic.
 * It supports every retrieve method present in RestPersistenceService
 *
 */
public class RestCachingManagerImpl implements RestCachingManager {

	@Autowired
	public CacheManager cacheManager;

	@AutowiredLogger
	private static Logger logger;

	private Object manageRestCaching(final Builder builder, final WebTarget webTarget,
			final Method method,
			final GenericType<?> type,
			final IRestPersistenceService<?> restService, final String key)
			throws Exception
	{
		//Check if caching logic is enabled in repository 
		//(RestCaching annotations could be present in interface OR implementations)

		RestCaching rc = AnnotationUtils.findAnnotation(restService.getClass(), RestCaching.class);
		if (rc != null && key != null)
		{
			String cacheName = rc.cacheName();
			Cache cache = cacheManager.getCache(cacheName);

			if (logger.isDebugEnabled())
				logger.debug("Checking path::" + key);

			Element wrap = cache.get(key);

			//Requested data should be in cache
			if (wrap != null) {
				if (logger.isDebugEnabled())
					logger.debug("Entry in cache is not null...checking expiration date..");
				CacheEntry entry = (CacheEntry) wrap.getObjectValue();
				EntityTag etag = entry.getEtag();
				//Data is cached, so we can return it and avoid to call rest service
				if (!entry.hasExpired() && entry.getObject() != null)
				{
					if (logger.isDebugEnabled())
						logger.debug("Entry has not expired, so returned from cache");

					return entry.getObject();
				}
				//Data is cached, but expired, so remove it
				else if (entry.hasExpired())
				{
					if (logger.isDebugEnabled())
						logger.debug("Entry has expired, do not use it");
				}
				//If we have EntityTag header, send to server to check conditional existence
				if (etag != null && etag.getValue() != null)
				{
					builder.header("If-None-Match", "\"" + etag.getValue() + "\"");
				}

			}

			if (logger.isDebugEnabled())
				logger.debug("Actual call to service");
			//Invoke rest service

			Object data = null;
			boolean notModified = false;
			try
			{
				//Paging methods
				if (webTarget != null)
					data = method.invoke(restService, webTarget, builder);
				//Common retrieve and retrieveList methods
				else
				{

					if (type != null)
						data = method.invoke(builder, type);
					else
						data = method.invoke(builder);
				}
			} catch (InvocationTargetException ite)
			{
				//This could be a NOT_MODIFIED response, produced by server ETAG caching
				//We must check it, and rethrow exception if it's another error
				Throwable target = ite.getTargetException();
				if (target != null && target instanceof RedirectionException)
				{
					RedirectionException re = (RedirectionException) target;
					if (re.getResponse().getStatus() == Status.NOT_MODIFIED.getStatusCode())
					{
						if (logger.isDebugEnabled())
							logger.debug("NOT_MODIFIED response");

						notModified = true;
					}
				}
				if (!notModified)
					throw ite;
			}
			//Jersey RestRequestCachingFilter intercept response from server and analyze headers

			if (logger.isDebugEnabled())
				logger.debug("Checking if we should insert or retrieve data in/from cache");
			wrap = cache.get(key);
			if (wrap != null)
			{
				CacheEntry entry = (CacheEntry) wrap.getObjectValue();
				//This only happens if response includes cache control headers. 
				//Jersey RestRequestCachingFilter is in charge of preparing this entry.
				if (entry.getObject() == null)
				{
					if (logger.isDebugEnabled())
						logger.debug("Insert data in cache");
					entry.setObject(data);
				}
				//In this case, server is not returning data, since object is cached and not has been modified
				else if ((notModified || entry.getStatus() == Status.NOT_MODIFIED.getStatusCode())
						&& entry.getObject() != null)
				{
					if (logger.isDebugEnabled())
						logger.debug("Retrieve data from cache");

					return entry.getObject();
				}

			}
			return data;

		}

		//Caching is not enabled

		//Paging methods
		if (webTarget != null)
			return method.invoke(restService, webTarget, builder);
		//Common retrieve and retrieveList methods
		else
		{
			if (type != null)
				return method.invoke(builder, type);
			else
				return method.invoke(builder);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T manageRestCaching(final Builder builder, final Method method,
			final GenericType<T> type,
			final IRestPersistenceService<?> restService, final String key)
			throws Exception
	{

		T data = (T) manageRestCaching(builder, null, method, type, restService, key);
		return data;
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.managers.RestCachingManager#manageRestCaching(javax.ws.rs.client.Invocation.Builder, java.lang.reflect.Method, javax.ws.rs.core.Response, org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractIntegrationBean> IntegrationPaginatedResult<T> manageRestCaching(
			final Builder builder,
			final WebTarget webTarget,
			final Method method,
			final IRestPersistenceService<T> restService, final String key) throws Exception {

		IntegrationPaginatedResult<T> data = (IntegrationPaginatedResult<T>) manageRestCaching(
				builder, webTarget, method, null, restService, key);
		return data;

	}

}
