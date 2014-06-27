#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.integration;

import ${package}.model.integration.jpa.AccountDTO;
import org.appverse.web.framework.backend.persistence.services.integration.IJPAPersistenceService;

import java.util.List;

/**
 * Created by MCRZ on 6/03/14.
 */
public interface AccountRepository extends IJPAPersistenceService<AccountDTO> {

    static final String CREDIT_CARDS_CODE = "CREDIT_CARDS";

    List<AccountDTO> loadAccountsByUsername(String username) throws Exception;
    List<AccountDTO> loadAccountsByOperationAndTypeAndUsername(String accountType,String username) throws Exception;
    AccountDTO loadAccountsByAccountNumber(String accountNumber) throws Exception;
}
