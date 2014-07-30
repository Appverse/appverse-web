package org.appverse.web.framework.backend.security.oauth2.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class Oath2TokenRevokeLogoutHandler implements LogoutHandler {


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        /* TODO: Will call the revoke server endpoint (pending to implement) */
        return;
    }
}
