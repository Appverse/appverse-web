#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converters.p2b;

import ${package}.model.business.customer.User;
import ${package}.model.presentation.common.UserVO;
import org.appverse.web.framework.backend.api.converters.AbstractDozerP2BBeanConverter;
import org.springframework.stereotype.Component;

/**
 * Created by MCRZ on 10/03/14.
 */
@Component("userP2BBeanConverter")
public class UserP2BBeanConverter extends
        AbstractDozerP2BBeanConverter<UserVO,User> {

    public UserP2BBeanConverter() {
        setScopes("user-p2b");
        setBeanClasses(UserVO.class,User.class);
    }
}
