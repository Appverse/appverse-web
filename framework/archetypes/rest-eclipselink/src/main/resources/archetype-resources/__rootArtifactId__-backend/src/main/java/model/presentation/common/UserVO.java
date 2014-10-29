#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

import java.util.Date;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "UserVO model")
#end
public class UserVO extends AbstractPresentationBean{

    private Long id;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "name")
    #end
    private String name;

    private String username;
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "surname")
    #end
    private String surname;

    /**
     * Mr, Mrs.. etc
     */
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "title")
    #end
    private String title;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "lastLoggedDate")
    #end
    private Date lastLoggedDate;

    public UserVO() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the lastLoggedDate
     */
    public Date getLastLoggedDate() {
        return lastLoggedDate;
    }

    /**
     * @param lastLoggedDate the lastLoggedDate to set
     */
    public void setLastLoggedDate(Date lastLoggedDate) {
        this.lastLoggedDate = lastLoggedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


