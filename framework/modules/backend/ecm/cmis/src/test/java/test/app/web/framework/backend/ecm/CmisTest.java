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

        // Remove the container folder tree
        documentRepository.deleteFolder("/test");
    }

    @Test
    public void testPrintRepositoryRootFolderWithDefaultUser() throws Exception {
        List<NodeDTO> nodes = sampleRepository.getRootFolderNodes();
        logger.info("Found the following objects (nodes) in the root folder :");
        for (NodeDTO node : nodes) {
            logger.info(node.getName());
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

    @Test
    public void testAccessingWithSpecificUserAndPassword() throws Exception{
        // Create document. This will create folder structure to the specified path if necessary
        DocumentDTO document = new DocumentDTO();
        document.setContentStreamFilename("textDocument.txt");
        document.setContentStream("This is the content for this test file".getBytes());
        document.setContentStreamMimeType("text/plain");
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
