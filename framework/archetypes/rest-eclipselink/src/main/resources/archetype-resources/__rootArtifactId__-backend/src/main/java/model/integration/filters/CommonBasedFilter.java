#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.integration.filters;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;

import java.util.Date;

/**
 * Created by MCRZ on 13/03/14.
 */
public class CommonBasedFilter extends AbstractIntegrationBean {

    private Date fromDate;

    private Date toDate;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
