package org.appverse.web.framework.frontend.gwt.json;

import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

/**
 * Created with IntelliJ IDEA.
 * User: RRBL
 * Date: 17/10/13
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationJsonAsyncCallback<T> extends AppverseCallback<T> implements MethodCallback<T> {
    private Method method;
    @Override
    public void onFailure(Method method, Throwable throwable) {
        this.method = method;
        super.onFailure(throwable);
    }

    @Override
    public void onSuccess(Method method, T o) {
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }
}
