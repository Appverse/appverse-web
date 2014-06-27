#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.helpers;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * JSON Authentication Failure Handler
 *
 *
 */
@Component( "restAuthenticationEntryPoint" )
public class JSONAuthenticationEntryPoint
        implements AuthenticationEntryPoint {


        @Override
        public void commence( HttpServletRequest request, HttpServletResponse response,
                              AuthenticationException authException ) throws IOException{
            String message = "{${symbol_escape}"error${symbol_escape}":{${symbol_escape}"code${symbol_escape}":-1,${symbol_escape}"message${symbol_escape}":${symbol_escape}""+authException.getMessage()+"${symbol_escape}"}}";
            response.addHeader("Content-Type", MediaType.APPLICATION_JSON);
            response.addHeader("Content-Length",""+message.length());
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().print(message);
            response.getWriter().flush();
        }

}
