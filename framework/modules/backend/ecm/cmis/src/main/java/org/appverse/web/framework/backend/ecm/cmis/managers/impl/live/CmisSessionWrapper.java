package org.appverse.web.framework.backend.ecm.cmis.managers.impl.live;

import org.apache.chemistry.opencmis.client.api.Session;

import java.util.Date;

/**
 *
 */
public class CmisSessionWrapper {

    private Session session;
    private String user;
    private String repositoryId;
    private Date created;
    private Date lastRequested;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastRequested() {
        return lastRequested;
    }

    public void setLastRequested(Date lastRequested) {
        this.lastRequested = lastRequested;
    }

    public String getKey(){
        return user + "-" + repositoryId;
    }
}
