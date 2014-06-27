#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

/**
 * Created by MCRZ on 3/03/14.
 */


import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author maps
 */
@ApiModel(value = "UserDataVO model")
public class UserDataVO extends ResultDataVO {

    @ApiModelProperty(value = "result")
    private UserVO result;

    // optional value, only filled in case of small business customers in certain countries
    //private SmallBusinessParticipant[] smallBusinessParticipants;

    public UserDataVO() {
    }

    /**
     * @return the result
     */
    public UserVO getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(UserVO result) {
        this.result = result;
    }

    /**
     * @return the smallBusinessParticipants
     */
    /*public SmallBusinessParticipant[] getSmallBusinessParticipants() {
        return smallBusinessParticipants;
    }*/

    /**
     * @param smallBusinessParticipants the smallBusinessParticipants to set
     */
    /*public void setSmallBusinessParticipants(SmallBusinessParticipant[] smallBusinessParticipants) {
        this.smallBusinessParticipants = smallBusinessParticipants;
    }*/


}

