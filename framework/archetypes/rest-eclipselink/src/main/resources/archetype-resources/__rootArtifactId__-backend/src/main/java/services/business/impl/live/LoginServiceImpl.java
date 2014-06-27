#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the conditions of the AppVerse Public License v2.0
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */
package ${package}.services.business.impl.live;

import ${package}.helpers.ErrorCodes;
import ${package}.model.business.customer.User;
import ${package}.services.business.LoginService;
import ${package}.services.integration.UserRepository;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.api.services.business.BusinessException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("loginService")
public class LoginServiceImpl extends AbstractBusinessService implements
        LoginService{


    @AutowiredLogger
    private Logger logger;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserRepository userRepository;

    @PreAuthorize("permitAll")
    public String getLoggedUser(){
        String username = DEFAULT_ANONYMOUS_USER;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return username;
    }

    @PreAuthorize("permitAll")
    public User authenticate(Authentication authenticationToken ) throws Exception{
        User user = null;
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException bce){
            throw new BusinessException(ErrorCodes.ERROR_CODE_login_wrongcredentials);
        }catch(AuthenticationException ae){
            throw new BusinessException(ErrorCodes.ERROR_CODE_unhandled, ae);
        }
        if (authentication!=null && authentication.isAuthenticated()){
            logger.info("authenticated user: {}", authenticationToken.getName());


            //obatin user
            String username = authentication.getName();

            //user is authenticated
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //create user object
            user = new User();
            user.setName(username);
            user.setLastLoggedDate(new Date());


        }
        return user;
    }

    @PreAuthorize("permitAll")
    public void logout()throws Exception{
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
