#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converters.b2i;

import ${package}.model.business.customer.Account;
import ${package}.model.integration.jpa.AccountDTO;
import org.appverse.web.framework.backend.api.converters.AbstractDozerB2IBeanConverter;
import org.springframework.stereotype.Component;

/**
 * Created by MCRZ on 10/03/14.
 */
@Component("accountB2IBeanConverter")
public class AccountB2IBeanConverter extends
        AbstractDozerB2IBeanConverter<Account,AccountDTO> {

    public AccountB2IBeanConverter() {
        setScopes("account-b2i-complete", "account-b2i-without-dependencies");
        setBeanClasses(Account.class,AccountDTO.class);
    }
}
