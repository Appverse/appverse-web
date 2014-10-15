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

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
		try{
			userDTORetrieved = userRepository.retrieve(filter);
		}catch(PersistenceException e){
			if(!(e.getCause() instanceof NoResultException)){
				throw e;
			}
		}
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
	
	@Test
	public void useCriteriaBuilder() throws Exception {
		persist();
		CriteriaBuilder cb = userRepository.getCriteriaBuilder();		 
		CriteriaQuery<UserDTO> cq = cb.createQuery(UserDTO.class);
		Root<UserDTO> u = cq.from(UserDTO.class);
		cq.select(u);
		TypedQuery<UserDTO> q = userRepository.createQuery(cq);
		List<UserDTO> allUsers = q.getResultList();		
		Assert.notEmpty(allUsers, "allUsers should not be empty or null");
	}
	
	@Test
	public void testSingleResultWithoutResults() throws Exception {
		UserDTO userDTORetrieved = userRepository.retrieve("select u from UserDTO u");
		Assert.isNull(userDTORetrieved);
	}	

    /*  Tests optimistic locking. This can not be uncommented because even though it is possible to assert that
        certain exception is thrown, the transaction fails anyway and the test is flaged as 'failed'.
    @Test
    public void optimistickLocking() throws Exception {

        // We save an user (id = 1)

        UserDTO testDTO = new UserDTO();
        testDTO.setName("name");
        testDTO.setLastName("lastName");
        testDTO.setPassword("password");
        testDTO.setEmail("email");
        userRepository.persist(testDTO);

        // We retrieve the user, the version has to be 1
        UserDTO userDTORetrieved = userRepository.retrieve(1);
        Assert.notNull(userDTORetrieved);
        Assert.isTrue(userDTORetrieved.getVersion() == 1);

        // We simulate that the version has been updated
        userDTORetrieved.setVersion(2);
        userDTORetrieved.setName("Updated name");
        userRepository.persist(userDTORetrieved);
        Assert.isTrue(userDTORetrieved.getVersion() == 2);

        boolean optimistickLockingException = false;
        try{
        // We simultate that the userDTO has been received (a form, for instnace) with old version (1)
        UserDTO staleDTOSimulation = new UserDTO();
        staleDTOSimulation.setId(1);
        staleDTOSimulation.setName("");
        staleDTOSimulation.setLastName("");
        staleDTOSimulation.setPassword("");
        staleDTOSimulation.setEmail("");
        staleDTOSimulation.setActive(true);
        staleDTOSimulation.setCreated(new Date());
        staleDTOSimulation.setCreatedBy("");
        staleDTOSimulation.setStatus("");
        staleDTOSimulation.setVersion(1);
        userRepository.persist(staleDTOSimulation);
        }
        catch (Throwable e){
            optimistickLockingException = true;
            e.printStackTrace();
        }
    }
    */
}
