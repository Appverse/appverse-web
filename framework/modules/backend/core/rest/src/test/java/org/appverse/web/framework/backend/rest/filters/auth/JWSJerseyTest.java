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
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Key;
import java.security.KeyStore;

@RunWith(MockitoJUnitRunner.class)
public class JWSJerseyTest {

    private String clientCertPath="certificates/client/client.pfx";
    private String clientCertAlias="client.restservices.gbs.db.com";
    private String clientCertPassword="export";

    @Mock
    private ClientRequestContext context;
    @Mock
    private Configuration config;
    @Mock
    private FeatureContext featureContext;
    @Mock
    private MultivaluedMap<String, Object> headers;

    private JWSJerseyFilter jwsJerseyFilter = new JWSJerseyFilter();

    @Before
    public void init()throws IOException{
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testJerseyFilterServer() throws IOException{
        //environment
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.SERVER);
        //test
        jwsJerseyFilter.filter(context);
        //validation
        verify(config,times(1)).getRuntimeType();
        validateMockitoUsage();
    }

    @Test
    public void testJerseyFilterClientNoParams() throws Exception{
        //environment
        ArgumentCaptor<Response> argument = ArgumentCaptor.forClass(Response.class);
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(context.getProperty(JWSJerseyFilter.JWS_FILTER_KEY)).thenReturn(null);
        when(context.getUri()).thenReturn(new URI("http://localhost:8080"));
        //test
        jwsJerseyFilter.filter(context);
        //validation
        verify(context,atLeastOnce()).abortWith(argument.capture());
        Assert.assertEquals("Response should be bad request",Response.Status.BAD_REQUEST.getStatusCode(),argument.getValue().getStatus());
        validateMockitoUsage();

    }
    @Test
    public void testJerseyFilterClientWithParams() throws Exception{
        //load certificate
        KeyStore keyStore = getKeyStoreClient();
        Key key = keyStore.getKey(clientCertAlias, clientCertPassword.toCharArray());
        //environment
        ArgumentCaptor<String> argumentHeader = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentHeaderValue = ArgumentCaptor.forClass(String.class);
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(context.getProperty(JWSJerseyFilter.JWS_FILTER_KEY)).thenReturn(key);
        when(context.getUri()).thenReturn(new URI("http://localhost:8080"));
        when(context.getHeaders()).thenReturn(headers);
        //test
        jwsJerseyFilter.filter(context);
        //validation
        verify(headers,times(1)).add(argumentHeader.capture(), argumentHeaderValue.capture());
        Assert.assertEquals("Response should contain header",JWSJerseyFilter.JWS_AUTHORIZATION_HEADER,argumentHeader.getValue());
        String headerValue = argumentHeaderValue.getValue();
        Assert.assertTrue("Response should contain token",headerValue.contains(JWSJerseyFilter.JWS_AUTHORIZATION_START_TOKEN));
        //validate split by space
        String[] token = headerValue.split(" ");
        Assert.assertEquals("Header should contain signed hash", 2,token.length);
        Assert.assertFalse("Header should contain signed hash", token[1].isEmpty());
        validateMockitoUsage();

    }

    private KeyStore getKeyStoreClient() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(clientCertPath);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(in,clientCertPassword.toCharArray());
        return keyStore;
    }

    @Test
    public void testJerseyFeatureClientWithParamsConstructor1() throws Exception{
        //load certificate
        KeyStore keyStore = getKeyStoreClient();
        Key key = keyStore.getKey(clientCertAlias, clientCertPassword.toCharArray());

        //test constructor1
        JWSJerseyFeature jwsJerseyFeature = new JWSJerseyFeature(keyStore,clientCertAlias,clientCertPassword);

        //environment
        ArgumentCaptor<String> argumentName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> argumentKey = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Class> argumentClazz = ArgumentCaptor.forClass(Class.class);
        when(featureContext.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(config.isRegistered(JWSJerseyFilter.class)).thenReturn(false);

        //test
        boolean response = jwsJerseyFeature.configure(featureContext);
        //validation
        Assert.assertTrue("response should be true", response);
        verify(featureContext,times(1)).property(argumentName.capture(),argumentKey.capture());
        Assert.assertEquals("Response should have the same name as param", JWSJerseyFilter.JWS_FILTER_KEY, argumentName.getValue());
        Assert.assertEquals("Response should have the same key as param", key, argumentKey.getValue());
        verify(featureContext,times(1)).register(argumentClazz.capture());
        Assert.assertEquals("Response should have the same key as param",JWSJerseyFilter.class,argumentClazz.getValue());
        validateMockitoUsage();

    }
    @Test
    public void testJerseyFeatureClientWithParamsConstructor2() throws Exception{
        //load certificate
        KeyStore keyStore = getKeyStoreClient();
        Key key = keyStore.getKey(clientCertAlias, clientCertPassword.toCharArray());
        //test constructor1
        JWSJerseyFeature jwsJerseyFeature = new JWSJerseyFeature(key);

        //environment
        ArgumentCaptor<String> argumentName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> argumentKey = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Class> argumentClazz = ArgumentCaptor.forClass(Class.class);
        when(featureContext.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(config.isRegistered(JWSJerseyFilter.class)).thenReturn(false);

        //test
        boolean response = jwsJerseyFeature.configure(featureContext);
        //validation
        Assert.assertTrue("response should be true", response);
        verify(featureContext,times(1)).property(argumentName.capture(),argumentKey.capture());
        Assert.assertEquals("Response should have the same name as param", JWSJerseyFilter.JWS_FILTER_KEY, argumentName.getValue());
        Assert.assertEquals("Response should have the same key as param", key, argumentKey.getValue());
        verify(featureContext,times(1)).register(argumentClazz.capture());
        Assert.assertEquals("Response should have the same key as param",JWSJerseyFilter.class,argumentClazz.getValue());
        validateMockitoUsage();

    }

}
