#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end

import java.util.List;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "UserDataVO model")
#end
public class UsersDataVO extends ResultDataVO {

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "result")
    #end
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

