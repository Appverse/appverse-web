#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

/**
 * Created by MCRZ on 3/03/14.
 */


import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 * @author maps
 */
@ApiModel(value = "UserDataVO model")
public class UsersDataVO extends ResultDataVO {

    @ApiModelProperty(value = "result")
    private List<UserVO> result;

    // optional value, only filled in case of small business customers in certain countries
    //private SmallBusinessParticipant[] smallBusinessParticipants;

    public UsersDataVO() {
    }

    /**
     * @return the result
     */
    public List<UserVO> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<UserVO> result) {
        this.result = result;
    }



}

