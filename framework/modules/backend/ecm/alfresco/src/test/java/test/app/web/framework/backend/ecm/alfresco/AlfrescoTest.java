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
package test.app.web.framework.backend.ecm.alfresco;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.helpers.test.AbstractTest;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.services.integration.IntegrationException;
import org.appverse.web.framework.backend.ecm.alfresco.model.integration.repository.links.LinkDTO;
import org.appverse.web.framework.backend.ecm.alfresco.services.integration.repository.links.LinkRepository;
import org.appverse.web.framework.backend.ecm.core.model.integration.DocumentDTO;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import test.app.web.framework.backend.ecm.alfresco.services.integration.DocumentRepository;

public class AlfrescoTest extends AbstractTest {

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    DocumentRepository documentRepository;

    @AutowiredLogger
    private static Logger logger;

    @Before
    public void init()
    {
    }


    @Test
    public void cmisManageDocumentTest() throws Exception{
        // Create document. This will create folder structure to the specified path if necessary
        DocumentDTO document = new DocumentDTO();
        document.setContentStreamFilename("textDocument.txt");
        document.setContentStream("This is the content for this test file".getBytes());
        document.setContentStreamMimeType("text/plain");
        documentRepository.insert("/test/folder1/", document);

        // Retrieve the recently created document
        DocumentDTO retrievedDocument = documentRepository.retrieve("/test/folder1/", document.getContentStreamFilename());
        Assert.assertNotNull(retrievedDocument);
        Assert.assertEquals("Document name does not match", retrievedDocument.getContentStreamFilename(), "textDocument.txt");
        Assert.assertNotNull(retrievedDocument.getContentStream());
        Assert.assertNotNull(retrievedDocument.getContentStreamLenght());
        Assert.assertNotNull(retrievedDocument.getContentStreamMimeType());

        // Move the recently created document to another location
        documentRepository.move("/test/folder1/", "textDocument.txt", "/test/folder2", "textDocument2.txt");

        // Retrieve the recently moved document
        DocumentDTO movedDocument = documentRepository.retrieve("/test/folder2/", "textDocument2.txt");
        Assert.assertNotNull(movedDocument);
        Assert.assertEquals("Document name does not match", movedDocument.getContentStreamFilename(), "textDocument2.txt");
        Assert.assertNotNull(movedDocument.getContentStream());
        Assert.assertNotNull(movedDocument.getContentStreamLenght());
        Assert.assertNotNull(movedDocument.getContentStreamMimeType());

        // Remove the container folder tree
        documentRepository.deleteFolder("/test");
    }


    // @Test
    public void alfrescoRESTApiRetrieveLinksTestWithDefaultUser() throws Exception{
        IntegrationPaginatedDataFilter filter = new IntegrationPaginatedDataFilter();
        filter.setLimit(100);
        filter.setOffset(1);
        IntegrationPaginatedResult<LinkDTO> result = new IntegrationPaginatedResult<LinkDTO>();
        result = linkRepository.retrievePagedLinks("webtestsite", "links", filter);
    }

    // @Test
    public void alfrescoRESTApiRetrieveLinksTestParticularUser() throws Exception{
        IntegrationPaginatedDataFilter filter = new IntegrationPaginatedDataFilter();
        filter.setLimit(100);
        filter.setOffset(1);
        IntegrationPaginatedResult<LinkDTO> result = new IntegrationPaginatedResult<LinkDTO>();

        IntegrationException e = null;
        try{
            result = linkRepository.retrievePagedLinks("webtestsite", "links", filter, "user", "wrongpassword");
        }
        catch (IntegrationException ie){
            logger.info("Error code::" + ie.getCode());
            logger.info("Error code::" + ie.getMessage());
            e = ie;
        }
        Assert.assertNotNull(e);
    }

    // @Test
    public void alfrescoRestApiAndCMISCombinedTest() throws Exception{
        // Using repository based in CMIS API
        cmisManageDocumentTest();
        // Using repository based in Alfresco REST API
        alfrescoRESTApiRetrieveLinksTestWithDefaultUser();
    }

}
