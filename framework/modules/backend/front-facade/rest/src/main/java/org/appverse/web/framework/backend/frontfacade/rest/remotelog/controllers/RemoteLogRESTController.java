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
package org.appverse.web.framework.backend.frontfacade.rest.remotelog.controllers;

import org.appverse.web.framework.backend.api.model.presentation.RemoteLogRequestVO;
import org.appverse.web.framework.backend.api.model.presentation.RemoteLogResponseVO;
import org.appverse.web.framework.backend.api.services.presentation.RemoteLogServiceFacade;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Singleton
@Path("/remotelog")
/**
 * This JAX-RS (Jersey) controller exposes a Remote Log REST service.
 *
 * Remember to register your controller using Jersey2 application in your project and JacksonFeature
 * Example:
 * register(JacksonFeature.class);
 * register(RemoteLogRESTController.class);
 */
public class RemoteLogRESTController implements ApplicationContextAware {

    /**
     * With Jersey 2.x applicationContext is not Autowired (see https://java.net/jira/browse/JERSEY-2169)
     * It must implement ApplicationContextAware to get the ApplicationContext instance
     */
    private ApplicationContext applicationContext;

    @Autowired
    RemoteLogServiceFacade remoteLogServiceFacade;

    /**
     * Writes
     * @param remoteLogRequestVO
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("log")
    public Response writeLog(RemoteLogRequestVO remoteLogRequestVO) {
        RemoteLogResponseVO remoteLogResponseVO = null;
        try{
            remoteLogResponseVO = remoteLogServiceFacade.writeLog(remoteLogRequestVO);
            if (!remoteLogResponseVO.getStatus().equals(RemoteLogResponseVO.OK)){
                // Error related to client call
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        catch(Exception e){
            // Server error
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
