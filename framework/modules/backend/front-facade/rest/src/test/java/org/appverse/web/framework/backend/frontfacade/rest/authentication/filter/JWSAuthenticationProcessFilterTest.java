package org.appverse.web.framework.backend.frontfacade.rest.authentication.filter;/*
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

import org.appverse.web.framework.backend.rest.filters.auth.JWSJerseyFilter;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.security.Key;
import java.security.KeyStore;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/application-config.xml")
public class JWSAuthenticationProcessFilterTest {

    private Logger logger = LoggerFactory.getLogger(JWSAuthenticationProcessFilterTest.class);

    //server
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Autowired
    JWSAuthenticationProcessingFilter myJWSFilter;

    //client
    @Mock
    private ClientRequestContext context;
    @Mock
    private Configuration config;
    @Mock
    private FeatureContext featureContext;
    @Mock
    private MultivaluedMap<String, Object> headers;
    @Mock
    private MessageBodyWorkers messageBodyWorkers;
    @Mock
    private MessageBodyWriter<Object> messageBodyWriter;
    private String clientCertPath="certificates/client/client.pfx";
    private String clientCertAlias="client.restservices.gbs.db.com";
    private String clientCertPassword="export";



    @Before
    public void init()throws IOException {
        MockitoAnnotations.initMocks(this);
    }
    @After
    public void end()throws IOException {
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
    public void testJWSAuthenticationFilterFailNoHeader() throws Exception{

        ArgumentCaptor<Integer> errorCode = ArgumentCaptor.forClass(Integer.class);
        when(request.getHeader(JWSAuthenticationProcessingFilter.JWS_AUTH_HEADER)).thenReturn(null);
        //test
        myJWSFilter.doFilter(request, response, chain);
        //verification
        verify(response,times(1)).sendError(errorCode.capture());
        int errorCodeValue = errorCode.getValue().intValue();
        logger.info("Response error:{}",errorCodeValue);
        Assert.assertEquals("sendError should be:", HttpServletResponse.SC_UNAUTHORIZED, errorCodeValue);
        verify(chain,times(0)).doFilter(any(ServletRequest.class), any(ServletResponse.class));

    }
    @Test
    public void testJWSAuthenticationFilterFailInvalidHeader() throws Exception{
        //environement
        String content="";
        String requestURL="http://localhost:8080";
        ServletInputStream emptyContent = new DelegatingServletInputStream(new ByteArrayInputStream( content.getBytes()));
        ArgumentCaptor<Integer> errorCode = ArgumentCaptor.forClass(Integer.class);
        when(request.getHeader(JWSAuthenticationProcessingFilter.JWS_AUTH_HEADER)).thenReturn(JWSAuthenticationProcessingFilter.JWS_AUTH_HEADER_TOKEN_MARK+"invalidHash");
        when(request.getInputStream()).thenReturn(emptyContent);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));

        //test
        myJWSFilter.doFilter(request, response, chain);

        //verify
        verify(chain,times(0)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(response, times(1)).sendError(errorCode.capture());//check sendError is not set
        int errorCodeValue = errorCode.getValue().intValue();
        logger.info("Response error:{}",errorCodeValue);
        Assert.assertEquals("sendError should be:", HttpServletResponse.SC_UNAUTHORIZED, errorCodeValue);

    }
    @Test
    public void testJWSAuthenticationFilterFailInvalidSignature() throws Exception{

        String a = "eyJhbGciOiJSUzI1NiJ9.aHR0cDovL2xvY2FsaG9zdDo4MDgw.g1naD_1vfSoXXC-KlOLbzQSmfCyO4JySqAyAC4RSGvEHO2v2V0coWjtzIEkCJ-d-JA_xyxc1me7L3q5PC8zx3IGayIgphqx2KO8CddY0RKTkbP6I3WaKZ3LhzTUZiO9MY5ATmTCYT05HWp9zgW-QAhdqTexzLPS5t1rszkmir0U";
        String content="";
        String requestURL="http://someserver:8080";
        ServletInputStream emptyContent = new DelegatingServletInputStream(new ByteArrayInputStream( content.getBytes()));
        ArgumentCaptor<Integer> errorCode = ArgumentCaptor.forClass(Integer.class);
        when(request.getHeader(JWSAuthenticationProcessingFilter.JWS_AUTH_HEADER)).thenReturn(JWSAuthenticationProcessingFilter.JWS_AUTH_HEADER_TOKEN_MARK+a);
        when(request.getInputStream()).thenReturn(emptyContent);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        //test
        myJWSFilter.doFilter(request, response, chain);
        verify(chain,times(0)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(response, times(1)).sendError(errorCode.capture());//check sendError is not set
        int errorCodeValue = errorCode.getValue().intValue();
        logger.info("Response error:{}",errorCodeValue);
        Assert.assertEquals("sendError should be:", HttpServletResponse.SC_UNAUTHORIZED, errorCodeValue);

    }
    @Test
    public void testJWSAuthenticationFilterHeaderWithNoContent() throws Exception{
        //empty content and specific url
        String content = "";
        String requestURL="http://localhost:8080";
        ServletInputStream emptyContent = new DelegatingServletInputStream(new ByteArrayInputStream( content.getBytes()));

        //prepare client
        KeyStore keyStore = getKeyStoreClient();
        Key key = keyStore.getKey(clientCertAlias, clientCertPassword.toCharArray());
        JWSJerseyFilter jwsJerseyFilter = new JWSJerseyFilter();

        //environment
        ArgumentCaptor<String> argumentHeader = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentHeaderValue = ArgumentCaptor.forClass(String.class);
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(context.getProperty(JWSJerseyFilter.JWS_FILTER_KEY)).thenReturn(key);
        when(context.getUri()).thenReturn(new URI(requestURL));
        when(context.getHeaders()).thenReturn(headers);
        when(context.getEntity()).thenReturn(null);


        //test client
        jwsJerseyFilter.filter(context);
        //validation client
        verify(headers,times(1)).add(argumentHeader.capture(), argumentHeaderValue.capture());
        String headerKey = argumentHeader.getValue();
        String headerValue = argumentHeaderValue.getValue();
        Assert.assertTrue("Response from client should contain token", headerValue.contains(JWSJerseyFilter.JWS_AUTHORIZATION_START_TOKEN));
        logger.info("Client Header Content: {}={}", headerKey,headerValue);

        //prepare server
        when(request.getHeader(headerKey)).thenReturn(headerValue);
        when(request.getInputStream()).thenReturn(emptyContent);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        //test server
        myJWSFilter.doFilter(request, response, chain);

        //validation
        verify(chain,times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(response, times(0)).sendError(anyInt());//check sendError is not set


    }
    @Test
    public void testJWSAuthenticationFilterHeaderWithContent() throws Exception{
        //some content and specific url
        final String content = "{\n" +
                "\t\"id\": \"0001\",\n" +
                "\t\"type\": \"donut\",\n" +
                "\t\"name\": \"Cake\",\n" +
                "\t\"image\":\n" +
                "\t\t{\n" +
                "\t\t\t\"url\": \"images/0001.jpg\",\n" +
                "\t\t\t\"width\": 200,\n" +
                "\t\t\t\"height\": 200\n" +
                "\t\t},\n" +
                "\t\"thumbnail\":\n" +
                "\t\t{\n" +
                "\t\t\t\"url\": \"images/thumbnails/0001.jpg\",\n" +
                "\t\t\t\"width\": 32,\n" +
                "\t\t\t\"height\": 32\n" +
                "\t\t}\n" +
                "}";
        String requestURL="http://localhost:8080";

        //prepare client
        ServletInputStream someContent = new DelegatingServletInputStream(new ByteArrayInputStream( content.getBytes()));
        KeyStore keyStore = getKeyStoreClient();
        Key key = keyStore.getKey(clientCertAlias, clientCertPassword.toCharArray());
        JWSJerseyFilter jwsJerseyFilter = new JWSJerseyFilter();

        //environment
        ArgumentCaptor<String> argumentHeader = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentHeaderValue = ArgumentCaptor.forClass(String.class);
        when(context.getConfiguration()).thenReturn(config);
        when(config.getRuntimeType()).thenReturn(RuntimeType.CLIENT);
        when(context.getProperty(JWSJerseyFilter.JWS_FILTER_KEY)).thenReturn(key);
        when(context.getUri()).thenReturn(new URI(requestURL));
        when(context.getHeaders()).thenReturn(headers);
        when(context.getEntity()).thenReturn(content);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable{
                Object[] arguments = invocation.getArguments();

                ((OutputStream)arguments[6]).write(content.getBytes());
                ((OutputStream)arguments[6]).flush();
                return null;
            }
        }).when(messageBodyWriter).writeTo(any(Object.class), any(Class.class), any(Type.class), any(Annotation[].class), any(MediaType.class), any(MultivaluedMap.class), any(OutputStream.class));
        when(messageBodyWorkers.getMessageBodyWriter(any(Class.class), any(Type.class), any(Annotation[].class), any(MediaType.class))).thenReturn(messageBodyWriter);
        jwsJerseyFilter.setWorkers(messageBodyWorkers);

        Type entity = content.getClass();
        when(context.getEntityClass()).thenReturn((Class)entity);


        //test client
        jwsJerseyFilter.filter(context);
        //validation client
        verify(headers,times(1)).add(argumentHeader.capture(), argumentHeaderValue.capture());
        String headerKey = argumentHeader.getValue();
        String headerValue = argumentHeaderValue.getValue();
        Assert.assertTrue("Response from client should contain token", headerValue.contains(JWSJerseyFilter.JWS_AUTHORIZATION_START_TOKEN));
        logger.info("Client Header Content: {}={}", headerKey,headerValue);

        //prepare server
        when(request.getHeader(headerKey)).thenReturn(headerValue);
        when(request.getInputStream()).thenReturn(someContent);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        //test server
        myJWSFilter.doFilter(request, response, chain);

        //validation
        verify(chain,times(1)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(response, times(0)).sendError(anyInt());//check sendError is not set


    }

}
