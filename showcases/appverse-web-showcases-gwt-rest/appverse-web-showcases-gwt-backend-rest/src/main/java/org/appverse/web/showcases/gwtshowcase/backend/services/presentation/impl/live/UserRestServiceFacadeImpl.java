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
package org.appverse.web.showcases.gwtshowcase.backend.services.presentation.impl.live;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.appverse.web.framework.backend.api.converters.p2b.PaginatedDataFilterP2BBeanConverter;
import org.appverse.web.framework.backend.api.model.business.BusinessPaginatedDataFilter;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.appverse.web.framework.backend.frontfacade.gxt.converters.p2b.GWTPaginatedDataFilterP2BBeanConverter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.showcases.gwtshowcase.backend.converters.p2b.UserP2BBeanConverter;
import org.appverse.web.showcases.gwtshowcase.backend.model.business.User;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.backend.services.business.UserService;
import org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserRestServiceFacade;
import org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade;
import org.fusesource.restygwt.client.MethodCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userRestServiceFacade")
@Path("hello")
public class UserRestServiceFacadeImpl extends AbstractPresentationService {

	@Autowired
	private UserService userService;
	@Autowired
	private UserP2BBeanConverter userP2BBeanConverter;
	@Autowired
	private GWTPaginatedDataFilterP2BBeanConverter gwtPaginatedDataFilterP2BBeanConverter;

	@Autowired
	private PaginatedDataFilterP2BBeanConverter paginatedDataFilterP2BBeanConverter;

	public UserRestServiceFacadeImpl() {
	}
	
	  @GET
	  @Produces(MediaType.TEXT_HTML)
	  public String sayHtmlHello() {
	    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
	        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	  }

//	@Override
//	public UserVO loadUser(final long userId) throws Exception {
//		final User user = userService.loadUser(userId);
//		return userP2BBeanConverter.convert(user);
//	}
//
//	@Override
//	public List<UserVO> loadUsers() throws Exception {
//		final List<User> users = userService.loadUsers();
//
//		final List<UserVO> usersVO = userP2BBeanConverter
//				.convertBusinessList(users);
//		return usersVO;
//	}
//
	public GWTPresentationPaginatedResult<UserVO> loadUsers(
			final GWTPresentationPaginatedDataFilter config) throws Exception {

		final BusinessPaginatedDataFilter businessDatafilter = paginatedDataFilterP2BBeanConverter
				.convert(config);

		// Get the total number of rows first
		final int total = userService.countUsers(businessDatafilter);

		// Get the rows
		final List<User> users = userService.loadUsers(businessDatafilter);

		final List<UserVO> usersVO = userP2BBeanConverter
				.convertBusinessList(users);

		return new GWTPresentationPaginatedResult<UserVO>(usersVO, total,
				config.getOffset());
	}
//
//	@Override
//	public long saveUser(final UserVO userVO) throws Exception {
//		final User user = userP2BBeanConverter.convert(userVO);
//		return userService.saveUser(user);
//	}
//	
	public void deleteUser(final UserVO userVO) throws Exception {
		final User user = userP2BBeanConverter.convert(userVO);
		System.out.println("UserRestServiceFacadeImpl, delete user ["+userVO.getName()+"]");
		if( true ) {
			throw new Exception("I WANNA SEE THIS MESSAGE ON THE CLIENT.");
		}
		userService.deleteUser(user);
	}

//	public List<UserVO> loadUsers() throws Exception {
//		
//		final List<User> users = userService.loadUsers();
//
//		final List<UserVO> usersVO = userP2BBeanConverter
//				.convertBusinessList(users);
////		callback.onSuccess(method, response)
//		return usersVO;
//
//	}
	
	public UserVO loadUser(long userId) throws Exception {
		final User user = userService.loadUser(userId);
		UserVO uservo = userP2BBeanConverter
				.convert(user);
		return uservo;
		
	}
}
