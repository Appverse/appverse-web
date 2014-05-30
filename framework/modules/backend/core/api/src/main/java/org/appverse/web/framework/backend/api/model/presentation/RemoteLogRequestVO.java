package org.appverse.web.framework.backend.api.model.presentation;

import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

public class RemoteLogRequestVO extends AbstractPresentationBean{

    String logLevel;

    String message;

    String userAgent;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
