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
package org.appverse.web.framework.backend.rest.filters.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.rest.helpers.cache.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * This class is Jersey client provider, responsible of analyzing response headers 
 * to decide if returned data should be cached 
 *
 */
@Provider
public class RestRequestCachingFilter implements ClientResponseFilter {

	private final Cache cache;

	@AutowiredLogger
	private static Logger logger = LoggerFactory.getLogger(RestRequestCachingFilter.class);

	public RestRequestCachingFilter(final Cache cache)
	{
		this.cache = cache;
	}

	@Override
	public void filter(final ClientRequestContext req,
			final ClientResponseContext responseContext)
			throws IOException {

		if (req.getMethod().equals(HttpMethod.GET)) {

			String cacheControl = responseContext.getHeaderString(HttpHeaders.CACHE_CONTROL);

			EntityTag etag = responseContext.getEntityTag();

			//If some caching headers exist in server response
			if ((cacheControl != null && !cacheControl.isEmpty())
					|| (etag != null && etag.getValue() != null))
			{
				CacheEntry entry = new CacheEntry();
				boolean cacheControlEnable = false;
				boolean etagEnable = false;
				if ((cacheControl != null && !cacheControl.isEmpty()))
				{

					CacheControl control = CacheControl.valueOf(cacheControl);
					if (logger.isDebugEnabled())
						logger.debug("Cache-Control header::" + cacheControl);
					if (!control.isNoCache() && control.getMaxAge() != -1)
					{
						if (logger.isDebugEnabled())
							logger.debug("Max-age::" + control.getMaxAge());
						cacheControlEnable = true;
						entry.setMaxAge(new Long(control.getMaxAge()));
					}
				}
				if (etag != null && etag.getValue() != null)
				{
					etagEnable = true;
					if (logger.isDebugEnabled())
						logger.debug("etag header::" + etag.getValue());
					entry.setEtag(etag);

				}

				String path = req.getUri().getPath();
				String query = req.getUri().getQuery();
				if (query != null && !query.isEmpty())
					path += query;

				//Data is new, modified or expired
				if ((cacheControlEnable
						|| etagEnable) && responseContext.getStatus() == Status.OK.getStatusCode())
				{
					if (logger.isDebugEnabled())
						logger.debug("Insert in cache with key::"
								+ path
								+ ". current object value is null, since will be inserted in RestCachingManagerImpl");
					entry.setStatus(Status.OK.getStatusCode());
					Element el = new Element(path, entry);
					cache.put(el);

				}
				//If status is 304 (not modified) data is not returned by server				
				else if (etagEnable
						&& responseContext.getStatus() == Status.NOT_MODIFIED.getStatusCode())
				{
					//update existing reference with cacheControl info and status
					Element wrap = cache.get(path);
					CacheEntry existingEntry = (CacheEntry) wrap.getObjectValue();
					existingEntry.setStatus(responseContext.getStatus());
					existingEntry.setExpiration(entry.getExpiration());
				}

			}
		}
	}
}
