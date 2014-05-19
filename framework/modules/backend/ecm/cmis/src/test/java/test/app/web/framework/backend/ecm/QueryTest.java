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

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryTest {
    // private static final String CMIS_ENDPOINT_TEST_SERVER = "http://localhost:8080/inmemory/atom";
    private static final String CMIS_ENDPOINT_TEST_SERVER = "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11";

    private Session session;
    
    private void getCmisClientSession(){
        // default factory implementation
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<String, String>();
        // user credentials
        parameters.put(SessionParameter.USER, "dummyuser");
        parameters.put(SessionParameter.PASSWORD, "dummysecret");
        // connection settings
        parameters.put(SessionParameter.ATOMPUB_URL, 
                CMIS_ENDPOINT_TEST_SERVER );
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB 
                .value());
        // create session
        session =  factory.getRepositories(parameters).get(0).createSession();
    }

    public void createTestArea()
            throws Exception
            {

        //creating a new folder
        Folder root = session.getRootFolder();
        Map<String, Object> folderProperties = new HashMap<String, Object>();
        folderProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        folderProperties.put(PropertyIds.NAME, "testdata");

        Folder newFolder = root.createFolder(folderProperties);
        //create a new content in the folder
        String name = "testdata1.txt";
        // properties
        // (minimal set: name and object type id)
        Map<String, Object> contentProperties = new HashMap<String, Object>();
        contentProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        contentProperties.put(PropertyIds.NAME, name);

        // content
        byte[] content = "CMIS Testdata One".getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        ContentStream contentStream = new ContentStreamImpl(name, new BigInteger(content), "text/plain", stream);

        // create a major version
        Document newContent1 =  newFolder.createDocument(contentProperties, contentStream, null);
        System.out.println("DocumentDTO created: " + newContent1.getId());
    }

    private void doQuery() {
        ItemIterable<QueryResult> results = session.query("SELECT * FROM cmis:folder WHERE cmis:name='testdata'", false);
        for (QueryResult result : results) {
            String id = result.getPropertyValueById(PropertyIds.OBJECT_ID);
            System.out.println("doQuery() found id: " + id);
        }
    }
    
    public static void main(String args[]) {
        QueryTest o = new QueryTest();
        try {
            o.getCmisClientSession();
            o.createTestArea();
            o.doQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void xmain(String args[]) {
        
        System.out.println(QueryTest.class.getName() + " started");

        // Create a SessionFactory and set up the SessionParameter map
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        // connection settings - we're connecting to a public cmis repo,
        // using the AtomPUB binding
        // parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/inmemory/atom/");

        parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11/");

        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        // find all the repositories at this URL - there should only be one.
        List<Repository> repositories = new ArrayList<Repository>();
        repositories = sessionFactory.getRepositories(parameter);
        for (Repository r : repositories) {
            System.out.println("Found repository: " + r.getName());
        }

        // create session with the first (and only) repository
        Repository repository = repositories.get(0);
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());
        Session session = sessionFactory.createSession(parameter);

        System.out.println("Got a connection to repository: " + repository.getName() + ", with id: "
                + repository.getId());

//        // Get everything in the root folder and print the names of the objects
//        Folder root = session.getRootFolder();
//        ItemIterable<CmisObject> children = root.getChildren();
//        System.out.println("Found the following objects in the root folder:-");
//        for (CmisObject o : children) {
//            System.out.println(o.getName());
//        }
//        
        System.out.println(QueryTest.class.getName() + " ended");
    }
}
