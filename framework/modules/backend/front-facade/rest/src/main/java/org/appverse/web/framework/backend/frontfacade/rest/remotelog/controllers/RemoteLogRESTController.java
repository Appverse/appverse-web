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
