#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converters.p2b;

import ${package}.model.business.customer.AccountsData;
import ${package}.model.presentation.customer.AccountsDataVO;
import org.appverse.web.framework.backend.api.converters.AbstractDozerP2BBeanConverter;
import org.springframework.stereotype.Component;

/**
 * Created by MCRZ on 10/03/14.
 */
@Component("accountsP2BBeanConverter")
public class AccountsP2BBeanConverter extends
        AbstractDozerP2BBeanConverter<AccountsDataVO,AccountsData> {

    public AccountsP2BBeanConverter() {
        setScopes("accounts-p2b");
        setBeanClasses(AccountsDataVO.class,AccountsData.class);
    }
}
