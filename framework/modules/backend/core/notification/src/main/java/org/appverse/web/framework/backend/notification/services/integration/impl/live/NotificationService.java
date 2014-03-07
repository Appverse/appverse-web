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
package org.appverse.web.framework.backend.notification.services.integration.impl.live;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.notification.model.integration.NPlatformDTO;
import org.appverse.web.framework.backend.notification.model.integration.NUserDTO;
import org.appverse.web.framework.backend.notification.services.integration.INotificationService;
import org.appverse.web.framework.backend.notification.services.integration.NotPlatformRepository;
import org.appverse.web.framework.backend.notification.services.integration.NotUserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Repository("notificationService")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class NotificationService extends AbstractBusinessService implements INotificationService {

    @AutowiredLogger
    private static Logger logger;

    @Autowired
    private NotUserRepository notUserRepository;

    @Autowired
    private NotPlatformRepository notPlatformRepository;

    private Sender googleSender = null;
    private ApnsService appleSender = null;

    @Value("${google.api.key}")
    private String googleApiKey = "";

    @Value("${apple.p12.path}")
    private String appleP12Path = "";

    @Value("${apple.p12.pwd}")
    private String appleP12Password = "";

    @Override
    public void registerUser(NUserDTO nUserDTO) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println("Persisting user ["+nUserDTO.getUserId()+"]");
        long l = notUserRepository.createUser(nUserDTO);
        System.out.println("User Persisted with id ["+l+"]");
    }

    @Override
    public void updateUser(NUserDTO nUserDTO) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NUserDTO retrieveUser(String userId) throws Exception {
        IntegrationDataFilter idf = new IntegrationDataFilter();
        idf.addLikeCondition("userId",userId);
        return notUserRepository.retrieve(idf);
    }

    @Override
    public void addPlatformToUser(String userId, NPlatformDTO nPlatformDTO) throws Exception {
        //retrieve user by userId
        NUserDTO nUserDTO = retrieveUser(userId);
        if( nUserDTO == null) {
            throw new Exception("User ["+userId+"] not found.");
        }
        //first persist nPlatformDTO
        long platId = notPlatformRepository.persist(nPlatformDTO);
        nUserDTO.getNotPlatformDTOs().add(nPlatformDTO);

        //now save the User so the relations are finaly set
        notUserRepository.persist(nUserDTO);
    }

    @Override
    public boolean sendNotification(String userId, List<String> platformIds) throws Exception {
        List<NPlatformDTO> nPlatformDTOs = new ArrayList<NPlatformDTO>();
        for( String platId: platformIds) {
            IntegrationDataFilter idf = new IntegrationDataFilter();
            idf.addLikeCondition("platformId",platId);
            NPlatformDTO nPlatformDTO = notPlatformRepository.retrieve(idf);
            nPlatformDTOs.add(nPlatformDTO);
        }
        for( NPlatformDTO platformDTO: nPlatformDTOs) {
            System.out.println("Sending notification to ("+platformDTO.getnPlatformTypeDTO().getName()+","+platformDTO.getToken()+")");
            sendNotification(platformDTO.getnPlatformTypeDTO().getName(),platformDTO.getToken(), "Nomina ingressada: 81.333€€");
        }

        return false;
    }

    @Override
    public void updatePlatform(String userId, String platformId, String token) throws Exception {
        //retrieve user by userId
        NUserDTO nUserDTO = retrieveUser(userId);
        if( nUserDTO == null) {
            throw new Exception("User ["+userId+"] not found.");
        }
        IntegrationDataFilter idf = new IntegrationDataFilter();
        idf.addLikeCondition("platformId",platformId);
        NPlatformDTO nPlatformDTO = notPlatformRepository.retrieve(idf);
        if( nPlatformDTO == null ) {
            throw new Exception("Platform ["+platformId+"] not found.");
        }

        nPlatformDTO.setToken(token);
        long result = notPlatformRepository.persist(nPlatformDTO);
    }

    @Override
    public boolean sendNotification(String platform, String token, String body) throws Exception {
        checkSenders(platform);
        if ("android".equals(platform)) {
            Message.Builder build = new Message.Builder();

            if (body != null) {
                build.addData("body", body);
            }

            Message msg = build.build();
            Result rs = googleSender.send(msg, token, 1);
            if( rs != null ) {
                System.out.println("getCanonicalRegistrationId  :"+rs.getCanonicalRegistrationId());
                System.out.println("getMessageId                :"+rs.getMessageId());
                System.out.println("getErrorCodeName            :"+rs.getErrorCodeName());
            } else {
                System.out.println(" Result is null !");
            }
            return true;
        } else if ("ios".equals(platform)) {
            String payload = APNS.newPayload().alertBody(body).build();
            appleSender.push(token, payload);
            return true;
        }

        return false;
    }

    public void outputData() throws Exception {
        System.out.println("Notification Service data output");
        System.out.println("   google api key ["+googleApiKey+"]");
        System.out.println("   apple path ["+appleP12Path+"]");
    }

    private void checkSenders(String platform) throws Exception {
        if("android".equals(platform) && googleSender == null ) {
            if( googleApiKey == null || googleApiKey.length()==0) {
                throw new Exception("Google API KEY not found.");
            }
            logger.info("Creating android sender with key ["+googleApiKey+"]");
            googleSender = new Sender(googleApiKey);
        } else if( "ios".equals(platform) && appleSender == null) {
            if( appleP12Path == null || appleP12Password == null || appleP12Path.length()==0 || appleP12Password.length()==0 ) {
                throw new Exception("Apple certificate path not found.");
            }
            logger.info("Creating ios sender with path ["+appleP12Path+"]");
            appleSender = APNS.newService()
                    .withCert(appleP12Path, appleP12Password)
                    .withProductionDestination()
                    .build();
        }
    }

}
