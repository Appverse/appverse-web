package org.appverse.web.framework.backend.rest.filters.auth;/*
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

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Response;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class JWSJerseyTest {

    @Mock
    private ClientRequestContext context;
    @Mock
    private Configuration config;

    private JWSJerseyFilter jwsJerseyFilter = new JWSJerseyFilter();

    @Before
    public void init()throws IOException{
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testJerseyFilterServer()throws IOException{

        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.SERVER);
        jwsJerseyFilter.filter(context);
        validateMockitoUsage();
    }
    @Test
    public void testJerseyFilterClientNoParams()throws IOException{
        ArgumentCaptor<Response> argument = ArgumentCaptor.forClass(Response.class);
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(context.getProperty(JWSJerseyFilter.JWS_FILTER_KEY)).thenReturn(null);
        jwsJerseyFilter.filter(context);
        verify(context,times(1)).abortWith(argument.capture());
        Assert.assertEquals("Response should be bad request",Response.Status.BAD_REQUEST.getStatusCode(),argument.getValue().getStatus());
        validateMockitoUsage();

    }

}
