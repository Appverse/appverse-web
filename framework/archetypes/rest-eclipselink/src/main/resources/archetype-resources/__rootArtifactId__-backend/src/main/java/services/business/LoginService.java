#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.business;

import ${package}.model.business.customer.User;
import org.springframework.security.core.Authentication;


public interface LoginService {

    public static final String DEFAULT_ANONYMOUS_USER = "anonymous";

    String getLoggedUser();

    User authenticate(Authentication authenticationToken) throws Exception;

    void logout() throws Exception;
}
