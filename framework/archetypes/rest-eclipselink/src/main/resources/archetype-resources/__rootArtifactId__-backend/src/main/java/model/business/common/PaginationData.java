#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.business.common;


/**
 *
 * @author maps
 */

public class PaginationData extends ResultData {

    /**
     * Indicates how many results are pending to be returned on next calls.
     */
    private boolean remainingResults;

    /**
     * Constructor
     */
    public PaginationData() {
    }

    /**
     * @return the remainingResults
     */
    public boolean getRemainingResults() {
        return remainingResults;
    }

    /**
     * @param remainingResults the remainingResults to set
     */
    public void setRemainingResults(boolean remainingResults) {
        this.remainingResults = remainingResults;
    }
    
    
}
