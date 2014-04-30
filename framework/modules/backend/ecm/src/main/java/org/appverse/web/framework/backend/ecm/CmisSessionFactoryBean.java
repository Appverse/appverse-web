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
package org.appverse.web.framework.backend.ecm;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import org.apache.chemistry.opencmis.client.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmisSessionFactoryBean implements FactoryBean<Session>, BeanNameAware, InitializingBean {

    String beanName;

    SessionFactory sessionFactory;

    Session session;
    private Map cmisSessionInitProperties;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Create a SessionFactory and set up the SessionParameter map
        sessionFactory = SessionFactoryImpl.newInstance();

        // Binding examples

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
/*
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom");
*/

        // create session with the first (and only) repository
        List<Repository> repositories = new ArrayList<Repository>();
        repositories = sessionFactory.getRepositories(cmisSessionInitProperties);
        Repository repository = repositories.get(0);
        cmisSessionInitProperties.put(SessionParameter.REPOSITORY_ID, repository.getId());
        session = sessionFactory.createSession(cmisSessionInitProperties);
    }

    @Override
    public Session getObject() throws Exception {
        return this.session;
    }

    @Override
    public Class<Session> getObjectType() {
        return Session.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCmisSessionInitProperties(Map cmisSessionInitProperties) {
        this.cmisSessionInitProperties = cmisSessionInitProperties;
    }

    public Map getCmisSessionInitProperties() {
        return cmisSessionInitProperties;
    }
}
