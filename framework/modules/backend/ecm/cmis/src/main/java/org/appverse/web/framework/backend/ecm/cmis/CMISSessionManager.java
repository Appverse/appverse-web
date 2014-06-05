package org.appverse.web.framework.backend.ecm.cmis;

import org.apache.chemistry.opencmis.client.api.Session;

public class CmisSessionManager {

    // List of sessions


    CmisSessionFactory cmisSessionFactory;

    public Session getCmisSession(String repositoryId, String username, String password) throws Exception {
        return cmisSessionFactory.createCmisSession();
    }

    public Session getCmisSession(String username, String password) throws Exception {
        return cmisSessionFactory.createCmisSession();
    }

    public Session getCmisSession() throws Exception {
        return cmisSessionFactory.createCmisSession();
    }

    public CmisSessionFactory getCmisSessionFactory() throws Exception {
        return cmisSessionFactory;
    }

    public void setCmisSessionFactory(CmisSessionFactory cmisSessionFactory) throws Exception {
        this.cmisSessionFactory = cmisSessionFactory;
    }
}