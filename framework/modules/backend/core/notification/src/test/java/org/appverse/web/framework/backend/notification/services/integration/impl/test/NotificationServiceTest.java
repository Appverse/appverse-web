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
package org.appverse.web.framework.backend.notification.services.integration.impl.test;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.notification.services.integration.INotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import twitter4j.TwitterException;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
        })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/application-config.xml")
public class NotificationServiceTest extends AbstractJUnit4SpringContextTests {

    public static final String SAMPLE_MESSAGE = "Please complete the transaction";

    @AutowiredLogger
    private static Logger logger;

    @Autowired
    INotificationService notificationService;




    @Test
    public void testSendMessageAndroidOK() throws Exception {
        notificationService.sendNotification("android","APA91bFZbatlcLrqG-mryOjt0tkxDhGMu66vw0qzpsa7nUfvMuguQXGDqj6f0YJWN800cuR-dCGmveyZpo-hkEgRRfO3Yw1z2hjNbf6x986pLSJO9wUsEjvs-oahKc4wdQQoElpqIM3nqYoM6kE3mxJyjjVoqu_G_A",SAMPLE_MESSAGE);
    }
    @Test
    public void testSendMessageAndroidWithParamsOK()throws Exception{
        Map<String,String> params = new HashMap<String,String>();
        params.put("params","{}");
        notificationService.sendNotification("android","APA91bFZbatlcLrqG-mryOjt0tkxDhGMu66vw0qzpsa7nUfvMuguQXGDqj6f0YJWN800cuR-dCGmveyZpo-hkEgRRfO3Yw1z2hjNbf6x986pLSJO9wUsEjvs-oahKc4wdQQoElpqIM3nqYoM6kE3mxJyjjVoqu_G_A",SAMPLE_MESSAGE,params);
    }
    @Test
    public void testSendMessageFacebookOK()throws Exception{
        notificationService.sendNotification("facebook","",SAMPLE_MESSAGE);
    }
    @Test
    public void testSendMessageTwitterOK()throws Exception{
        //direct message to itself
        notificationService.sendNotification("twitter","@AppverseBank",SAMPLE_MESSAGE);
    }
    @Test(expected = TwitterException.class)
    public void testSendMessageTwitterKO()throws Exception{
        //direct message to someone that does not follow you -->TwitterException
        notificationService.sendNotification("twitter","@someone",SAMPLE_MESSAGE);
    }


    @Test
    public void testUpdateToken() throws Exception {
    }

    @Test
    public void testNotificationServiceGoogle() throws Exception {
        assertNotNull(notificationService);
        notificationService.outputData();
    }

}
