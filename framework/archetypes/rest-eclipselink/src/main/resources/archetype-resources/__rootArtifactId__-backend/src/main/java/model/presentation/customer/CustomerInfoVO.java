#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import ${package}.model.presentation.common.UserVO;

import javax.validation.constraints.Pattern;

/**
 * Created by MCRZ on 20/03/2014.
 */
@ApiModel(value="CustomerInfoVO Model")
public class CustomerInfoVO extends UserVO{


    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("idCard")
    @Pattern(message="valid idCard please" , regexp="^[0-9]{8}([a-zA-Z]{1})${symbol_dollar}")
    private String idCard;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("mobileNumber")
    @Pattern(message="valid phone number please" , regexp="^[+]?[0-9]*${symbol_dollar}")
    private String mobileNumber;

    @ApiModelProperty("phoneNumber")
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
