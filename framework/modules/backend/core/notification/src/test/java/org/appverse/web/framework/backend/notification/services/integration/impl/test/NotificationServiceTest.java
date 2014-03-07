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

import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.notification.model.integration.NPlatformDTO;
import org.appverse.web.framework.backend.notification.model.integration.NPlatformTypeDTO;
import org.appverse.web.framework.backend.notification.model.integration.NUserDTO;
import org.appverse.web.framework.backend.notification.services.integration.INotificationService;
import org.appverse.web.framework.backend.notification.services.integration.NotPlatformRepository;
import org.appverse.web.framework.backend.notification.services.integration.NotPlatformTypeRepository;
import org.appverse.web.framework.backend.notification.services.integration.NotUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.appverse.web.framework.backend.api.helpers.test.AbstractTransactionalTest;


import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

//@ContextConfiguration(locations = { "classpath:/spring/application-config.xml" })
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@TestExecutionListeners({NotificationServiceTest.WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class NotificationServiceTest extends AbstractTransactionalTest {



    @Autowired
    INotificationService notificationService;

    @Autowired
    NotUserRepository notUserRepository;

    @Autowired
    NotPlatformRepository notPlatformRepository;

    @Autowired
    NotPlatformTypeRepository notPlatformTypeRepository;

    public static class WebContextTestExecutionListener extends AbstractTestExecutionListener {
        @Override
        public void prepareTestInstance(TestContext testContext) {
            if (testContext.getApplicationContext() instanceof GenericApplicationContext) {
                GenericApplicationContext context = (GenericApplicationContext) testContext.getApplicationContext();
                ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
                //beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST,
                //        new SimpleThreadScope());
                beanFactory.registerScope(WebApplicationContext.SCOPE_SESSION,
                        new SimpleThreadScope());
            }
        }
    }

    @Before
    public void registerPlatformAndUser() throws Exception {
        assertNotNull(notificationService);
        assertNotNull(notUserRepository);
        assertNotNull(notPlatformTypeRepository);

        NPlatformTypeDTO platformType = notPlatformTypeRepository.retrieve(2);
        assertNotNull(platformType);

        NPlatformDTO nPlatform = new NPlatformDTO();
        nPlatform.setStatus("enabled");
        nPlatform.setAlias("myAndroidPhone");
        nPlatform.setAppVersion("testApp");
        nPlatform.setToken("APA91bElESbkkgYq84xUxUc5GsiT_TLYSLps-AdUvYYLAHTEPoCQJp7CnYNSSYCWZqejy2ozeRZgW1PPosRFXgWK5sfEu-brvGvflxlPg7hgjhQzkjOwr_wjuZOKHYyaD0c6MYqT08TBq-Q7MtBinWbCIYylqFb11Q");
        nPlatform.setnPlatformTypeDTO(platformType);
        nPlatform.setPlatformId("theplatformid");
        long platId = notPlatformRepository.persist(nPlatform);
        System.out.println("Platform persisted ["+platId+"]");
        //System.out.println(notPlatformRepository.retrieve(platId));

        NUserDTO nUser = new NUserDTO();
        nUser.setBanned(false);
        nUser.setUserId("testuserid");
        nUser.setStatus("enabled");
        List<NPlatformDTO> platforms = new ArrayList<NPlatformDTO>();
        platforms.add(notPlatformRepository.retrieve(platId));
        nUser.setNotPlatformDTOs(platforms);
        //notificationService.registerUser(nUser);
        long result = notUserRepository.persist(nUser);
        assertNotSame(-1L, result);

        NUserDTO nusasdfer = notUserRepository.retrieve(result);

        assertNotNull(nusasdfer);
        //there is a User (testuserid) with one Platform (myAndroidPlatform)
    }

    @Test
    public void testAddPlatformToUser() throws Exception {
        //Locate testuserId
        IntegrationDataFilter idf = new IntegrationDataFilter();
        idf.addLikeCondition("userId","testuserid");
        NUserDTO nuser = notUserRepository.retrieve(idf);
        assertNotNull("testuserid not retrieved successfully with IntegrationDataFilter by userId", nuser);


        NPlatformDTO nPlatform = new NPlatformDTO();
        nPlatform.setPlatformId("theotherplatformid");
        nPlatform.setStatus("enabled");
        nPlatform.setAlias("myOtherAndroidPhone");
        nPlatform.setAppVersion("testApp");
        nPlatform.setToken("the_other_token_that_i_do_not_know_yet");
        nPlatform.setnPlatformTypeDTO(notPlatformTypeRepository.retrieve(2));

        notificationService.addPlatformToUser("testuserid",nPlatform);


        NUserDTO nuser2 = notUserRepository.retrieve(idf);
        assertNotNull("testuserid not retrieved successfully with IntegrationDataFilter by userId after adding platform", nuser);
        assertEquals("User testuserid should have 2 platforms and has ["+nuser2.getNotPlatformDTOs().size()+"]",nuser2.getNotPlatformDTOs().size(),2);

        String theToken = "APA91bHaZB912Fho07RZmdMOm2gVME9AoZXvftXZxdi6lv2fjihC05oF7y-M2s8sS1puKZvOtH4PJst2pMvwDQ9bavFDgU0KTi5HTUXI2G3nqx7QLk2fWEV-3utJ88hdJuAEiCkqbLCQ0XiaFH6XTW_TebI1IhGj3Q";
        assertNotNull(notificationService);
        notificationService.updatePlatform(
                "testuserid",
                "theotherplatformid",
                theToken);

        IntegrationDataFilter idf2 = new IntegrationDataFilter();
        idf2.addLikeCondition("platformId","theotherplatformid");
        NPlatformDTO nPlatformDTO = notPlatformRepository.retrieve(idf2);
        assertNotNull(nPlatformDTO);
        assertEquals(
                "New Token not saved.",
                theToken,
                nPlatformDTO.getToken());

        List<String> platforms = new ArrayList<String>();
        platforms.add("theotherplatformid");
        platforms.add("theplatformid");
        notificationService.sendNotification("testuserId",platforms);


    }

    @Test
    public void testUpdateToken() throws Exception {
    }

    @Test
    public void testNotificationServiceGoogle() throws Exception {
        assertNotNull(notificationService);
        notificationService.outputData();
        /*NUserDTO nuser = notificationService.retrieveUser("testuserid");
        assertNotNull(nuser);
        System.out.println("User ["+nuser.getUserId()+"]");*/
        //boolean result = notificationService.sendNotification("android","thetoken","the_push_message");
    }

}
