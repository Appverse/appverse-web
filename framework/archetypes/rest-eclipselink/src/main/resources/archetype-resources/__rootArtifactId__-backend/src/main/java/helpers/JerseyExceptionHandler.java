#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.helpers;

import ${package}.model.presentation.common.ResultDataVO;
import org.appverse.web.framework.backend.api.common.AbstractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Generates a mapping between presentationException and errorCodes,
 *
 * uses the exception code and uses it to identify description code

 */
@Provider
@Repository
public class JerseyExceptionHandler implements ExceptionMapper<Exception>
{
    @Autowired
    ErrorCodes errorCodes;

    /**
     *
     * @param exception the exception to be translated
     * @return a Response object containing the status and entity
     */
    @Override
    public Response toResponse(Exception exception)
    {
        ResultDataVO data = new ResultDataVO();
        Long code = null;
        if (exception instanceof AbstractException) {
            code = ((AbstractException) exception).getCode();
        }
        if (code == null ) {
            code = ErrorCodes.ERROR_CODE_unhandled;
        }
        data.setError(errorCodes.getErrorVO(code));
        data.getError().setMessage(data.getError().getMessage()+":"+exception.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(data).build();
    }

}
