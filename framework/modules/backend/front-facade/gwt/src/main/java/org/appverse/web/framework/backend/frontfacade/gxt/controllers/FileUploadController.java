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
package org.appverse.web.framework.backend.frontfacade.gxt.controllers;

import com.google.gwt.user.server.rpc.RPC;

import org.appverse.web.framework.backend.api.helpers.security.ESAPIHelper;
import org.appverse.web.framework.backend.api.helpers.security.SecurityHelper;
import org.appverse.web.framework.backend.api.services.presentation.IFileUploadPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.PresentationException;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.apache.tika.Tika;
import org.apache.tika.parser.txt.CharsetDetector;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
@Singleton
@Path("/fileupload")
public class FileUploadController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ThreadLocal<String> serviceName = new ThreadLocal<String>();

    private final String MAX_FILE_SIZE_PARAM_NAME = "maxFileSize";


    /**
     *
     ------WebKitFormBoundaryx2lXibtD2G3Y2Qkz
     Content-Disposition: form-data; name="file"; filename="iphone100x100.png"
     Content-Type: image/png


     ------WebKitFormBoundaryx2lXibtD2G3Y2Qkz
     Content-Disposition: form-data; name="hiddenFileName"

     iphone100x100.png
     ------WebKitFormBoundaryx2lXibtD2G3Y2Qkz
     Content-Disposition: form-data; name="hiddenMediaCategory"

     2
     ------WebKitFormBoundaryx2lXibtD2G3Y2Qkz
     Content-Disposition: form-data; name="maxFileSize"

     14745600000
     ------WebKitFormBoundaryx2lXibtD2G3Y2Qkz--	 */

    @POST
    @Path("{servicemethodname}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void handleFormUpload(FormDataMultiPart multiPart,
            @PathParam("servicemethodname") String servicemethodname,
            @FormDataParam("file") InputStream stream,
            @FormDataParam(SecurityHelper.XSRF_TOKEN_NAME) String xsrfToken,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws Exception {
    	    	
    	Map<String, String> parameters = new HashMap<String, String>();

        Map<String, List<FormDataBodyPart>> multiParts = multiPart.getFields();
        Iterator<Entry<String, List<FormDataBodyPart>>> keySetIterator = multiParts.entrySet().iterator();
    	while(keySetIterator.hasNext()){
    		Entry<String, List<FormDataBodyPart>> parameter = keySetIterator.next();
    		String parameterName = parameter.getKey();
    		if (!parameterName.equals("file")){
        		List<FormDataBodyPart> valuesList = parameter.getValue();
        		FormDataBodyPart valueBodyPart=null;
        		if (valuesList != null){
        			valueBodyPart = valuesList.get(0);        			
        		}        		
        		parameters.put(parameterName, valueBodyPart.getValue());
    		}
    	}    	

        SecurityHelper.checkXSRFToken(xsrfToken, request);

        serviceName.set(servicemethodname.substring(0, servicemethodname.lastIndexOf(".")));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = stream.read(bytes)) != -1) {
            baos.write(bytes, 0, read);
        }
        baos.flush();
        baos.close();

        if ( baos!= null && baos.size()>0) {
            processCall(response, baos.toByteArray(), parameters);
        } else {
            throw new Exception("The file is empty");
        }
    }

    private void processCall(final HttpServletResponse response,
                             byte[] bytes, final Map<String, String> parameters) throws Exception {
        Object presentationService = applicationContext.getBean(serviceName
                .get());
        if (!(presentationService instanceof IFileUploadPresentationService)) {
            throw new IllegalArgumentException(
                    "Requested Spring Bean is not a File Upload Presentation Service: ("
                            + presentationService + ")");
        }
        String encodedResult = null;
        
        // XSS File upload protection. Clean parameters
        ESAPIHelper.cleanParams(parameters);
        
        // Check file size
        if (parameters.get(MAX_FILE_SIZE_PARAM_NAME) != null) {
            long maxFileSize = Long.parseLong(parameters
                    .get(MAX_FILE_SIZE_PARAM_NAME));
            if (bytes.length > maxFileSize) {
                encodedResult = RPC.encodeResponseForFailure(
                        ((IFileUploadPresentationService) presentationService)
                                .getClass().getDeclaredMethod("uploadFile",
                                bytes.getClass(), Map.class),
                        new GWTMaxFileSizeExceedException());
            }
        }
        
        // XSS File upload protection
        Tika tika = new Tika();
        String mime = tika.detect(bytes);
        if (mime.equals(org.apache.tika.mime.MediaType.APPLICATION_XML.toString()) || 
        	mime.equals(org.apache.tika.mime.MediaType.TEXT_HTML.toString()) || 
        	mime.equals(org.apache.tika.mime.MediaType.TEXT_PLAIN.toString())){
            CharsetDetector charsetDetector = new CharsetDetector();
            charsetDetector.setText(bytes);
            // Get the bytes as a String autodetecting charset
            String fileString = charsetDetector.getString(bytes, null);

            // Skip possible XSS in string
            fileString = ESAPIHelper.stripXSS(fileString);            
            
            // Write the skipped bytes respecting the autodetected charset
            bytes = fileString.getBytes(charsetDetector.detect().getName());
        }

        try {
            String result = ((IFileUploadPresentationService) presentationService)
                    .uploadFile(bytes, parameters);
            encodedResult = RPC.encodeResponseForSuccess(
                    ((IFileUploadPresentationService) presentationService)
                            .getClass().getDeclaredMethod("uploadFile",
                            bytes.getClass(), Map.class), result);
        } catch (PresentationException e) {
            GWTPresentationException pex = new GWTPresentationException(e);
            encodedResult = RPC.encodeResponseForFailure(
                    ((IFileUploadPresentationService) presentationService)
                            .getClass().getDeclaredMethod("uploadFile",
                            bytes.getClass(), Map.class), pex);
        }
        response.getOutputStream().write(encodedResult.getBytes());
        response.getOutputStream().flush();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}