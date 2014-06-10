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
package org.appverse.web.framework.backend.ecm.cmis.managers.impl.live;

import org.apache.chemistry.opencmis.client.api.Session;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.ecm.cmis.factories.CmisSessionFactory;
import org.appverse.web.framework.backend.ecm.cmis.managers.CmisSessionManager;
import org.slf4j.Logger;

import java.util.*;

/**
 * CmisSessionManagerImpl implements a CMIS Session Manager that allows session reuse.
 * Take into account that Open CMIS Session object is thread-safe and it is recommended to reuse as much as possible
 * for performance reasons. The Open CMIS Session object holds the user caches. So it is recommended to use the
 * same Session per user and repository.
 * This implementation allows to reuse sessions and configure a "buffer" limiting the number of existing sessions.
 * When the maximum capacity is reached, the Session Manager will remove the session that shows less activity.
 * In order to create new sessions, the session manager delegates to the CmisSessionFactory.
 */
public class CmisSessionManagerImpl implements CmisSessionManager {

    // TODO: An improvement would be that the CmisSessionManager proactively expired sessions based on
    // an expiration time parameter and session inactivity

    @AutowiredLogger
    private static Logger logger;

    private CmisSessionFactory cmisSessionFactory;

    // Collection of sessions
    private HashMap<String, CmisSessionWrapper> cmisSessionsMap;
    private SortedSet<CmisSessionWrapper> orderedCmisSessionsList;

    // Default number of sessions hold is 10
    private int maxNumberOfSessions = 10;

    public CmisSessionManagerImpl(){
        cmisSessionsMap = new HashMap<String, CmisSessionWrapper>();
        orderedCmisSessionsList = new TreeSet<CmisSessionWrapper>(new sortByLastRequestedDateComparator());
    }

    private Session lookUpSession(String repositoryId, String username){
        CmisSessionWrapper cmisSessionWrapper = cmisSessionsMap.get(username + "-" + repositoryId);
        if (cmisSessionWrapper != null){
            cmisSessionWrapper.setLastRequested(new Date());
            return cmisSessionWrapper.getSession();
        }
        else return null;
    }

    private void registerSession(Session session, String repositoryId, String username){
        CmisSessionWrapper cmisSessionWrapper = new CmisSessionWrapper();
        cmisSessionWrapper.setSession(session);
        cmisSessionWrapper.setRepositoryId(repositoryId);
        cmisSessionWrapper.setUser(username);
        cmisSessionWrapper.setCreated(new Date());
        cmisSessionWrapper.setLastRequested(new Date());

        if (orderedCmisSessionsList.size() >= maxNumberOfSessions){
            // Remove the session that shows more inactivity
            CmisSessionWrapper cmisSessionWrapperToRemove = orderedCmisSessionsList.first();
            orderedCmisSessionsList.remove(cmisSessionWrapperToRemove);
            cmisSessionsMap.remove(cmisSessionWrapperToRemove.getKey());
            logger.debug("Removing CMIS session from manager buffer: " + cmisSessionWrapperToRemove.getSession().toString() + " Buffer size: " + maxNumberOfSessions);
        }
        cmisSessionsMap.put(cmisSessionWrapper.getKey(), cmisSessionWrapper);
        orderedCmisSessionsList.add(cmisSessionWrapper);

        logger.debug("Created and registered new open CMIS session in buffer: " + cmisSessionWrapper.getSession().toString() +
                "Overall number of sessions is: " + orderedCmisSessionsList.size());
    }

    @Override
    public Session getCmisSession(String repositoryId, String username, String password) throws Exception {
        Session session = lookUpSession(repositoryId, username);
        if (session == null){
            // Create an register a new session
            session = cmisSessionFactory.createCmisSession(repositoryId, username, password);
            registerSession(session, repositoryId, username);
        }
        return session;
    }

    @Override
    public Session getCmisSession(String username, String password) throws Exception {
        Session session = lookUpSession(cmisSessionFactory.getRepositoryId(), username);
        if (session == null){
            // Create an register a new session
            session = cmisSessionFactory.createCmisSession(username, password);
            registerSession(session, cmisSessionFactory.getRepositoryId(), username);
        }
        return session;
    }

    @Override
    public Session getCmisSession() throws Exception {
        Session session = lookUpSession(cmisSessionFactory.getRepositoryId(), cmisSessionFactory.getUser());
        if (session == null){
            // Create an register a new session
            session = cmisSessionFactory.createCmisSession();
            registerSession(session, cmisSessionFactory.getRepositoryId(), cmisSessionFactory.getUser());
        }
        return session;
    }

    public int getMaxNumberOfSessions() {
        return maxNumberOfSessions;
    }

    public void setMaxNumberOfSessions(int maxNumberOfSessions) {
        this.maxNumberOfSessions = maxNumberOfSessions;
    }

    public CmisSessionFactory getCmisSessionFactory() throws Exception {
        return cmisSessionFactory;
    }

    public void setCmisSessionFactory(CmisSessionFactory cmisSessionFactory) throws Exception {
        this.cmisSessionFactory = cmisSessionFactory;
    }

    // Comparator to sort the TreeSet by CmisSessionManager lastRequestedDate.
    // Even though this seems to be the best criteria to order sessions in the Collection, in the future
    // might be provided, if necessary, to change the ordering criteria
    private class sortByLastRequestedDateComparator implements Comparator<CmisSessionWrapper>
    {
        @Override
        public int compare(CmisSessionWrapper o1, CmisSessionWrapper o2) {
            return o1.getLastRequested().compareTo(o2.getLastRequested());
        }
    }
}