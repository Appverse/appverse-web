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
package org.appverse.web.framework.backend.ecm.alfresco.services.integration.impl.live.repository.links;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.ecm.alfresco.model.integration.repository.links.LinkDTO;
import org.appverse.web.framework.backend.ecm.alfresco.model.integration.repository.links.PageDTO;
import org.appverse.web.framework.backend.ecm.alfresco.services.integration.repository.links.LinkRepository;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.services.integration.impl.live.RestPersistenceService;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LinkRepository implementation corresponding to Alfresco REST API "links" package:
 * http://docs.alfresco.com/4.1/references/RESTful-Links.html
 */
@Repository("linkRepository")
public class LinkRepositoryImpl extends RestPersistenceService<LinkDTO>
        implements LinkRepository {


    @Autowired
    private WebTarget alfrescoRestClient;

    @AutowiredLogger
    private static Logger logger;


    @Override
    public List<LinkDTO> getTypeSafeList() {
        return null;
    }

    @Override
    public IntegrationPaginatedResult<LinkDTO> mapPagedResult(final Response response)
            throws Exception {

        GenericType<PageDTO<LinkDTO>> genericType = new GenericType<PageDTO<LinkDTO>>() {
        };

        PageDTO<LinkDTO> page = response.readEntity(genericType);
        IntegrationPaginatedResult<LinkDTO> result = new IntegrationPaginatedResult<LinkDTO>();

        result.setData(page.getItems());
        result.setTotalLength(page.getTotal());
        result.setOffset(page.getStartIndex());

        return result;
    }

    @Override
    public IntegrationPaginatedResult<LinkDTO> retrievePagedLinks(final String site, final String container,
            final IntegrationPaginatedDataFilter filter) throws Exception {
        return retrievePagedLinks(site, container, filter, null, null);
    }

    @Override
    public IntegrationPaginatedResult<LinkDTO> retrievePagedLinks(final String site, final String container,
        final IntegrationPaginatedDataFilter filter,
        final String user,
        final String password) throws Exception {

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("page", (filter.getOffset() / filter.getLimit()) + 1 );
        queryParams.put("pageSize", filter.getLimit());

        HashMap<String,Object> properties = null;
        if (user != null && password != null){
            properties = new HashMap<String,Object>();
            properties.put(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, user);
            properties.put(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
        }
        return this.retrievePagedQuery(alfrescoRestClient.path("links/site/"+ site +"/" + container),
                filter, null, queryParams, properties);
    }


}
