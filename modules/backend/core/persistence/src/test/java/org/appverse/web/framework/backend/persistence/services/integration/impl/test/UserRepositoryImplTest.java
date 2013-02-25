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

import org.appverse.web.framework.backend.api.helpers.test.AbstractTransactionalTest;
import org.appverse.web.framework.backend.api.helpers.test.JPATest;
import org.appverse.web.framework.backend.persistence.model.integration.UserDTO;
import org.appverse.web.framework.backend.persistence.services.integration.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

@TransactionConfiguration(defaultRollback = false)
public class UserRepositoryImplTest extends AbstractTransactionalTest implements JPATest{


	@Autowired
	UserRepository userRepository;

	@Override
	public void delete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	@Test
	public void persist() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("name");
		userDTO.setLastName("lastName");
		userDTO.setPassword("password");
		userDTO.setEmail("email");
		UserDTO userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.isNull(userDTORetrieved);
		userRepository.persist(userDTO);
		userDTORetrieved = userRepository.retrieve(userDTO);
		Assert.notNull(userDTORetrieved);

	}

	@Override
	public void retrieveAll() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void retrieveByBean() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void retrieveByFilter() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void retrieveByPk() throws Exception {
		// TODO Auto-generated method stub

	}

}
