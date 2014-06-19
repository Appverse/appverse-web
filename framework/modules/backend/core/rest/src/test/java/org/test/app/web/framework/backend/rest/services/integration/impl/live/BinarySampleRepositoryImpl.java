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

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;
import org.appverse.web.framework.backend.rest.services.integration.IRestPersistenceService;
import org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;
import org.test.app.web.framework.backend.rest.services.integration.BinarySampleRepository;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

@Repository("sampleRepositoryBinary")
public class BinarySampleRepositoryImpl extends RestPersistenceService<SampleDTO>
		implements BinarySampleRepository, IRestPersistenceService<SampleDTO> {

	@Autowired
	private WebTarget sampleClient;

	@AutowiredLogger
	private static Logger logger;

	@Override
	public InputStream getFile(final Long fileId) throws Exception {
		return this.retrieveInputStream(sampleClient.path("samples/file/{fileId}"), "fileId",
				fileId, null, null);
	}

	@Override
	public StatusResult createFile(final Long fileId, final InputStream data) throws Exception {
		/*
				Response response = sampleClient.path("samples/file")
						.request()
						.accept(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(data, acceptMediaType()));
				*/
		Response response = this.insert(sampleClient.path("samples/file"), data, null, null);
		return getStatusResult(response);
	}

	@Override
	public String acceptMediaType()
	{
		return MediaType.APPLICATION_JSON;
	}

	@Override
	public List<SampleDTO> getTypeSafeList() {
		// TODO Auto-generated method stub
		return null;
	}

}
