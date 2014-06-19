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
package org.test.app.web.framework.backend.rest.services.integration.impl.live;

import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;
import org.test.app.web.framework.backend.rest.model.integration.xml.PageDTO;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Repository("sampleRepositoryXml")
public class XMLSampleRepositoryImpl extends JSONSampleRepositoryImpl
{

	@Autowired
	private WebTarget sampleClient;

	@Override
	public String acceptMediaType()
	{
		return MediaType.APPLICATION_XML;
	}

	@Override
	public IntegrationPaginatedResult<SampleDTO> retrievePagedSamples(
			final IntegrationPaginatedDataFilter filter) throws Exception
	{
		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("columnName", filter.getColumns().get(0));
		queryParams.put("value", filter.getValues().get(0));

		return this.retrievePagedQuery(sampleClient.path("samples/paged/xml/{page}/{pageSize}"),
				filter, null,
				queryParams);

	}

	@Override
	public IntegrationPaginatedResult<SampleDTO> mapPagedResult(final Response response)
			throws Exception {

		GenericType<PageDTO<SampleDTO>> genericType = new GenericType<PageDTO<SampleDTO>>() {
		};

		PageDTO<SampleDTO> page = response.readEntity(genericType);
		IntegrationPaginatedResult<SampleDTO> result = new IntegrationPaginatedResult<SampleDTO>();

		result.setData(page.getDataList());
		result.setTotalLength(page.getTotalSize());
		result.setOffset(page.getInitialPos());

		return result;
	}

}
