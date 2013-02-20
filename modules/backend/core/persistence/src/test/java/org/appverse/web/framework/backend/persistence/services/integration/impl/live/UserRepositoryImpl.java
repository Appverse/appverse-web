package org.appverse.web.framework.backend.persistence.services.integration.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.persistence.model.integration.UserDTO;
import org.appverse.web.framework.backend.persistence.services.integration.UserRepository;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.impl.live.JPAPersistenceService;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public class UserRepositoryImpl extends JPAPersistenceService<UserDTO>
		implements UserRepository {

	@AutowiredLogger
	private static Logger logger;

	@Override
	public UserDTO loadUserByUsername(final String username) throws Exception {
		final StringBuilder queryString = new StringBuilder();
		queryString.append("select user from UserDTO user where user.email='")
				.append(username).append("'");
		final QueryJpaCallback<UserDTO> query = new QueryJpaCallback<UserDTO>(
				queryString.toString(), false);
		List<UserDTO> list = execute(query);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
