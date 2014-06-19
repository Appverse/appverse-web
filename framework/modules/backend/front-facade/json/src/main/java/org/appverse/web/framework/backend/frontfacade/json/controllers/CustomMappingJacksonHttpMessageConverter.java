/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (â€œAPL v2.0â€�). If a copy of the APL was not distributed with this 
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
package org.appverse.web.framework.backend.frontfacade.json.controllers;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomMappingJacksonHttpMessageConverter extends
		AbstractHttpMessageConverter<Object> {

	private final Logger logger = LoggerFactory
			.getLogger(CustomMappingJacksonHttpMessageConverter.class);

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private ObjectMapper objectMapper = new ObjectMapper();

	private boolean prefixJson = false;

	/**
	 * Construct a new {@code BindingJacksonHttpMessageConverter}.
	 */
	public CustomMappingJacksonHttpMessageConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		JavaType javaType = getJavaType(clazz);
		return this.objectMapper.canDeserialize(javaType) && canRead(mediaType);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return this.objectMapper.canSerialize(clazz) && canWrite(mediaType);
	}

	@Override
	protected Long getContentLength(Object o, MediaType contentType) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JsonGenerator baosjsonGenerator = this.objectMapper
					.getJsonFactory().createJsonGenerator(baos);
			this.objectMapper.writeValue(baosjsonGenerator, o);
			ByteArrayOutputStream os = (ByteArrayOutputStream) baosjsonGenerator
					.getOutputTarget();
			return new Long(os.size());
		} catch (Exception e) {
			return new Long(0);
		}

	}
	
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	

	private JsonEncoding getEncoding(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

	/**
	 * Returns the Jackson {@link JavaType} for the specific class.
	 * 
	 * <p>
	 * Default implementation returns
	 * {@link TypeFactory#type(java.lang.reflect.Type)}, but this can be
	 * overridden in subclasses, to allow for custom generic collection
	 * handling. For instance:
	 * 
	 * <pre class="code">
	 * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
	 * 	if (List.class.isAssignableFrom(clazz)) {
	 * 		return TypeFactory.collectionType(ArrayList.class, MyBean.class);
	 * 	} else {
	 * 		return super.getJavaType(clazz);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param clazz
	 *            the class to return the java type for
	 * @return the java type
	 */
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputMessage.getBody(), DEFAULT_CHARSET));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + " ");
		}

		logger.debug("Middleware recieves " + clazz.getName() + " "
				+ stringBuilder.toString());
		JavaType javaType = getJavaType(clazz);
		return this.objectMapper.readValue(stringBuilder.toString(), javaType);
	}

	protected Object readInternal(Class<?> clazz, String inputMessage)
			throws IOException, HttpMessageNotReadableException {

		logger.debug("Middleware recieves " + clazz.getName() + " "
				+ inputMessage);
		JavaType javaType = getJavaType(clazz);
		return this.objectMapper.readValue(inputMessage, javaType);
	}

	/**
	 * Sets the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>
	 * Setting a custom-configured {@code ObjectMapper} is one way to take
	 * further control of the JSON serialization process. For example, an
	 * extended {@link org.codehaus.jackson.map.SerializerFactory} can be
	 * configured that provides custom serializers for specific types. The other
	 * option for refining the serialization process is to use Jackson's
	 * provided annotations on the types to be serialized, in which case a
	 * custom-configured ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	/**
	 * Indicates whether the JSON output by this view should be prefixed with
	 * "{} &&". Default is false.
	 * <p>
	 * Prefixing the JSON string in this manner is used to help prevent JSON
	 * Hijacking. The prefix renders the string syntactically invalid as a
	 * script so that it cannot be hijacked. This prefix does not affect the
	 * evaluation of JSON, but if JSON validation is performed on the string,
	 * the prefix would need to be ignored.
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getEncoding(outputMessage.getHeaders()
				.getContentType());
		JsonGenerator jsonGenerator = this.objectMapper.getJsonFactory()
				.createJsonGenerator(outputMessage.getBody(), encoding);
		if (this.prefixJson) {
			jsonGenerator.writeRaw("{} && ");
		}
		this.objectMapper.writeValue(jsonGenerator, o);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JsonGenerator baosjsonGenerator = this.objectMapper.getJsonFactory()
				.createJsonGenerator(baos, encoding);
		this.objectMapper.writeValue(baosjsonGenerator, o);
		logger.debug("Middleware returns " + o.getClass().getName() + " "
				+ baos.toString());
	}
	
	protected Object[] readInternal(Class<?>[] parameterTypes, String payload) throws Exception {
		JsonFactory jsonFactory = this.objectMapper.getJsonFactory();
		JsonParser jp = jsonFactory.createJsonParser(payload);
		JsonToken token;
		List<Object> lObjs = new ArrayList<Object>();
		int i=0;
		while ((token = jp.nextToken()) != null) {
		    switch (token) {
		    	case VALUE_NUMBER_INT:
                case VALUE_STRING:
		        case START_OBJECT:
		        	Object obj = jp.readValueAs(parameterTypes[i]);
		        	lObjs.add(obj);
				    i++;
		            break;
			default:
				break;
		    }
		}
		if( lObjs.size() != parameterTypes.length) {
			throw new Exception("Parsed parameters do not match requested types.");
		}
		Object [] parametersFound = lObjs.toArray();
		return parametersFound;
	}

}
