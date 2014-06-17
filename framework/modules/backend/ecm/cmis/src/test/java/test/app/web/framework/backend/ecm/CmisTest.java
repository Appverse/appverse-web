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
package test.app.web.framework.backend.ecm;

import org.apache.chemistry.opencmis.client.api.Session;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.helpers.test.AbstractTest;
import org.appverse.web.framework.backend.ecm.core.model.integration.DocumentDTO;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import test.app.web.framework.backend.ecm.model.integration.NodeDTO;
import test.app.web.framework.backend.ecm.services.integration.DocumentRepository;
import test.app.web.framework.backend.ecm.services.integration.SampleRepository;

import java.util.List;

public class CmisTest extends AbstractTest {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    SampleRepository sampleRepository;

    @AutowiredLogger
    private static Logger logger;

    @Test
    public void cmisManageDocumentTestWithDefaultUser() throws Exception{
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

    @Test
    public void testPrintRepositoryRootFolderWithDefaultUser() throws Exception {
        List<NodeDTO> nodes = sampleRepository.getRootFolderNodes();
        logger.debug("Found " + nodes.size() + " objects (nodes) in the root folder. Printing just the first 5 (maximum) :");

        int numberOfNodesToPrint = 5;
        if (nodes.size() < 5) numberOfNodesToPrint = nodes.size();

        for (int i = 0; i < numberOfNodesToPrint; i++) {
            NodeDTO node = nodes.get(i);
            logger.debug(node.getName());
        }
    }

    @Test
    public void testWithDefaultUser() throws Exception {
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

        // Test a query
        List<NodeDTO> nodesDTOList = sampleRepository.getNodesfromFolderUsingQuery("testfolder");
        Assert.assertNotNull(nodesDTOList);

        // Remove the container folder tree
        documentRepository.deleteFolder("/test");
    }

    // @Test
    /*
     * This test is not enabled as it requires several users to test the CmisSessionManager properly.
     * There is just a public user for the public Alfresco repository we use to run the tests.
     * If you wanted to try it out against your private repository just uncomment it and define
     * the proper users.
     */
    public void testCmisSessionManager() throws Exception{
        // Test designed to work with buffer size = 2 (very small just to try that removes the older sessions properly)
        Session session1 = documentRepository.getCmisSession("user1", "user1");
        Session session2 = documentRepository.getCmisSession("user1", "user1");
        Session session3 = documentRepository.getCmisSession("user1", "user1");
        Assert.assertEquals(session1, session2);
        Assert.assertEquals(session1, session3);

        Session session4 = documentRepository.getCmisSession("user2", "user2");
        Assert.assertNotEquals(session4, session1);

        Session session5 = documentRepository.getCmisSession("user2", "user2");
        Session session55 = documentRepository.getCmisSession("user1", "user1");
        Assert.assertEquals(session55, session1);

        Session session6 = documentRepository.getCmisSession("user2", "user2");
        Session session7 = documentRepository.getCmisSession("yourrepoid", "user2", "user2");
        Session session75 = documentRepository.getCmisSession("user1", "user1");
        Assert.assertEquals(session75, session1);
        Assert.assertEquals(session5, session4);
        Assert.assertEquals(session6, session4);
        Assert.assertEquals(session7, session4);

        Session session8 = documentRepository.getCmisSession("user3", "user3");
        Assert.assertNotEquals(session8, session1);
        Session session85 = documentRepository.getCmisSession("user1", "user1");
        Assert.assertNotEquals(session85, session1);

        Session session9 = documentRepository.getCmisSession("guest", "");
        Session session95 = documentRepository.getCmisSession("user2", "user2");
        Assert.assertNotEquals(session95, session4);
    }

    @Test
    public void testAccessingWithSpecificUserAndPassword() throws Exception{
        // Create document. This will create folder structure to the specified path if necessary
        DocumentDTO document = new DocumentDTO();
        document.setContentStreamFilename("textDocument.txt");
        document.setContentStream("This is the content for this test file".getBytes());
        document.setContentStreamMimeType("text/plain");

        // Example, passing an specific user and password (and reusing the session)
        Session session = documentRepository.getCmisSession("admin", "admin");
        documentRepository.insert("/test/folder1/", document, session);

        // Retrieve the recently created document
        DocumentDTO retrievedDocument = documentRepository.retrieve("/test/folder1/", document.getContentStreamFilename(), session);
        Assert.assertNotNull(retrievedDocument);
        Assert.assertEquals("Document name does not match", retrievedDocument.getContentStreamFilename(), "textDocument.txt");
        Assert.assertNotNull(retrievedDocument.getContentStream());
        Assert.assertNotNull(retrievedDocument.getContentStreamLenght());
        Assert.assertNotNull(retrievedDocument.getContentStreamMimeType());

        // Test a query
        List<NodeDTO> nodesDTOList = sampleRepository.getNodesfromFolderUsingQuery("testfolder", session);
        Assert.assertNotNull(nodesDTOList);

        // Remove the container folder tree
        documentRepository.deleteFolder("/test");
    }

}
