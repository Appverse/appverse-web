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
package org.appverse.web.framework.backend.api.services.presentation;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.TextCallback;


/**
 * This is the interface that RestyGWT, and only RestyGWT, will use to access the AuthenticationService.
 * The annotations in this interface are required by RestyGWT.
 * The annotations needed by Jersey to publish the service are in the corresponding service implementation.
 * @see org.appverse.web.framework.backend.api.services.presentation.impl.live.AuthenticationServiceFacadeImpl
 * @author RRBL
 */

@Path("authenticationServiceFacade")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthenticationRestServiceFacade extends RestService {

//	@POST
//	public void authenticatePrincipal(String username, List<String> authorities);

    @POST
    @Path("authenticatePrincipal")
	public void  authenticatePrincipal(UserInfoVO userInfo, MethodCallback<AuthorizationDataVO> callback);

	@POST
    @Path("getAuthorities")
	public void getAuthorities(MethodCallback<List<String>> callback);

	//TextCallback are needed when method return type is just String. See https://github.com/resty-gwt/resty-gwt/issues/63
	@POST
    @Path("getPrincipal")
	public void getPrincipal(TextCallback callback);

	@POST
    @Path("getXSRFSessionToken")
	public void getXSRFSessionToken(TextCallback callback);

	@POST
    @Path("isPrincipalAuthenticated")
	public void isPrincipalAuthenticated(MethodCallback<Boolean> callback);
}
