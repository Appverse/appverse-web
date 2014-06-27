#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.business.common;


import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;

/**
 *
 * @author maps
 */

public class PaginationRequestData extends AbstractBusinessBean {


    private long fromDate;

    private long toDate;

    private Pagination goTo;

    /**
     * Constructor
     */
    public PaginationRequestData() {
    }

    /**
     * @return the fromDate
     */
    public long getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public long getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the goTo
     */
    public Pagination getGoTo() {
        return goTo;
    }

    /**
     * @param goTo the goTo to set
     */
    public void setGoTo(Pagination goTo) {
        this.goTo = goTo;
    }

            
        
}
