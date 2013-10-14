package org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.frontend.gwt.rest;

import java.util.Date;

import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.appverse.web.framework.frontend.gwt.common.utils.GWTUtils;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.Callback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class ApplicationRestAsyncCallback<T> implements MethodCallback<T>, Callback<T,Throwable> {
	public static final int CUSTOM_SESSION_EXPIRED_CODE = 901;
	public static final String CUSTOM_EXPIRED_JSP = "login.jsp?sessionExpired=true";


	/**
	 * Default Presentation Exception treatment without closing the browser tab.
	 * If the exception code is informed and is coded in
	 * ExceptionMessages.properties it will be automatically translated and the
	 * corresponding message will be shown to the user. If the exception code is
	 * not informed or is not coded in ExceptionMessages.properties a generic
	 * exception notification will be shown to the user using the exception
	 * message information.
	 * 
	 * @param PresentationException
	 *            PresentationException instance to handle
	 */

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
	 * @param PresentationException
	 *            PresentationException instance to handle.
	 */

	public void handlePresentationException(final GWTPresentationException ex) {
		defaultPresentationExceptionTreatmentWithoutClosingWindow(ex);
	}
	
	private void handleFailure(Throwable ex, Method method) {
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
			
			pex = new GWTPresentationException(ex.getMessage()+(method!=null?" ["+method.getResponse().getText()+"]":""), null);
		}
		handlePresentationException(pex);
		return;
	}
	
	@Override
	public void onFailure(Method method, Throwable ex) {
		System.out.println("Application failure ["+ex.getMessage()+","+(method!=null?method.getClass().getName():"Method is Null")+"]...");
		handleFailure(ex, method);
	}

	@Override
	public void onFailure(Throwable ex) {
		System.out.println("Application failure ["+ex.getMessage()+"]...");
		handleFailure(ex, null);		
	}

	@Override
	public void onSuccess(T result) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onSuccess(Method method, T response) {
		// TODO Auto-generated method stub
	}

}
