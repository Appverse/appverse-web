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
package org.test.app.web.framework.backend.rest.services.integration;

import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.rest.annotations.RestCaching;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;

import java.util.List;

@RestCaching(cacheName = "sampleRestClientCache")
public interface SampleRepository {

	// GET
	SampleDTO retrieveSample(final Long sampleId) throws Exception;

	List<SampleDTO> retrieveSamples(final Long fkId) throws Exception;

	List<SampleDTO> retrieveSomeSamples(final List<Long> ids) throws Exception;

	List<SampleDTO> retrieveSamples() throws Exception;

	List<SampleDTO> retrieveByFilter(IntegrationPaginatedDataFilter filter) throws Exception;

	SampleDTO retrieveOneByFilter(IntegrationPaginatedDataFilter filter) throws Exception;

	SampleDTO updateSample(SampleDTO dto) throws Exception;

	StatusResult updateSampleStatus(SampleDTO dto) throws Exception;

	SampleDTO createSample(SampleDTO dto) throws Exception;

	StatusResult createSampleStatus(SampleDTO dto) throws Exception;

	// DELETE

	SampleDTO deleteSample(Long id) throws Exception;

	SampleDTO deleteSampleException(Long id) throws Exception;

	StatusResult deleteSampleStatus(Long sampleId) throws Exception;

	IntegrationPaginatedResult<SampleDTO> retrievePagedSamples(
			IntegrationPaginatedDataFilter filter)
			throws Exception;

    IntegrationPaginatedResult<SampleDTO> retrievePagedSamplesWithHttpError(
            final IntegrationPaginatedDataFilter filter) throws Exception;

}
