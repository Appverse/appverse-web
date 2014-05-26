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
package org.appverse.web.framework.backend.rest.services.integration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;

/**
 * This is the interface for REST Integration services implementations providing
 * support for integration with external REST services.
 * 
 * @param <T>
 *            The integration bean type managed by your integration service
 */
public interface IRestPersistenceService<T extends AbstractIntegrationBean> {

	// GET METHODS

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read resource.
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @return T the bean we want to obtain
	 * @throws Exception
	 */
	T retrieve(WebTarget webClient, String idName, Long id) throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read resource.
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 *
	 * @param webClient
	 * @param pathParams
	 * @param queryParams
	 * @return T the bean we want to obtain
	 * @throws Exception
	 */
	T retrieve(WebTarget webClient, Map<String, Object> pathParams, Map<String, Object> queryParams)
			throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read resource.
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 *
	 * @param webClient
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return T the bean we want to obtain
	 * @throws Exception
	 */
	T retrieve(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams)
			throws Exception;

    /**
     * This method executes GET method call against REST service.
     * Usually used to retrieve/read resource.
     * "idName" and "id" are provided as convenience parameters when using GET methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @return T the bean we want to obtain
     * @throws Exception
     */
    T retrieve(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
                      Map<String, Object> queryParams, Map<String, Object> builderProperties)
            throws Exception;


	// GET INPUTSTREAM METHODS
	//Useful to retrieve binary objects (InputStream)

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read resource.
	 * 
	 * This method do not use acceptMediaType method. Accept MediaType is ALWAYS OCTECT_STREAM
	 * 
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 *
	 * @param webClient
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return Response JAX-RS response.
	 * @throws Exception
	 */
	InputStream retrieveInputStream(WebTarget webClient, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams)
			throws Exception;

    /**
     * This method executes GET method call against REST service.
     * Usually used to retrieve/read resource.
     *
     * This method do not use acceptMediaType method. Accept MediaType is ALWAYS OCTECT_STREAM
     *
     * "idName" and "id" are provided as convenience parameters when using GET methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return Response JAX-RS response.
     * @throws Exception
     */
    InputStream retrieveInputStream(WebTarget webClient, String idName, Long id,
            Map<String, Object> pathParams,
            Map<String, Object> queryParams,
            Map<String, Object> builderProperties)
            throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read list of resources.
	 * "idName" and "id" are provided as convenience parameters when using GET methods.
	 * 
	 * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @return List<T> the list of beans we want to obtain
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, String idName, Long id) throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read list of resources.
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return List<T> the list of beans we want to obtain
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes GET method call against REST service.
     * Usually used to retrieve/read list of resources.
     * "idName" and "id" are provided as convenience parameters when using GET methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
     *
     * @param webClient
     * @param idName
     * @param id
     * @param pathParams
     * @param builderProperties
     * @return List<T> the list of beans we want to obtain
     * @throws Exception
     */
    List<T> retrieveList(WebTarget webClient, String idName, Long id,
            Map<String, Object> pathParams,
            Map<String, Object> queryParams,
            Map<String, Object> builderProperties)
            throws Exception;


    /**
     * This method executes GET method call against REST service.
     * Usually used to retrieve/read list of resources.
     *
     * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
     *
     * @param webClient
     * @return List<T> the list of beans we want to obtain
     * @throws Exception
     */
	List<T> retrieveList(WebTarget webClient) throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read list of resources.
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
	 *     
	 * @param webClient
	 * @param pathParams
	 * @param queryParams
	 * @return List<T> the list of beans we want to obtain
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read list of resources by their Ids.
	 * 
	 * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
	 *     
	 * @param webClient
	 * @param idsName key of path fragment to resolve the template.
	 * @param ids List of id's to concat. They are resolved in URL as a string of values comma separated 
	 * @return List<T> the list of beans we want to obtain
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, String idsName, List<Long> ids) throws Exception;

	/**
	 * This method executes GET method call against REST service.
	 * Usually used to retrieve/read list of resources by their Ids.
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * "retrieveList" methods require that service implementation overwrites getTypeSafeList() method.
	 *     
	 * @param webClient
	 * @param idsName key of path fragment to resolve the template.
	 * @param ids List of id's to concat. They are resolved in URL as a string of values comma separated 
	 * @return List<T> the list of beans we want to obtain
	 * @param pathParams
	 * @param queryParams
	 * @return List<T> the list of beans we want to obtain
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, String idsName, List<Long> ids,
			Map<String, Object> pathParams, Map<String, Object> queryParams) throws Exception;

	/**
	 * This method performs paginated query.
	 * Only offset and maxRegs params are read from IntegrationPaginatedDataFilter.
	 * This method could be overriden at application level to provide more customized behaviour
	 *
	 * @param webClient
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
			final IntegrationPaginatedDataFilter filter)
			throws Exception;

	/**
	 * This method performs paginated query.
	 * Only offset and maxRegs params are read from IntegrationPaginatedDataFilter.
	 * This method could be overriden at application level to provide more customized behaviour
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * @param webClient
	 * @param filter
	 * @param pathParams
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
			IntegrationPaginatedDataFilter filter,
			Map<String, Object> pathParams, final Map<String, Object> queryParams) throws Exception;

    /**
     * This method performs paginated query.
     * Only offset and maxRegs params are read from IntegrationPaginatedDataFilter.
     * This method could be overriden at application level to provide more customized behaviour
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param filter
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return
     * @throws Exception
     */
    IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
            IntegrationPaginatedDataFilter filter, final Map<String, Object> pathParams,
            Map<String, Object> queryParams, final Map<String, Object> builderProperties)
            throws Exception;


	// Delete methods

	/**
	 * This method executes DELETE method call against REST service.
	 * "idName" and "id" are provided as convenience parameters when using DELETE methods. 
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @return T the deleted bean
	 * @throws Exception
	 */
	T delete(WebTarget webClient, String idName, Long id) throws Exception;

	/**
	 * This method executes DELETE method call against REST service.
	 * "idName" and "id" are provided as convenience parameters when using DELETE methods.
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return T the deleted bean
	 * @throws Exception
	 */
	T delete(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;


    /**
     * This method executes DELETE method call against REST service.
     * "idName" and "id" are provided as convenience parameters when using DELETE methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return T the deleted bean
     * @throws Exception
     */
    T delete(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
            Map<String, Object> queryParams, Map<String, Object> builderProperties) throws Exception;

	/**
	 * This method executes DELETE method call against REST service.
	 * "idName" and "id" are provided as convenience parameters when using DELETE methods.
	 *  
	 * Use this method when Rest service is not returning the T bean.  
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult deleteStatusReturn(WebTarget webClient, String idName, Long id) throws Exception;

	/**
	 * This method executes DELETE method call against REST service.
	 * "idName" and "id" are provided as convenience parameters when using DELETE methods.
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 *
	 * 	Use this method when Rest service is not returning the T bean.     
	 * 
	 * @param webClient
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult deleteStatusReturn(WebTarget webClient, String idName, Long id,
			Map<String, Object> pathParams, Map<String, Object> queryParams) throws Exception;


    /**
     * This method executes DELETE method call against REST service.
     * "idName" and "id" are provided as convenience parameters when using DELETE methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * 	Use this method when Rest service is not returning the T bean.
     *
     * @param webClient
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return StatusResult Status info included in response.
     * @throws Exception
     */
    StatusResult deleteStatusReturn(WebTarget webClient, String idName, Long id,
            Map<String, Object> pathParams, Map<String, Object> queryParams, Map<String, Object> builderProperties) throws Exception;


	// POST methods

	/**
	 * This method executes POST method call against REST service.
	 * Usually used to insert.
	 * Returning inserted object makes sense since service may update some values (sequences...)
	 *  
	 * @param webClient
	 * @param object
	 * @return T the inserted bean
	 * @throws Exception
	 */
	T insert(WebTarget webClient, T object) throws Exception;

	/**
	 * This method executes POST method call against REST service.
	 * Usually used to insert.
	 * Returning inserted object makes sense since service may update some values (sequences...)
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 *  
	 * @param webClient
	 * @param object
	 * @param pathParams
	 * @param queryParams
	 * @return T the inserted bean
	 * @throws Exception
	 */
	T insert(WebTarget webClient, T object, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes POST method call against REST service.
     * Usually used to insert.
     * Returning inserted object makes sense since service may update some values (sequences...)
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param object
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return T the inserted bean
     * @throws Exception
     */
    T insert(WebTarget webClient, T object, Map<String, Object> pathParams,
           Map<String, Object> queryParams, Map<String, Object> builderProperties) throws Exception;


	// POST INPUTSTREAM METHOD
	//Useful to send binary objects (InputStream)

	/**
	 * This method executes POST  method call against REST service.
	 * Usually used to insert an InputStream (binary object)
	 * 
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 *
	 * @param webClient
	 * @param object
	 * @param pathParams
	 * @param queryParams
	 * @return Response JAX-RS response.
	 * @throws Exception
	 */
	Response insert(WebTarget webClient, InputStream object,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes POST  method call against REST service.
     * Usually used to insert an InputStream (binary object)
     *
     * "idName" and "id" are provided as convenience parameters when using GET methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied
     *
     * @param webClient
     * @param object
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return Response JAX-RS response.
     * @throws Exception
     */
    Response insert(WebTarget webClient, InputStream object,
            Map<String, Object> pathParams,
            Map<String, Object> queryParams,
            Map<String, Object> builderProperties) throws Exception;


	/**
	 * This method executes POST method call against REST service.
	 * Usually used to insert.
	 * 
	 * Use this method when Rest service is not returning the T bean. 
	 *  
	 * @param webClient
	 * @param object
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult insertStatusReturn(WebTarget webClient, T object) throws Exception;

	/**
	 * This method executes POST method call against REST service.
	 * Usually used to insert.
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * Use this method when Rest service is not returning the T bean. 
	 *  
	 * @param webClient
	 * @param object
	 * @param pathParams
	 * @param queryParams
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult insertStatusReturn(WebTarget webClient, T object, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes POST method call against REST service.
     * Usually used to insert.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied.
     *
     * Use this method when Rest service is not returning the T bean.
     *
     * @param webClient
     * @param object
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return StatusResult Status info included in response.
     * @throws Exception
     */
    StatusResult insertStatusReturn(WebTarget webClient, T object, Map<String, Object> pathParams,
            Map<String, Object> queryParams, Map<String, Object> builderProperties) throws Exception;


	// PUT methods

	/**
	 * This method executes PUT method call against REST service.
	 * Usually used to update resource.
	 * "idName" and "id" are provided as convenience parameters when using PUT methods. 
	 * 
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @return T the updated bean
	 * @throws Exception
	 */
	T update(WebTarget webClient, T object, String idName, Long id) throws Exception;

	/**
	 * This method executes PUT method call against REST service.
	 * Usually used to update resource.
	 * "idName" and "id" are provided as convenience parameters when using PUT methods. 
	 *
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 *
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return T the updated bean
	 * @throws Exception
	 */
	T update(WebTarget webClient, T object, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes PUT method call against REST service.
     * Usually used to update resource.
     * "idName" and "id" are provided as convenience parameters when using PUT methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied.
     *
     * @param webClient
     * @param object
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return T the updated bean
     * @throws Exception
     */
    T update(WebTarget webClient, T object, String idName, Long id, Map<String, Object> pathParams,
            Map<String, Object> queryParams, Map<String, Object> builderProperties) throws Exception;


	// PUT INPUTSTREAM METHODS
	//Useful to send binary objects (InputStream)

	/**
	 * This method executes PUT  method call against REST service.
	 * Usually used to update a binary resource 
	 * 
	 * "idName" and "id" are provided as convenience parameters when using GET methods. 
	 *  
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.    
	 *
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return Response JAX-RS response.
	 * @throws Exception
	 */
	Response update(WebTarget webClient, InputStream object, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes PUT  method call against REST service.
     * Usually used to update a binary resource
     *
     * "idName" and "id" are provided as convenience parameters when using GET methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied.
     *
     * @param webClient
     * @param object
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param builderProperties
     * @return Response JAX-RS response.
     * @throws Exception
     */
    Response update(WebTarget webClient, InputStream object, String idName, Long id,
            Map<String, Object> pathParams,
            Map<String, Object> queryParams,
            Map<String, Object> builderProperties) throws Exception;

	/**
	 * This method executes PUT method call against REST service.
	 * Usually used to update resource.
	 * "idName" and "id" are provided as convenience parameters when using PUT methods. 
	 * 
	 * Use this method when Rest service is not returning the T bean.
	 * 
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult updateStatusReturn(WebTarget webClient, T object, String idName, Long id)
			throws Exception;

	/**
	 * This method executes PUT method call against REST service.
	 * Usually used to update resource.
	 * "idName" and "id" are provided as convenience parameters when using PUT methods. 
	 * 
	 * "pathParams" Map allows user to perform every template path resolution is needed.
	 * "queryParams" elements are included as parameters in request.
	 * 
	 * Use this method when Rest service is not returning the T bean.
	 * 
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @param pathParams
	 * @param queryParams
	 * @return StatusResult Status info included in response.
	 * @throws Exception
	 */
	StatusResult updateStatusReturn(WebTarget webClient, T object, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

    /**
     * This method executes PUT method call against REST service.
     * Usually used to update resource.
     * "idName" and "id" are provided as convenience parameters when using PUT methods.
     *
     * "pathParams" Map allows user to perform every template path resolution is needed.
     * "queryParams" elements are included as parameters in request.
     * "builderProperties" properties corresponding to builder request features to be applied.
     *
     * Use this method when Rest service is not returning the T bean.
     *
     * @param webClient
     * @param object
     * @param idName
     * @param id
     * @param pathParams
     * @param queryParams
     * @param  builderProperties
     * @return StatusResult Status info included in response.
     * @throws Exception
     */
    StatusResult updateStatusReturn(WebTarget webClient, T object, String idName, Long id,
            Map<String, Object> pathParams,
            Map<String, Object> queryParams,
            Map<String, Object> builderProperties) throws Exception;

	/** 
	 * Implementations must overwrite this method to manage mapping from DTO.
	 * Main task of this method is to unmarshall data and convert the received DTO message to IntegrationPaginatedResult 
	 * 
	 * This method can also define deserialization policy, 
	 * Implementation could depend on the supported message format ( json, xml...)
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	IntegrationPaginatedResult<T> mapPagedResult(Response response)
			throws Exception;

	/**
	 * Service implementation must override this method to use paged queries.
	 * Return value should contains the offset param name in the Rest service. 
	 * 
	 * @return
	 */
	String getOffsetParamName();

	/**
	 * Service implementation must override this method to use paged queries.
	 * Return value should contains the max records param name in the Rest service. 
	 * 
	 * @return
	 */
	String getMaxRecordsParamName();

	/**
	 * Insert query params into webClient
	 * 
	 * @param webClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	WebTarget applyQuery(WebTarget webClient, Map<String, Object> queryParams) throws Exception;

	/**
	 * This method must be overridden to use any "retrieveList" methods.
	 * Service implementation should define the actual Integration bean class in the return of method signature.
	 * Return value of this method is not used, so can be null
	 * 
	 * @return
	 */
	List<T> getTypeSafeList();

	/**
	 * Overwrite to define consume media type of the Service Implementacion ( JSON, XML, text... )
	 * JSON is the default value
	 * 
	 * @return
	 */
	String acceptMediaType();

}
