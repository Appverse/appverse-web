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
package org.appverse.web.showcases.gwtshowcase.backend.services.business.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.api.converters.ConversionType;
import org.appverse.web.framework.backend.api.converters.b2i.PaginatedDataFilterB2IBeanConverter;
import org.appverse.web.framework.backend.api.model.business.BusinessPaginatedDataFilter;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.showcases.gwtshowcase.backend.converters.b2i.UserB2IBeanConverter;
import org.appverse.web.showcases.gwtshowcase.backend.model.business.User;
import org.appverse.web.showcases.gwtshowcase.backend.model.integration.UserDTO;
import org.appverse.web.showcases.gwtshowcase.backend.services.business.UserService;
import org.appverse.web.showcases.gwtshowcase.backend.services.integration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl extends AbstractBusinessService implements
		UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserB2IBeanConverter userB2IBeanConverter;

	@Autowired
	private PaginatedDataFilterB2IBeanConverter paginatedDataFilterB2IBeanConverter;

	@Override
	public int countUsers(final BusinessPaginatedDataFilter filter)
			throws Exception {
		final IntegrationPaginatedDataFilter integrationDataFilter = paginatedDataFilterB2IBeanConverter
				.convert(filter);
		return userRepository.count(integrationDataFilter);
	}

	@Override
	public User loadUser(final long pk) throws Exception {
		// Note that default conversion type (not specified) is
		// ConversionType.Complete. This will convert all collections
		// using the proper corresponding mapping
		final UserDTO userDTO = userRepository.retrieve(pk);
		final User user = userB2IBeanConverter
				.convert(userDTO);
		return user;
	}
	
	@Override
	public void deleteUser(final User user) throws Exception {
		final UserDTO userDTO = userRepository.retrieve(user.getId());
		userRepository.delete(userDTO);
	}	
	
	@Override
	public List<User> loadUsers()
			throws Exception {
		// Note that ConversitonType.WithoutDependencies will not convert
		// collections using the corresponding mapping
		List<UserDTO> userList = userRepository.retrieveList();
		return userB2IBeanConverter.convertIntegrationList(userList);
	}

	@Override
	public List<User> loadUsers(
			final BusinessPaginatedDataFilter config) throws Exception {
		final IntegrationPaginatedDataFilter integrationDataFilter = paginatedDataFilterB2IBeanConverter
				.convert(config);

		final List<UserDTO> userList = userRepository
				.retrieveList(integrationDataFilter);

		return userB2IBeanConverter.convertIntegrationList(userList);
	}

	@Override
	public long saveUser(
			final User user)
			throws Exception {
		UserDTO userDTO;

		if (user.getId() != 0L) {
			// As it is an existing user we retrieve the entity manager managed
			// object
			userDTO = userRepository.retrieve(user.getId());
			userB2IBeanConverter.convert(user, userDTO,
					ConversionType.WithoutDependencies);
		} else {
			// We are creating a new DTO (not managed by the entity manager yet)
			userDTO = userB2IBeanConverter.convert(user);
		}
		return userRepository.persist(userDTO);
	}
}