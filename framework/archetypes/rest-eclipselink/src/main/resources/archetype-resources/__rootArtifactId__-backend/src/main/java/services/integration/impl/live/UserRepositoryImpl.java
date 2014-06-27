#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.services.integration.impl.live;

import ${package}.model.integration.jpa.UserDTO;
import ${package}.services.integration.UserRepository;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.impl.live.JPAPersistenceService;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository("userRepository")
public class UserRepositoryImpl extends JPAPersistenceService<UserDTO>
		implements UserRepository {

    @AutowiredLogger
    private Logger logger;

	@Override
	public UserDTO loadUserByUsername(final String username) throws Exception {
		final StringBuilder queryString = new StringBuilder();
        if (!StringUtils.isEmpty(username)){
            queryString.append("select user from UserDTO user where user.username='")
                    .append(username).append("'");
            final QueryJpaCallback<UserDTO> query = new QueryJpaCallback<UserDTO>(
                    queryString.toString(), false);
            List<UserDTO> list = retrieveList(query);

            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
	}
    @Override
    public List<UserDTO> findUserByPattern(final String pattern) throws Exception {
        final StringBuilder queryString = new StringBuilder();
        if (!StringUtils.isEmpty(pattern)){
            queryString.append("select user from UserDTO user where user.username like '%")
                    .append(pattern).append("%'");
            final QueryJpaCallback<UserDTO> query = new QueryJpaCallback<UserDTO>(
                    queryString.toString(), false);
            List<UserDTO> list = retrieveList(query);

            if (list != null && list.size() > 0) {
                return list;
            }
        }
        return null;
    }
}
