package org.appverse.web.framework.backend.api.model.presentation;

public class RemoteLogResponseVO extends AbstractPresentationBean{

    // Valid statuses
    public final static String OK = "OK";
    public final static String WARN = "WARN";
    public final static String ERROR = "ERROR";

    String status;

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
