#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.integration;

import ${package}.model.integration.jpa.AccountTypeDTO;
import org.appverse.web.framework.backend.persistence.services.integration.IJPAPersistenceService;

/**
 * Created by MCRZ on 6/03/14.
 */
public interface AccountTypeRepository extends IJPAPersistenceService<AccountTypeDTO> {

}
