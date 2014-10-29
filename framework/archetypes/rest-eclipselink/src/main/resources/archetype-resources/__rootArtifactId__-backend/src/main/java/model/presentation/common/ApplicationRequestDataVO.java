#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;
#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "AuthDataVO model")
#end
public class ApplicationRequestDataVO extends AbstractPresentationBean{
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "Language", required=true)
    #end
    private String language;

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "appVersion", required=true)
    #end
    private String appVersion;

    public ApplicationRequestDataVO() {
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion the appVersion to set
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

}
