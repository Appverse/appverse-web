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

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.helpers.test.AbstractTest;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.ecm.alfresco.model.integration.LinkDTO;
import org.appverse.web.framework.backend.ecm.alfresco.services.integration.LinkRepository;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class AlfrescoTest extends AbstractTest {

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    Session cmisSession;

    @Autowired
    Session cmisSessionRepo2;

    @AutowiredLogger
    private static Logger logger;

    @Before
    public void init()
    {
    }


    @Test
    public void testTwoRepositoriesPrintRootFolder() {
        logger.info(AlfrescoTest.class.getName() + " started");

        // Get everything in the root folder and print the names of the objects
        Folder root = cmisSession.getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        logger.info("Found the following objects in the root folder for repository 1 :-");
        for (CmisObject o : children) {
            logger.info(o.getName());
        }

        // Get everything in the root folder and print the names of the objects
        root = cmisSessionRepo2.getRootFolder();
        children = root.getChildren();
        System.out.println("Found the following objects in the root folder for repository 2 :-");
        for (CmisObject o : children) {
            System.out.println(o.getName());
        }
        logger.info(AlfrescoTest.class.getName() + " ended");
    }

    @Test
    public void testQuery() throws Exception
    {
        Folder root = cmisSession.getRootFolder();

        // Removing the test folder that might have been created in previous tests
        try{
            Folder folder = (Folder) FileUtils.getObject("/testfolder", cmisSession);
            folder.deleteTree(true, UnfileObject.DELETE, true);
        }
        catch (CmisObjectNotFoundException e){
            // The folder did not exist previously
        }

        //creating the test folder
        Map<String, Object> folderProperties = new HashMap<String, Object>();
        folderProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        folderProperties.put(PropertyIds.NAME, "testfolder");
        Folder newFolder = root.createFolder(folderProperties);

        // Create a new content in the folder
        String name = "testfile.txt";
        // properties
        // (minimal set: name and object type id)
        Map<String, Object> contentProperties = new HashMap<String, Object>();
        contentProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        contentProperties.put(PropertyIds.NAME, name);

        // content
        byte[] content = "CMIS test data".getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        ContentStream contentStream = new ContentStreamImpl(name, new BigInteger(content), "text/plain", stream);

        // create a major version
        Document newContent1 =  newFolder.createDocument(contentProperties, contentStream, null);
        logger.info("DocumentDTO created: " + newContent1.getId());

        ItemIterable<QueryResult> results = cmisSession.query("SELECT * FROM cmis:folder WHERE cmis:name='testfolder'", false);
        for (QueryResult result : results) {
            String id = result.getPropertyValueById(PropertyIds.OBJECT_ID);
            Assert.assertNotNull(id);
        }
    }


    @Test
    public void alfrescoRESTApiRetrieveLinksTest() throws Exception{
        IntegrationPaginatedDataFilter filter = new IntegrationPaginatedDataFilter();
        filter.setLimit(100);
        filter.setOffset(1);

        IntegrationPaginatedResult<LinkDTO> result = new IntegrationPaginatedResult<LinkDTO>();
        result = linkRepository.retrievePagedLinks("webtestsite", "links", filter);
    }

    @Test
    public void alfrescoRestApiAndCMISCombinedTest() throws Exception{
        // Retrieving root repository folder using CMIS API
        testTwoRepositoriesPrintRootFolder();

        // Retrieve links using Alfresco REST API by means LinkRepository
        IntegrationPaginatedDataFilter filter = new IntegrationPaginatedDataFilter();
        filter.setLimit(10);
        filter.setOffset(1);

        IntegrationPaginatedResult<LinkDTO> result = new IntegrationPaginatedResult<LinkDTO>();
        result = linkRepository.retrievePagedLinks("webtestsite", "links", filter);
    }

}
