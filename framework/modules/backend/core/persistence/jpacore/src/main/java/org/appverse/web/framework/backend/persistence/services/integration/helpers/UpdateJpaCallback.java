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
package org.appverse.web.framework.backend.persistence.services.integration.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

public class UpdateJpaCallback<T> {

	private Query query;
	private String queryString;
	private List<QueryJpaCallbackParameter> namedParameters;
	private Object[] indexedParameters;
	private List<QueryJpaCallbackHint> hints;

	public UpdateJpaCallback(String queryString) {
		super();
		if (queryString==null || queryString.isEmpty()){
			throw new PersistenceException("query cannot be null or empty");
		}
		this.queryString = queryString;
	}
	
	public UpdateJpaCallback(Query query) {
		super();
		if(query==null){
			throw new PersistenceException("query cannot be null");
		}
		this.query = query;
	}	

	public Object doInJpa(EntityManager em) throws PersistenceException {

		// create query if it was set as a queryString
		if(query==null){
			query = em.createQuery(queryString);
		}		

		// set parameters
		if (namedParameters != null) {
			for (QueryJpaCallbackParameter namedParameter : namedParameters) {
				query.setParameter(namedParameter.getName(),
						namedParameter.getValue());
			}
		} else if (indexedParameters != null) {
			for (int i = 0; i < indexedParameters.length; i++) {
				query.setParameter(i + 1, indexedParameters[i]);
			}
		}

		// set query hints
		if (hints != null) {
			for (QueryJpaCallbackHint hint : hints) {
				query.setHint(hint.getHint(), hint.getValue());
			}
		}

		return query.executeUpdate();
	}

	public void setHints(List<QueryJpaCallbackHint> hints) {
		this.hints = hints;
	}

	public void setIndexedParameters(Object[] indexedParameters) {
		this.indexedParameters = indexedParameters;
	}

	public void setNamedParameters(List<QueryJpaCallbackParameter> parameters) {
		this.namedParameters = parameters;
	}

	public void setNamedParameters(Map<String, Object> parameters) {
		List<QueryJpaCallbackParameter> queryJpaCallbackParameters = new ArrayList<QueryJpaCallbackParameter>();
		for (String key : parameters.keySet()) {
			QueryJpaCallbackParameter queryJpaCallbackParameter = new QueryJpaCallbackParameter(
					key, parameters.get(key));
			queryJpaCallbackParameters.add(queryJpaCallbackParameter);
		}
		this.namedParameters = queryJpaCallbackParameters;
	}
}
