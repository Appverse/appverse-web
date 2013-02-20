package org.appverse.web.framework.backend.persistence.services.integration.impl.test;

import org.appverse.web.framework.backend.api.helpers.test.AbstractTransactionalTest;
import org.appverse.web.framework.backend.api.helpers.test.JPATest;
import org.appverse.web.framework.backend.persistence.model.integration.UserDTO;
import org.appverse.web.framework.backend.persistence.services.integration.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

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
