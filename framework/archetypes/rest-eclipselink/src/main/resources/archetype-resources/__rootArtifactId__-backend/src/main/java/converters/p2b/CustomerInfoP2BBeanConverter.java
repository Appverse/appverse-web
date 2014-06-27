#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converters.p2b;

import ${package}.model.business.customer.User;
import ${package}.model.presentation.customer.CustomerInfoVO;
import org.appverse.web.framework.backend.api.converters.AbstractDozerP2BBeanConverter;
import org.springframework.stereotype.Component;

/**
 * Created by MCRZ on 20/03/2014.
 */
@Component("customerInfoP2BBeanConverter")
public class CustomerInfoP2BBeanConverter  extends
        AbstractDozerP2BBeanConverter<CustomerInfoVO,User> {
    public CustomerInfoP2BBeanConverter() {
        setScopes("customerInfo-p2b");
        setBeanClasses(CustomerInfoVO.class,User.class);
    }
}
