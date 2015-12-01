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
package org.appverse.web.framework.backend.api.helpers.security;

import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

/**
 * A Request Filter to filter out possible XSS attacks in the request query
 * parameters or headers.
 */
@PreMatching
public class XSSSecurityFilter implements ContainerRequestFilter {
	/**
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext request) {
		// Clean the query string
		// The query string parameters come in an inmutable data structure, so they cannot be modified.
		// It will be necessary to copy the query string parameters to a brand new hash, clean them and later 
		// rebuild the request URI with the cleaned parameters.    
		final MultivaluedMap<String, String> parameters = request.getUriInfo().getQueryParameters();
		if (parameters != null && !parameters.isEmpty()){
			MultivaluedHashMap<String, String> parametersToClean = new MultivaluedHashMap<String, String>();
			parametersToClean.putAll(parameters);
			ESAPIHelper.cleanParams(parametersToClean);
			UriBuilder query = request.getUriInfo().getRequestUriBuilder().replaceQuery("");
			for (Map.Entry<String, List<String>> e : parametersToClean.entrySet()) {
				final String key = e.getKey();
				for (String v : e.getValue()) {
					query = query.queryParam(key, v);
				}
			}
			request.setRequestUri(query.build());
		}

		// Clean the headers
		MultivaluedMap<String, String> headers = request.getHeaders();
		if (headers != null && !headers.isEmpty()){
			ESAPIHelper.cleanParams(headers);
		}
	}
}