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
package org.appverse.web.framework.backend.ecm.cmis.factories;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * CmisSessionFactory implements a factory in charge to create new Open CMIS sessions
 */
public class CmisSessionFactory implements InitializingBean {

    private SessionFactory sessionFactory;
    private Map cmisSessionInitProperties;

    public CmisSessionFactory(){
        sessionFactory = SessionFactoryImpl.newInstance();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String defaultRepositoryId = (String)cmisSessionInitProperties.get(SessionParameter.REPOSITORY_ID);
        if (defaultRepositoryId == null){
            // If not repository is specified, the first repository is taken as default
            List<Repository> repositories = sessionFactory.getRepositories(cmisSessionInitProperties);
            Repository repository = repositories.get(0);
            cmisSessionInitProperties.put(SessionParameter.REPOSITORY_ID, repository.getId());
        }
    }


    public Session createCmisSession(String repositoryId, String username, String password){
        cmisSessionInitProperties.put(SessionParameter.REPOSITORY_ID, repositoryId);
        cmisSessionInitProperties.put(SessionParameter.USER, username);
        cmisSessionInitProperties.put(SessionParameter.PASSWORD, password);
        return sessionFactory.createSession(cmisSessionInitProperties);
    }

    public Session createCmisSession(String username, String password){
        cmisSessionInitProperties.put(SessionParameter.USER, username);
        cmisSessionInitProperties.put(SessionParameter.PASSWORD, password);
        return sessionFactory.createSession(cmisSessionInitProperties);
    }

    public Session createCmisSession() throws Exception {
        if (cmisSessionInitProperties.get(SessionParameter.USER) == null || cmisSessionInitProperties.get(SessionParameter.USER) == null) {
            throw new Exception("CmisSessionFactoryBean: not default user or password specified and no credentials have been explicitly specified");
        }
        return sessionFactory.createSession(cmisSessionInitProperties);
    }

    public void setCmisSessionInitProperties(Map cmisSessionInitProperties) {
        this.cmisSessionInitProperties = cmisSessionInitProperties;
    }

    public Map getCmisSessionInitProperties() {
        return cmisSessionInitProperties;
    }

    public String getRepositoryId(){
        return (String)cmisSessionInitProperties.get(SessionParameter.REPOSITORY_ID);
    }

    public String getUser(){
        return (String)cmisSessionInitProperties.get(SessionParameter.USER);
    }

    public String getPassword(){
        return (String)cmisSessionInitProperties.get(SessionParameter.PASSWORD);
    }

}