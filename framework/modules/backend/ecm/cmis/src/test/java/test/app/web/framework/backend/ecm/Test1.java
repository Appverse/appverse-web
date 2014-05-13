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
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.appverse.web.framework.backend.api.helpers.test.AbstractTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 extends AbstractTest {

    @Test
    public void test() {
        
        System.out.println(Test1.class.getName() + " started");

        // Create a SessionFactory and set up the SessionParameter map
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        // connection settings - we are connecting to a public cmis repo,
        // using the AtomPUB binding
//        parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11" /*" http://repo.opencmis.org/inmemory/atom/"*/);
//        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        // BROWSER BINDING  CMIS 1.1
/*
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());
        parameter.put(SessionParameter.BROWSER_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/browser");
*/

        // ATOM CMIS 1.1
/*
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11");
*/

        // ATOM CMIS 1.0
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom");

        // find all the repositories at this URL - there should only be one.
        List<Repository> repositories = new ArrayList<Repository>();
        repositories = sessionFactory.getRepositories(parameter);
        for (Repository r : repositories) {
            System.out.println("Found repository: " + r.getName());
            System.out.println("Repsoitory CMIS version: " + r.getCmisVersion());
            System.out.println("Repsoitory CMIS version supported: " + r.getCmisVersionSupported());
        }

        // create session with the first (and only) repository
        Repository repository = repositories.get(0);
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());
        Session session = sessionFactory.createSession(parameter);

        System.out.println("Got a connection to repository: " + repository.getName() + ", with id: "
                + repository.getId());

        // TEST: que pasa si se intenta crear un tipo (soportado en CMIS 1.1 pero con un repo 1.0??)

        TypeDefinition typeDefinitionTest = new TypeDefinition() {
            @Override
            public String getId() {
                return "test";
            }

            @Override
            public String getLocalName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getLocalNamespace() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDisplayName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getQueryName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDescription() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public BaseTypeId getBaseTypeId() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getParentTypeId() {
                return "test";
            }

            @Override
            public Boolean isCreatable() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isFileable() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isQueryable() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isFulltextIndexed() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isIncludedInSupertypeQuery() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isControllablePolicy() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean isControllableAcl() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Map<String, PropertyDefinition<?>> getPropertyDefinitions() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public TypeMutability getTypeMutability() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<CmisExtensionElement> getExtensions() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        session.createType(typeDefinitionTest);


        // Get everything in the root folder and print the names of the objects
        Folder root = session.getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        System.out.println("Found the following objects in the root folder:-");
        for (CmisObject o : children) {
            System.out.println(o.getName());
        }
        
        System.out.println(Test1.class.getName() + " ended");
    }
}
