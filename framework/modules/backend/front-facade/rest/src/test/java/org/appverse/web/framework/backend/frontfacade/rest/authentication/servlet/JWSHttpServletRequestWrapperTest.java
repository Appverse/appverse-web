package org.appverse.web.framework.backend.frontfacade.rest.authentication.servlet;/*
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.DelegatingServletInputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JWSHttpServletRequestWrapperTest {

    private static final String TEST_CONTENT="Some text content";

    @Mock
    private HttpServletRequest request ;

    @Before
    public void init()throws IOException {
        MockitoAnnotations.initMocks(this);
    }
    @After
    public void end()throws IOException {
        validateMockitoUsage();
    }

    @Test
    public void testWrapperException() throws Exception{

        //environment
        when(request.getInputStream()).thenThrow(IOException.class);
        //test
        JWSHttpServletRequestWrapper jwsHttpServletRequestWrapper = new JWSHttpServletRequestWrapper(request, null);
        //verify
        InputStream is = jwsHttpServletRequestWrapper.getInputStream();
        Assert.assertTrue("Empty stream",is.read()<0);



    }
    @Test
    public void testWrapperObtainContent() throws Exception{

        //environment
        ServletInputStream sis = new DelegatingServletInputStream(new ByteArrayInputStream( TEST_CONTENT.getBytes()));
        when(request.getInputStream()).thenReturn(sis);

        //test
        JWSHttpServletRequestWrapper jwsHttpServletRequestWrapper = new JWSHttpServletRequestWrapper(request, null);
        InputStream is = jwsHttpServletRequestWrapper.getInputStream();

        //validation
        String obtainedContent = obtainContent(is);
        Assert.assertNotNull("content should be not be null",obtainedContent);
        Assert.assertEquals("content should be the same",TEST_CONTENT,obtainedContent);


    }
    private String obtainContent(InputStream is) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        char[] charBuffer = new char[128];
        int bytesRead = -1;

        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
            stringBuilder.append(charBuffer, 0, bytesRead);
        }
        return stringBuilder.toString();
    }

}
