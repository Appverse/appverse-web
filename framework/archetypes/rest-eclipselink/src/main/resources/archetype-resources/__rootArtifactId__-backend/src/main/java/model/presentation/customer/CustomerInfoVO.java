#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import ${package}.model.presentation.common.UserVO;

import javax.validation.constraints.Pattern;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value="CustomerInfoVO Model")
#end
public class CustomerInfoVO extends UserVO{

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty("email")
    #end
    private String email;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty("idCard")
    #end
    @Pattern(message="valid idCard please" , regexp="^[0-9]{8}([a-zA-Z]{1})${symbol_dollar}")
    private String idCard;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty("address")
    #end
    private String address;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty("mobileNumber")
    #end
    @Pattern(message="valid phone number please" , regexp="^[+]?[0-9]*${symbol_dollar}")
    private String mobileNumber;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty("phoneNumber")
    #end
    @Pattern(message="valid phone number please" , regexp="^[+]?[0-9]*${symbol_dollar}")
    private String phoneNumber;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
