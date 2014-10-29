#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

public class FamilyVO {
    
    private String code;
    private String description;
    private double aggregatedBalance;
    private String aggregatedBalanceCurrency;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the aggregatedBalance
     */
    public double getAggregatedBalance() {
        return aggregatedBalance;
    }

    /**
     * @param aggregatedBalance the aggregatedBalance to set
     */
    public void setAggregatedBalance(double aggregatedBalance) {
        this.aggregatedBalance = aggregatedBalance;
    }

    /**
     * @return the aggregatedBalanceCurrency
     */
    public String getAggregatedBalanceCurrency() {
        return aggregatedBalanceCurrency;
    }

    /**
     * @param aggregatedBalanceCurrency the aggregatedBalanceCurrency to set
     */
    public void setAggregatedBalanceCurrency(String aggregatedBalanceCurrency) {
        this.aggregatedBalanceCurrency = aggregatedBalanceCurrency;
    }

}
