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
import org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceFacade")
public class UserServiceFacadeImpl extends AbstractPresentationService
		implements UserServiceFacade {

	@Autowired
	private UserService userService;
	@Autowired
	private UserP2BBeanConverter userP2BBeanConverter;
	@Autowired
	private GWTPaginatedDataFilterP2BBeanConverter gwtPaginatedDataFilterP2BBeanConverter;

	@Autowired
	private PaginatedDataFilterP2BBeanConverter paginatedDataFilterP2BBeanConverter;

	public UserServiceFacadeImpl() {
	}

	@Override
	public UserVO loadUser(final long userId) throws Exception {
		final User user = userService.loadUser(userId);
		return userP2BBeanConverter.convert(user);
	}

//	@Override
//	public List<UserVO> loadUsers() throws Exception {
//		final List<User> users = userService.loadUsers();
//
//		final List<UserVO> usersVO = userP2BBeanConverter
//				.convertBusinessList(users);
//		return usersVO;
//	}

	@Override
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

	@Override
	public long saveUser(final UserVO userVO) throws Exception {
		final User user = userP2BBeanConverter.convert(userVO);
		return userService.saveUser(user);
	}
	
	@Override
	public void deleteUser(final UserVO userVO) throws Exception {
		final User user = userP2BBeanConverter.convert(userVO);
		userService.deleteUser(user);
	}	
}
