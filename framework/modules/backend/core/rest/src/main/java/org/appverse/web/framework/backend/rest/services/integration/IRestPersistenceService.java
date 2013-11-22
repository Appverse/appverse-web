package org.appverse.web.framework.backend.rest.services.integration;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.rest.model.integration.IntegrationPaginatedResult;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;

/**
 *
 */
public interface IRestPersistenceService<T extends AbstractIntegrationBean> {

	// Delete methods

	T delete(WebTarget webClient, String idName, Long id) throws Exception;

	T delete(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	StatusResult deleteStatusReturn(WebTarget webClient, String idName, Long id) throws Exception;

	StatusResult deleteStatusReturn(WebTarget webClient, String idName, Long id,
			Map<String, Object> pathParams, Map<String, Object> queryParams) throws Exception;

	// POST methods

	T insert(WebTarget webClient, T object) throws Exception;

	T insert(WebTarget webClient, T object, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	StatusResult insertStatusReturn(WebTarget webClient, T object) throws Exception;

	StatusResult insertStatusReturn(WebTarget webClient, T object, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	// PUT methods

	/**
	 * Make a PUT call to update resource
	 * 
	 * @param webClient
	 * @param object
	 * @param idName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T update(WebTarget webClient, T object, String idName, Long id) throws Exception;

	T update(WebTarget webClient, T object, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	StatusResult updateStatusReturn(WebTarget webClient, T object, String idName, Long id)
			throws Exception;

	StatusResult updateStatusReturn(WebTarget webClient, T object, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	/**
	 * Simple dto retrieve by id
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T retrieve(WebTarget webClient, String idName, Long id) throws Exception;

	T retrieve(WebTarget webClient, Map<String, Object> pathParams, Map<String, Object> queryParams)
			throws Exception;

	T retrieve(WebTarget webClient, String idName, Long id, Map<String, Object> pathParams,
			Map<String, Object> queryParams)
			throws Exception;

	List<T> retrieveList(WebTarget webClient, String idName, Long id) throws Exception;

	List<T> retrieveList(WebTarget webClient, String idName, Long id,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	/**
	 * Retrieve dto list without query criteria
	 * 
	 * @return
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient) throws Exception;

	List<T> retrieveList(WebTarget webClient, Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;

	/**
	 * Retrive dto list filtering by list of Ids
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<T> retrieveList(WebTarget webClient, String idsName, List<Long> ids) throws Exception;

	List<T> retrieveList(WebTarget webClient, String idsName, List<Long> ids,
			Map<String, Object> pathParams, Map<String, Object> queryParams) throws Exception;

	/**
	 * It performs paginated query using params in filterDTO.
	 * Pagination info and data is returned in IntegrationPaginatedResult
	 * Filtering is performed by overwriting applyFilter and defineQuery
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
			final IntegrationPaginatedDataFilter filter)
			throws Exception;

	IntegrationPaginatedResult<T> retrievePagedQuery(WebTarget webClient,
			IntegrationPaginatedDataFilter filter,
			Map<String, Object> pathParams, final Map<String, Object> queryParams) throws Exception;

	/**
	 * Execute search method 
	 * Filtering is performed by overwriting applyFilter and defineQuery
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	/*
	List<T> retrieveQuery(WebTarget webClient, final IntegrationPaginatedDataFilter filter)
			throws Exception;

	List<T> retrieveQuery(WebTarget webClient, IntegrationPaginatedDataFilter filter,
			Map<String, Object> pathParams,
			Map<String, Object> queryParams) throws Exception;
	 */

	/** 
	 * Implementations must overwrite this method to manage mapping from DTO.
	 * Main task of this method is convert the received DTO message to IntegrationPaginatedResult 
	 * 
	 * This method can also define deserialization policy, 
	 * It could depend on the supported message format ( json, xml...)
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	IntegrationPaginatedResult<T> mapPagedResult(Response response)
			throws Exception;

	String getOffsetParamName();

	String getMaxRecordsParamName();

	WebTarget applyQuery(WebTarget webClient, Map<String, Object> queryParams) throws Exception;

	List<T> getTypeSafeList();

	String getMediaType();

}
