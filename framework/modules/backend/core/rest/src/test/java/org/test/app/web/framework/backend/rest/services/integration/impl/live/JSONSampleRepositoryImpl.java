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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;
import org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService;
import org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;
import org.test.app.web.framework.backend.rest.model.integration.json.PageDTO;
import org.test.app.web.framework.backend.rest.services.integration.SampleRepository;

@Repository("sampleRepositoryJson")
public class JSONSampleRepositoryImpl extends RestPersistenceService<SampleDTO>
		implements SampleRepository, IRestPersistenceService<SampleDTO> {

	// resolves to <base>/restservice/samples
	//@ClientSample
	//@Uri("http://mail.acme.com/accounts/{name}")
	@Autowired
	private WebTarget sampleClient;

	@AutowiredLogger
	private static Logger logger;

	@Override
	public List<SampleDTO> getTypeSafeList()
	{
		return null;
	}

	@Override
	public SampleDTO retrieveSample(final Long sampleId) throws Exception {
		return this.retrieve(sampleClient.path("samples/{sampleId}"), "sampleId", sampleId);
	}

	@Override
	public List<SampleDTO> retrieveSamples(final Long fkId) throws Exception {

		/*
		Map<String, Object> pathParams = new HashMap<String, Object>();
		pathParams.put("fkId", new Long(3));
		*/

		return this.retrieveList(sampleClient.path("samples/type/{fkId}"), "fkId", fkId);
	}

	@Override
	public List<SampleDTO> retrieveSomeSamples(final List<Long> ids) throws Exception {

		return this.retrieveList(sampleClient.path("samples/multi/{sampleId_array:[\\d,(%2C)]+}"),
				"sampleId_array", ids);
	}

	@Override
	public List<SampleDTO> retrieveSamples() throws Exception {
		return this.retrieveList(sampleClient.path("samples"));
	}

	@Override
	public List<SampleDTO> retrieveByFilter(final IntegrationPaginatedDataFilter filter)
			throws Exception {

		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("columnName", filter.getColumns().get(0));
		queryParams.put("value", filter.getValues().get(0));

		return this.retrieveList(sampleClient.path("samples/filter"), null, queryParams);

	}

	@Override
	public SampleDTO retrieveOneByFilter(final IntegrationPaginatedDataFilter filter)
			throws Exception {
		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("columnName", filter.getColumns().get(0));
		queryParams.put("value", filter.getValues().get(0));

		return this.retrieve(sampleClient.path("samples/filterOne"), null, queryParams);
	}

	/* 
	 * We are using distinct PageDTO objects, since Jackson read Jaxb annotations and It's complex to get a generic list page for XML and JSON 
	 * 
	 * (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.SampleRepository#retrievePagedSamples(org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter)
	 */
	@Override
	public IntegrationPaginatedResult<SampleDTO> retrievePagedSamples(
			final IntegrationPaginatedDataFilter filter) throws Exception
	{
		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("columnName", filter.getColumns().get(0));
		queryParams.put("value", filter.getValues().get(0));

		return this.retrievePagedQuery(sampleClient.path("samples/paged/json/{page}/{pageSize}"),
				filter, null,
				queryParams);

	}

    /*
     * We are using distinct PageDTO objects, since Jackson read Jaxb annotations and It's complex to get a generic list page for XML and JSON
     *
     * (non-Javadoc)
     * @see org.appverse.web.framework.backend.rest.services.integration.SampleRepository#retrievePagedSamples(org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter)
     */
    @Override
    public IntegrationPaginatedResult<SampleDTO> retrievePagedSamplesWithHttpError(
            final IntegrationPaginatedDataFilter filter) throws Exception
    {
        Map<String, Object> queryParams = new HashMap<String, Object>();

        queryParams.put("columnName", filter.getColumns().get(0));
        queryParams.put("value", filter.getValues().get(0));

        return this.retrievePagedQuery(sampleClient.path("samples/nonexistentpath/paged/json/{page}/{pageSize}"),
                filter, null,
                queryParams);

    }

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService#mapPagedResult(javax.ws.rs.core.Response)
	 */
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

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService#getOffsetParamName()
	 */
	@Override
	public String getOffsetParamName() {
		return "page";
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService#getMaxRecordsParamName()
	 */
	@Override
	public String getMaxRecordsParamName() {
		return "pageSize";
	}

	@Override
	public SampleDTO updateSample(final SampleDTO dto) throws Exception {
		return this.update(sampleClient.path("samples/{sampleId}"), dto, "sampleId",
				Long.valueOf(dto.getId()));
	}

	@Override
	public StatusResult updateSampleStatus(final SampleDTO dto) throws Exception {
		return this.updateStatusReturn(sampleClient.path("samples/response/{sampleId}"), dto,
				"sampleId",
				Long.valueOf(dto.getId()));
	}

	@Override
	public SampleDTO createSample(final SampleDTO dto) throws Exception {
		return this.insert(sampleClient.path("samples"), dto);
	}

	@Override
	public StatusResult createSampleStatus(final SampleDTO dto) throws Exception {
		return this.insertStatusReturn(sampleClient.path("samples/response"), dto);
	}

	@Override
	public SampleDTO deleteSample(final Long sampleId) throws Exception {
		return this.delete(sampleClient.path("samples/{sampleId}"), "sampleId", sampleId);
	}

	@Override
	public SampleDTO deleteSampleException(final Long sampleId) throws Exception
	{
		SampleDTO dto = null;
		dto = this.delete(sampleClient.path("samples/exception/{sampleId}"), "sampleId",
				sampleId);

		return dto;
	}

	@Override
	public StatusResult deleteSampleStatus(final Long sampleId)
			throws Exception {

		return this.deleteStatusReturn(sampleClient.path("samples/response/{sampleId}"),
				"sampleId",
				sampleId);
	}

}
