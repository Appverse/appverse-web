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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XSSHeaderFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;

    @Before
    public void init()throws IOException {
        MockitoAnnotations.initMocks(this);
    }
    @After
    public void end()throws IOException {
        validateMockitoUsage();
    }
    @Test
    public void testXSSHeaderWithoutRequestOrigin() throws Exception{
        //environment
        ArgumentCaptor<String> allowOriginString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> cacheControl = ArgumentCaptor.forClass(String.class);
        when(request.getHeader(XSSHeaderFilter.REQUEST_ORIGIN)).thenReturn(null);
        //test
        XSSHeaderFilter filter = new XSSHeaderFilter();
        filter.doFilter(request,response,chain);
        //validation
        verify(chain,times(1)).doFilter(any(ServletRequest.class),any(ServletResponse.class));
        verify(response,times(1)).addHeader(eq("Access-Control-Allow-Origin"), allowOriginString.capture());
        Assert.assertEquals("Access-Control-Allow-Origin should be *",XSSHeaderFilter.ACCESS_CONTROL_ALLOW_ORIGIN_DEFAULT , allowOriginString.getValue());

        verify(response,times(1)).addHeader(eq("Cache-Control"), cacheControl.capture());
        Assert.assertEquals("Cache-Control should be private","private" , cacheControl.getValue());
    }

    @Test
    public void testXSSHeaderWithRequestOrigin() throws Exception{
        //environment
        String originUrl="http://somewhere:8080";
        ArgumentCaptor<String> allowOriginString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> cacheControl = ArgumentCaptor.forClass(String.class);
        when(request.getHeader(XSSHeaderFilter.REQUEST_ORIGIN)).thenReturn(originUrl);
        //test
        XSSHeaderFilter filter = new XSSHeaderFilter();
        filter.doFilter(request,response,chain);
        //validation
        verify(chain,times(1)).doFilter(any(ServletRequest.class),any(ServletResponse.class));
        verify(response,times(1)).addHeader(eq("Access-Control-Allow-Origin"), allowOriginString.capture());
        Assert.assertEquals("Access-Control-Allow-Origin should be :"+originUrl,originUrl, allowOriginString.getValue());

        verify(response,times(1)).addHeader(eq("Cache-Control"),cacheControl.capture());
        Assert.assertEquals("Cache-Control should be private","private" , cacheControl.getValue());
    }
    @Test
    public void testXSSHeaderWithRequestOriginAndConfigFalse() throws Exception{
        //environment
        String originUrl="http://somewhere:8080";
        ArgumentCaptor<String> cacheControl = ArgumentCaptor.forClass(String.class);
        when(request.getHeader(XSSHeaderFilter.REQUEST_ORIGIN)).thenReturn(originUrl);
        //test
        XSSHeaderFilter filter = new XSSHeaderFilter();
        filter.setAccessControlAllow(false);
        filter.doFilter(request,response,chain);
        //validation
        verify(chain,times(1)).doFilter(any(ServletRequest.class),any(ServletResponse.class));
        //should not be called 0 times
        verify(response,times(0)).addHeader(eq("Access-Control-Allow-Origin"), any(String.class));

        verify(response,times(1)).addHeader(eq("Cache-Control"),cacheControl.capture());
        Assert.assertEquals("Cache-Control should be private","private" , cacheControl.getValue());
    }



}
