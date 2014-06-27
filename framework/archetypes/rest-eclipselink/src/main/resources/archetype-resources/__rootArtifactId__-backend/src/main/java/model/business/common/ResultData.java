#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.business.common;


import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;

/**
 * @author maps
 * @version 1.0
 * @created 08-may-2012 9:45:23
 */
public class ResultData extends AbstractBusinessBean{



    /**
     * The error returned for this Result Data. Code is 0 if result is returned with success
     */
    private Error error;

    /**
     * Constructor
     */
    public ResultData(){

    }

    /**
     * @return the error
     */
    public Error getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Error error) {
        this.error = error;
    }


}
