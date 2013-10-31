/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.gwtshowcase.backend.services.presentation;

import org.appverse.web.framework.backend.api.services.presentation.IPresentationService;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.POST;

/**
 * This UserServiceFacade, is the interface for our presentation service.
 * As it is not RPC, it does not have the RemoteServiceRelativePath annotation, RestyGWT works slightly different.
 * See the UserRestServiceFacade interface below.
 */
//@RemoteServiceRelativePath("services/userServiceFacade.rpc")
public interface UserServiceFacade extends IPresentationService {

	//List<UserVO> loadUsers() throws Exception;
	
    UserVO loadUser(long userId) throws Exception;

	long saveUser(UserVO userVO) throws Exception;

    void deleteUser(UserVO userVO) throws Exception;

	GWTPresentationPaginatedResult<UserVO> loadUsers(
			GWTPresentationPaginatedDataFilter config) throws Exception;


    /**
     * This is the RestyGWT interface for our Presentation Service.
     * The fact that it must be with a MethodCallback parameter, make it a bit difficult to cleanly integrate.
     * It must extend RestService.
     * The Client class is a candidate to become part of AbstractRestCommandImpl, so part of Appverse Framework.
     */
    interface UserRestServiceFacade extends RestService {
        @POST
        void loadUser(Long userId, MethodCallback<UserVO> callback );

        @POST
        void deleteUser(UserVO userVo, MethodCallback<Void> callback );

        @POST
        void loadUsers(GWTPresentationPaginatedDataFilter config, MethodCallback<GWTPresentationPaginatedResult<UserVO>> callback);

        @POST
        void saveUser(UserVO userVo, MethodCallback<Long> callback);
    }




}
