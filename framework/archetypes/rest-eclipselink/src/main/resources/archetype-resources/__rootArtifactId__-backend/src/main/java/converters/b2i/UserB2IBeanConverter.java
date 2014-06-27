#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converters.b2i;

import ${package}.model.business.customer.User;
import ${package}.model.integration.jpa.UserDTO;
import org.appverse.web.framework.backend.api.converters.AbstractDozerB2IBeanConverter;
import org.springframework.stereotype.Component;

/**
 * Created by MCRZ on 10/03/14.
 */
@Component("userB2IBeanConverter")
public class UserB2IBeanConverter extends
        AbstractDozerB2IBeanConverter<User,UserDTO> {

    public UserB2IBeanConverter() {
        setScopes("user-b2i-complete", "user-b2i-without-dependencies");
        setBeanClasses(User.class,UserDTO.class);
    }
}
