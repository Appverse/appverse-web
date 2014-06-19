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
package org.appverse.web.framework.backend.bpm.services.integration.impl.test;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.bpm.services.integration.IBpmService;
import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
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

import java.util.List;

import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = { "classpath:/spring/application-config.xml" })
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@TestExecutionListeners({BpmServiceTest.WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BpmServiceTest {
    
    @AutowiredLogger
    private static Logger logger;

    @Autowired
    IBpmService bpmService;

    private String userName = "william.jobs";
    private String password = "bpm";


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
    @Test(expected = IllegalStateException.class)
    public void testLoginException() throws Exception{
        bpmService.executeTaskFlowNode(1L);

    }

    @Test
    @Ignore //Tests won't work if bonita is not started.
    public void testGetProcessAPI() throws Exception {
        assertNotNull(bpmService);
        bpmService.login(userName, password);
        try {
            long processInstanceId = bpmService.createCase("Onboarding", "1.0", null);

            List<ActivityInstance> activityInstances = null;
            do {
                Thread.sleep(1000);
                activityInstances = bpmService.getActivityInstancesByProcesInstance(processInstanceId);
                if (activityInstances != null && activityInstances.size() > 0) {
                    for (ActivityInstance activityInstance : activityInstances) {
                        List<DataDefinition> data = bpmService.getDataDefinitionsByActivity("Onboarding", "1.0", activityInstance.getName(), 0, 10);
                        if (data.size() != 0) {
                            for (DataDefinition field : data) {
                                logger.info("data: '{}' type:'{}'", field.getName(), field.getClassName());
                            }

                        } else {
                            logger.info("data: no data.");
                        }

                        bpmService.assignTaskToCurrentUser(activityInstance.getId());
                        bpmService.executeTaskFlowNode(activityInstance.getId());
                    }
                }
            } while (activityInstances != null && activityInstances.size() > 0);
        }finally {
            bpmService.logout();
        }
    }
    @Test
    public void testGetProcessAPIOnMultipleSessions() throws Exception {
        assertNotNull(bpmService);
        bpmService.login(userName, password);
        try {
            //long processInstanceId = bpmService.createCase("Onboarding","1.0", null);
            long processDefinitionId = bpmService.getProcessDefinitionIdByNameVersion("Onboarding", "1.0");
            List<HumanTaskInstance> list = bpmService.obtainCurrentFlows(processDefinitionId, 0, Integer.MAX_VALUE);
            if (list.size() == 0) {
                logger.info("no pending process found");
            }
            for (HumanTaskInstance element : list) {
                logger.info("obtain old process:{}", element.getName());
                long processInstanceId = element.getFlownodeDefinitionId();

                //bpmService.test("Onboarding","1.0","", 0, 100);

                //this piece of code assigns all possible tasks to the current user.
                List<ActivityInstance> activityInstances = null;
                do {
                    Thread.sleep(1000);
                    activityInstances = bpmService.getActivityInstancesByProcesInstance(processInstanceId);
                    if (activityInstances != null && activityInstances.size() > 0) {
                        for (ActivityInstance activityInstance : activityInstances) {
                            List<DataDefinition> data = bpmService.getDataDefinitionsByActivity("Onboarding", "1.0", activityInstance.getName(), 0, 10);
                            if (data.size() != 0) {
                                for (DataDefinition field : data) {
                                    logger.info("data: '{}' type:'{}'", field.getName(), field.getClassName());

                                }

                            } else {
                                logger.info("data: no data.");
                            }

                            bpmService.assignTaskToCurrentUser(activityInstance.getId());
                            bpmService.executeTaskFlowNode(activityInstance.getId());
                        }
                    }
                } while (activityInstances != null && activityInstances.size() > 0);

            }
        }finally {
            bpmService.logout();
        }
    }

}
