#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

/**
 *
 * @author MCLT
 */
@ApiModel(value = "AuthDataVO model")
public class ApplicationRequestDataVO extends AbstractPresentationBean{

    @ApiModelProperty(value = "Language", required=true)
    private String language;

    @ApiModelProperty(value = "appVersion", required=true)
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
