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
package org.appverse.web.framework.backend.rest.managers;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.lang.reflect.Method;

public interface RestCachingManager {

	/**
	 * @param builder
	 * @param method
	 * @param type
	 * @param restService
	 * @param key
	 * @return
	 * @throws Exception
	 */
	<T> T manageRestCaching(final Builder builder, final Method method, final GenericType<T> type,
			IRestPersistenceService<?> restService,
			final String key)
			throws Exception;

	/**
	 * 
	 * Paged queryies implementation is different. It requires to manage the method that is making the call to obtain 
	 * reponse and unmarshalling response to DTO.
	 * This is required since Response object cannot be cached (InputStream will be closed).
	 * @param builder
	 * @param webTarget
	 * @param method
	 * @param restService
	 * @param key
	 * @return
	 * @throws Exception
	 */
	<T extends AbstractIntegrationBean> IntegrationPaginatedResult<T> manageRestCaching(
			final Builder builder,
			final WebTarget webTarget,
			final Method method,
			final IRestPersistenceService<T> restService, final String key) throws Exception;

}
