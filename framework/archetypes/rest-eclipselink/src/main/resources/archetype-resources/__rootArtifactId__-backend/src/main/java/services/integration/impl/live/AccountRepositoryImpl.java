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

import ${package}.model.integration.jpa.AccountDTO;
import ${package}.services.integration.AccountRepository;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.impl.live.JPAPersistenceService;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository("accountRepository")
public class AccountRepositoryImpl extends JPAPersistenceService<AccountDTO>
		implements AccountRepository {

    public static final String ALL_FILTER="ALL";

    @AutowiredLogger
    private Logger logger;

	@Override
	public List<AccountDTO> loadAccountsByUsername(final String username) throws Exception {
		final StringBuilder queryString = new StringBuilder();
		queryString.append("select account from AccountDTO account where account.accountUser.owner.username='")
				.append(username).append("'");
		final QueryJpaCallback<AccountDTO> query = new QueryJpaCallback<AccountDTO>(
				queryString.toString(), false);
		return retrieveList(query);

	}
    @Override
    public List<AccountDTO> loadAccountsByOperationAndTypeAndUsername(String accountType,String username) throws Exception {
        final StringBuilder queryString = new StringBuilder();
        queryString.append("select account from AccountDTO account where account.accountUser.owner.username='")
                .append(username).append("'");
        if (!StringUtils.isEmpty(accountType) && !ALL_FILTER.equalsIgnoreCase(accountType)){
            queryString.append(" and account.accountType.code='"+accountType+"'");
        }
        final QueryJpaCallback<AccountDTO> query = new QueryJpaCallback<AccountDTO>(
                queryString.toString(), false);
        return retrieveList(query);

    }

    @Override
    public AccountDTO loadAccountsByAccountNumber(String accountNumber) throws Exception {
        AccountDTO account = null;
        final StringBuilder queryString = new StringBuilder();
        queryString.append("select account from AccountDTO account where account.accountNumber='")
                .append(accountNumber).append("'");

        final QueryJpaCallback<AccountDTO> query = new QueryJpaCallback<AccountDTO>(
                queryString.toString(), false);
        List<AccountDTO> accounts = retrieveList(query);
        if (accounts.size()>0){
            account = retrieveList(query).get(0);
        }
        return account;
    }
}
