package org.appverse.web.framework.backend.persistence.services.integration;

import org.appverse.web.framework.backend.persistence.model.integration.UserDTO;
import org.appverse.web.framework.backend.persistence.services.integration.IJPAPersistenceService;


public interface UserRepository extends IJPAPersistenceService<UserDTO> {

	UserDTO loadUserByUsername(final String username) throws Exception;

}
