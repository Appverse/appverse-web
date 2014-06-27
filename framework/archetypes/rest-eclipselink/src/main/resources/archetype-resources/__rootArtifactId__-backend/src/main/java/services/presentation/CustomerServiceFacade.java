#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.presentation;

import ${package}.model.presentation.customer.AccountsDataVO;
import ${package}.model.presentation.customer.CustomerDataVO;
import ${package}.model.presentation.customer.CustomerInfoVO;
import org.appverse.web.framework.backend.api.services.presentation.IPresentationService;

/**
 * Created by MCRZ on 28/02/14.
 */
public interface CustomerServiceFacade extends IPresentationService {

    CustomerDataVO getCustomerInfo() throws Exception;
    CustomerDataVO saveCustomerInfo(CustomerInfoVO customerInfoVO) throws Exception;
    AccountsDataVO globalPosition() throws Exception;


}
