package org.appverse.web.framework.frontend.gwt.callback;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.appverse.web.framework.frontend.gwt.common.utils.GWTUtils;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;

public class AppverseCallback<T> implements AsyncCallback<T>, MethodCallback<T> {

    public static final int CUSTOM_SESSION_EXPIRED_CODE = 901;
    public static final String CUSTOM_EXPIRED_JSP = "login.jsp?sessionExpired=true";

    // TODO: See how to do this so that we can have a ExceptionTranslator (with
    // property files in the application) but
    // supported by the framework. See for instance if we can use
    // "GWT Injection" / Deferred binding using an interface class
    // an use a "replace-with" to change the implementation (which has to be in
    // the application)
    /**
     * Default Presentation Exception treatment closing the browser tab. If the
     * exception code is informed and is coded in ExceptionMessages.properties
     * it will be automatically translated and the corresponding message will be
     * shown to the user. If the exception code is not informed or is not coded
     * in ExceptionMessages.properties a generic exception notification will be
     * shown to the user using the exception message information.
     *
     * @param PresentationException
     *            PresentationException instance to handle
     */
    // public void defaultPresentationExceptionTreatmentClosingWindow(
    // PresentationException ex) {
    // String translatedMessage = ExceptionTranslationManager.translate(ex);
    // ApplicationNotificationManager am = new ApplicationNotificationManager();
    // am.showExceptionAndClose(ex, translatedMessage);
    // }

    // TODO: See how to do this so that we can have a ExceptionTranslator (with
    // property files in the application) but
    // supported by the framework. See for instance if we can use
    // "GWT Injection" / Deferred binding using an interface class
    // an use a "replace-with" to change the implementation (which has to be in
    // the application)
    /**
     * Default Presentation Exception treatment without closing the browser tab.
     * If the exception code is informed and is coded in
     * ExceptionMessages.properties it will be automatically translated and the
     * corresponding message will be shown to the user. If the exception code is
     * not informed or is not coded in ExceptionMessages.properties a generic
     * exception notification will be shown to the user using the exception
     * message information.
     *
     * @param ex
     *            PresentationException instance to handle
     */
    // public void defaultPresentationExceptionTreatmentWithoutClosingWindow(
    // final PresentationException ex) {
    // String translatedMessage = ExceptionTranslationManager.translate(ex);
    // ApplicationNotificationManager am = new ApplicationNotificationManager();
    // am.showException(ex, translatedMessage);
    // }

    public void defaultPresentationExceptionTreatmentWithoutClosingWindow(
            final GWTPresentationException ex) {
        NotificationManager.showError("Error Message:"
                + ex.getMessage()
                + "Error Time:"
                + DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ")
                .format(new Date()));
    }

    /**
     * This method produces client url redirection to a custom expired JSP with a
     * default name in your application context. This is the default treatment for
     * expired session (or unauthorized), but can be overriden
     */
    public void handleExpiredSessionException()
    {
        String expiredPageUrl = GWTUtils.getModuleIndependentBaseURL() + CUSTOM_EXPIRED_JSP;
        Window.Location.replace(expiredPageUrl);
    }

    /**
     * Default Presentation Exception treatment. Default treatment will not
     * close the browser tab This method can be overriden (without calling
     * super()) to implement a particular treatment
     *
     * @param ex
     *            PresentationException instance to handle.
     */
    // public void handlePresentationException(final PresentationException ex) {
    // defaultPresentationExceptionTreatmentWithoutClosingWindow(ex);
    // }

    public void handlePresentationException(final GWTPresentationException ex) {
        defaultPresentationExceptionTreatmentWithoutClosingWindow(ex);
    }

    /**
     * Default onFailure() method. If the exception is an instance of
     * PresentationException and a code is informed we call
     * handleApplicationExeption which provides a default treatment translating
     * the exception code to a message and showing a dialog to the user without
     * closing the browser tab. handleApplicationException can be overriden if
     * we want to implement especific PresentationException treatment or just
     * close the browser tab.
     * If receive StatusCodeException, check expired session and call
     * handleExpiredSessionException(), which can be overridden
     *
     * @param ex
     *            Exception to handle.
     */
    public void onFailure(final Throwable ex) {
        GWTPresentationException pex = null;
        if (ex instanceof GWTPresentationException) {
            pex = (GWTPresentationException) ex;
        } else if (ex instanceof StatusCodeException) {
            if (((StatusCodeException) ex).getStatusCode() == CUSTOM_SESSION_EXPIRED_CODE)
            {
                handleExpiredSessionException();
                return;
            }
            else
                return;
            // Application exception (with an application code)
        } else {
            // We should not receive exceptions different from
            // GWTPresentationException in RPC commands. We control this just
            // for security.
            pex = new GWTPresentationException(ex.getMessage(), null);
        }
        handlePresentationException(pex);
        return;
    }

    public void onSuccess(T o) {}

    @Override
    public void onFailure(Method method, Throwable throwable) {
        this.onFailure(throwable);
    }

    @Override
    public void onSuccess(Method method, T t) {
        this.onSuccess(t);
    }
}
