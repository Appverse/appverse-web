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
package org.appverse.web.framework.backend.persistence.services.integration.impl.test;

import java.util.List;

import org.appverse.web.framework.backend.api.helpers.test.AbstractTransactionalTest;
import org.appverse.web.framework.backend.api.helpers.test.JPATest;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.persistence.model.integration.UserDTO;
import org.appverse.web.framework.backend.persistence.services.integration.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

@TransactionConfiguration(defaultRollback = false)
public class UserRepositoryImplTest extends AbstractTransactionalTest implements
		JPATest {

	private static UserDTO userDTO;

	@Autowired
	UserRepository userRepository;

	@Override
	public void delete() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		userRepository.persist(userDTO);
		userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.notNull(userDTORetrieved);
		userRepository.delete(userDTO);
		userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
	}

	@Override
	@Test
	public void deleteAll() throws Exception {
		retrieveAll();
		List<UserDTO> list = userRepository.retrieveList();
		Assert.notEmpty(list);
		userRepository.deleteAll();
		list = userRepository.retrieveList();
		Assert.isTrue(list.isEmpty());
	}

	@Override
	@After
	public void finalize() throws Exception {
		userRepository.deleteAll();
	}

	@Override
	@Before
	public void initialize() throws Exception {
		userDTO = new UserDTO();
		userDTO.setName("name");
		userDTO.setLastName("lastName");
		userDTO.setPassword("password");
		userDTO.setEmail("email");

	}

	@Override
	@Test
	public void persist() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		userRepository.persist(userDTO);
		userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.notNull(userDTORetrieved);

	}

	@Override
	@Test
	public void retrieveAll() throws Exception {
		List<UserDTO> list = userRepository.retrieveList();
		Assert.isTrue(list.isEmpty());
		persist();
		list = userRepository.retrieveList();
		Assert.notEmpty(list);
	}

	@Override
	@Test
	public void retrieveByBean() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		persist();
		userDTORetrieved = userRepository.retrieve(new UserDTO());
		Assert.isNull(userDTORetrieved);
		userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.notNull(userDTORetrieved);

	}

	@Override
	@Test
	public void retrieveByFilter() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		persist();
		IntegrationDataFilter filter = new IntegrationDataFilter();
		filter.addStrictCondition("name", "name1");
		userDTORetrieved = userRepository.retrieve(filter);
		Assert.isNull(userDTORetrieved);
		filter = new IntegrationDataFilter();
		filter.addStrictCondition("name", "name");
		userDTORetrieved = userRepository.retrieve(filter);
		Assert.notNull(userDTORetrieved);

	}

	@Override
	@Test
	public void retrieveByPk() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		persist();
		userDTORetrieved = userRepository.retrieve(0);
		Assert.isNull(userDTORetrieved);
		userDTORetrieved = userRepository.retrieve(userDTO.getId());
		Assert.notNull(userDTORetrieved);
	}
}
