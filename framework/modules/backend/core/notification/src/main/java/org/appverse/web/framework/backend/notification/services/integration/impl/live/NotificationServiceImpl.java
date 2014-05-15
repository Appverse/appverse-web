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
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
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
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("notificationService")
public class NotificationServiceImpl extends AbstractBusinessService implements INotificationService, ResourceLoaderAware {

    @AutowiredLogger
    private static Logger logger;

    @Autowired
    private NotUserRepository notUserRepository;

    @Autowired
    private NotPlatformRepository notPlatformRepository;

    private Sender googleSender = null;
    private ApnsService appleSender = null;

    private ResourceLoader resourceLoader;

    @Value("${google.api.key}")
    private String googleApiKey = "";

    @Value("${apple.p12.path}")
    private String appleP12Path = "";

    @Value("${apple.p12.pwd}")
    private String appleP12Password = "";

    @Override
    public void registerUser(NUserDTO nUserDTO) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
        logger.info("Persisting user ["+nUserDTO.getUserId()+"]");
        long l = notUserRepository.createUser(nUserDTO);
        logger.info("User Persisted with id ["+l+"]");
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
        nUserDTO.getNotPlatforms().add(nPlatformDTO);

        //now save the User so the relations are finaly set
        notUserRepository.persist(nUserDTO);
    }

    @Override
    public boolean sendNotification(String userId, List<String> platformIds, String body) throws Exception {
        return sendNotification(userId,platformIds,body,null);
    }
    @Override
    public boolean sendNotification(String userId, List<String> platformIds, String body, Map<String,String> params) throws Exception {
        boolean result = false;
        List<NPlatformDTO> nPlatformDTOs = new ArrayList<NPlatformDTO>();
        for( String platId: platformIds) {
            IntegrationDataFilter idf = new IntegrationDataFilter();
            idf.addLikeCondition("platformId",platId);
            NPlatformDTO nPlatformDTO = notPlatformRepository.retrieve(idf);
            nPlatformDTOs.add(nPlatformDTO);
        }
        for( NPlatformDTO platformDTO: nPlatformDTOs) {
            result = sendNotification(platformDTO.getNotPlatformType().getName(),platformDTO.getToken(), body, params);
        }

        return result;
    }

    @Override
    public boolean sendNotificationByPlatform(String userId, List<NPlatformDTO> nPlatformDTOs, String body) throws Exception {
        boolean result = false;
        for( NPlatformDTO platformDTO: nPlatformDTOs) {
            result = sendNotification(platformDTO.getNotPlatformType().getName(),platformDTO.getToken(), body);
        }
        return result;
    }
    @Override
    public boolean sendNotificationByPlatform(String userId, List<NPlatformDTO> nPlatformDTOs, String body, Map<String,String> params) throws Exception {
        boolean result = false;
        for( NPlatformDTO platformDTO: nPlatformDTOs) {
            result = sendNotification(platformDTO.getNotPlatformType().getName(),platformDTO.getToken(), body, params);
        }
        return result;
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
        return sendNotification(platform,token,body,null);
    }

    @Override
    public boolean sendNotification(String platform, String token, String body, Map<String,String> params) throws Exception {
        logger.info("Sending notification to ("+platform+","+token+")");
        checkSenders(platform);
        if ("android".equals(platform)) {
            Message.Builder build = new Message.Builder();

            //add special parameters
            if (params!=null && !params.isEmpty()) {
                //removes an element body
                params.remove("body");
                build.setData(params);
            }
            if (body != null) {
                build.addData("body", body);
            }

            Message msg = build.build();
            Result rs = googleSender.send(msg, token, 1);
            if( rs != null ) {
                logger.debug("getCanonicalRegistrationId  :"+rs.getCanonicalRegistrationId());
                logger.debug("getMessageId                :"+rs.getMessageId());
                logger.debug("getErrorCodeName            :"+rs.getErrorCodeName());
                return true;
            } else {
                logger.debug(" Send Result is null !");
                return false;
            }
        } else if ("ios".equals(platform)) {
            String strRelPath = null;

            Resource resource = resourceLoader.getResource(appleP12Path);

            try {
                List<PushedNotification> notifications = new ArrayList<PushedNotification>();
                if (params != null && !params.isEmpty()) {
                    //create a payload
                    PushNotificationPayload payload = PushNotificationPayload.complex();
                    payload.addAlert(body);
                    //add special parameters
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        payload.addCustomDictionary(entry.getKey(), entry.getValue());
                    }
                    //send
                    notifications = Push.payload(payload, resource.getURL().getPath(), appleP12Password, true, token);
                } else{
                    notifications = Push.alert(body, resource.getURL().getPath(), appleP12Password, true, token);
                }
                //Push.test(strRelPath, "apple2014", true, token);
                for (PushedNotification notification : notifications) {
                    if (notification.isSuccessful()) {
                        // Apple accepted the notification and should deliver it
                    } else {
                        String invalidToken = notification.getDevice().getToken();
                        //Add code here to remove invalidToken from your database
                        logger.warn("Invalid token:{}",invalidToken);
                    }
                }
            } catch (CommunicationException ex) {
                //Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                logger.error("CommunicationException", ex);
                throw ex;
            } catch (KeystoreException ex) {
                logger.error("KeyStoreException", ex);
                //Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }


        }

        return false;
    }

    public void outputData() throws Exception {
        logger.info("Notification Service data output");
        logger.info("   google api key ["+googleApiKey+"]");
        logger.info("   apple path ["+appleP12Path+"]");
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
            Resource resource = resourceLoader.getResource(appleP12Path);
            appleSender = APNS.newService()
                    .withCert(resource.getURL().getPath(), appleP12Password)
                    .withProductionDestination()
                    .build();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
