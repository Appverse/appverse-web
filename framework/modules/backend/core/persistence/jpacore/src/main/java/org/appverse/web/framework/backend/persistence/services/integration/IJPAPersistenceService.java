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
package org.appverse.web.framework.backend.persistence.services.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.api.model.integration.ResultIntegrationBean;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.UpdateJpaCallback;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Map;

/**
 * This is the interface for JPA Persistence services implementations providing
 * support for JPA operations.
 * 
 * @param <T>
 *            The integration bean type managed by your integration service
 */
public interface IJPAPersistenceService<T extends AbstractIntegrationBean> {

	/**
	 * Performs JPA contains operation
	 * 
	 * @param beanP
	 * @return boolean Returns if the bean is managed by the EntityManager
	 * @throws Exception
	 */
	boolean contains(final T beanP) throws Exception;

	/**
	 * This methods count the number of T elements
	 * 
	 * @return int The number of records found
	 */
	public int count() throws Exception;

	/**
	 * This method allows you to execute a count passing an
	 * IntegrationDataFilter as a parameter. You can pass an
	 * IntegrationPaginatedDataFilter as a parameter in order to perform counts
	 * related to remote pagination. The IntegrationDataFilter allows you to
	 * apply very simple conditions over <T> beans in order to filter the
	 * results to be retrieved.
	 * 
	 * @param filter
	 * @return int The number of records found
	 * @throws Exception
	 */
	int count(final IntegrationDataFilter filter) throws Exception;

	/**
	 * This methods allows you to perform any JPA operation providing direct
	 * access to the EntityManager by means a QueryJpaCallback<T> object for <T>
	 * beans for a count operation. If you want predefined standard hints, set
	 * QueryJPACallback "standardHints" boolean parameter to true.
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	int count(QueryJpaCallback<T> query) throws Exception;

	/**
	 * This methods count the number of T elements returned by a JPQL query
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @return int The number of records found
	 * @throws Exception
	 */
	int count(String queryString) throws Exception;

	/**
	 * This method allows you to execute a count passing a JPQL query string and
	 * a map with parameters.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @return int The number of records found
	 * @throws Exception
	 */
	int count(final String queryString, final Map<String, Object> parameters)
			throws Exception;

	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires. Queries byName must be
	 * added in your orm.xml file
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 * @throws Exception
	 */
	int count(final String queryName, final Object... values) throws Exception;

	/**
	 * This methods allows you to perform JPA operation providing direct access
	 * to the EntityManager by means a QueryJpaCallback<Object[]> returning the
	 * number of objects found. Use this method when you need to return
	 * directlyobjects that does not match neither the type of the managed
	 * objects of your repository <T> a result of your query nor a "resultType".
	 * A typical usage is when you do not want to retrieve a full object or a
	 * combination of different attributes of different objects involved in the
	 * query or just calculated fields. In summary, use this method when you do
	 * not have an IntegrationBean or "resultType" to act as a container of the
	 * data returned by the query. In this case you get directly a List with an
	 * array of objects including the selected data. Of course, in this case,
	 * you are responsible to parse this data to objects so do not use this
	 * method if you have an specific object to hold the data.
	 * 
	 * @param query
	 * @return int The number of records found
	 * @throws Exception
	 */
	int countObjectArray(QueryJpaCallback<Object[]> query) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning the number of
	 * objects found. Use this method when you need to return directly objects
	 * that does not match neither the type of the managed objects of your
	 * repository <T> a result of your query nor a "resultType". A typical usage
	 * is when you do not want to retrieve a full object or a combination of
	 * different attributes of different objects involved in the query or just
	 * calculated fields. In summary, use this method when you do not have an
	 * IntegrationBean or "resultType" to act as a container of the data
	 * returned by the query. In this case you get directly a List with an array
	 * of objects including the selected data. Of course, in this case, you are
	 * responsible to parse this data to objects so do not use this method if
	 * you have an specific object to hold the data.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @return int The number of records found
	 * @throws Exception
	 */
	int countObjectArray(String queryString) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning the number of
	 * objects found, and a map of parameters. Use this method when you need to
	 * return directly objects that does not match neither the type of the
	 * managed objects of your repository <T> a result of your query nor a
	 * "resultType". A typical usage is when you do not want to retrieve a full
	 * object or a combination of different attributes of different objects
	 * involved in the query or just calculated fields. In summary, use this
	 * method when you do not have an IntegrationBean or "resultType" to act as
	 * a container of the data returned by the query. In this case you get
	 * directly a List with an array of objects including the selected data. Of
	 * course, in this case, you are responsible to parse this data to objects
	 * so do not use this method if you have an specific object to hold the
	 * data.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @return int The number of records found
	 * @throws Exception
	 */
	int countObjectArray(final String queryString,
			final Map<String, Object> parameters) throws Exception;

	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires, returning the number of
	 * objects found. Queries byName must be added in your orm.xml file. Use
	 * this method when you need to return directly objects that does not match
	 * neither the type of the managed objects of your repository <T> a result
	 * of your query nor a "resultType". A typical usage is when you do not want
	 * to retrieve a full object or a combination of different attributes of
	 * different objects involved in the query or just calculated fields. In
	 * summary, use this method when you do not have an IntegrationBean or
	 * "resultType" to act as a container of the data returned by the query. In
	 * this case you get directly a List with an array of objects including the
	 * selected data. Of course, in this case, you are responsible to parse this
	 * data to objects so do not use this method if you have an specific object
	 * to hold the data.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return int The number of records found
	 * @throws Exception
	 */
	int countObjectArray(final String queryName, final Object... values)
			throws Exception;

	/**
	 * This methods allows you to perform any JPA operation providing direct
	 * access to the EntityManager by means a QueryJpaCallback<U extends
	 * ResultIntegrationBean> object for <U extends ResultIntegrationBean> beans
	 * for a count operation. If you want predefined standard hints, set
	 * QueryJPACallback "standardHints" boolean parameter to true. Use this
	 * method when you need to count IntegrationBeans that does not match the
	 * type of the managed objects of your repository <T>
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> int countResultType(
			QueryJpaCallback<U> query) throws Exception;

	/**
	 * This method allows you to execute a count passing a JPQL query string.
	 * Use this method when you need to count IntegrationBeans that does not
	 * match the type of the managed objects of your repository <T>
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @return int The number of records found
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> int countResultType(String queryString)
			throws Exception;

	/**
	 * This method allows you to execute a count passing a JPQL query string and
	 * a map with parameters. Use this method when you need to count
	 * IntegrationBeans that does not match the type of the managed objects of
	 * your repository <T>
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @return int The number of records found
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> int countResultType(
			final String queryString, final Map<String, Object> parameters)
			throws Exception;

	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires. Queries byName must be
	 * added in your orm.xml file Use this method when you need to count
	 * IntegrationBeans that does not match the type of the managed objects of
	 * your repository <T>
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> int countResultType(
			final String queryName, final Object... values) throws Exception;

	/**
	 * Deletes a <T> bean
	 * 
	 * @param bean
	 * @throws Exception
	 */
	void delete(T bean) throws Exception;

	/**
	 * Deletes all <T> beans
	 * 
	 * @throws Exception
	 */
	void deleteAll() throws Exception;

	/**
	 * Performs JPA detach operation
	 * 
	 * @param beanP
	 * @throws Exception
	 */
	void detach(final T beanP) throws Exception;

	/**
	 * Performs JPA flush operation
	 * 
	 * @throws Exception
	 */
	void flush() throws Exception;

	/**
	 * Executes a persist of a <T> bean
	 * 
	 * @param bean
	 * @return long The T bean primary key
	 * @throws Exception
	 */
	long persist(T bean) throws Exception;

	/**
	 * Performs JPA refresh operation
	 * 
	 * @param beanP
	 * @throws Exception
	 */
	void refresh(final T beanP) throws Exception;

	/**
	 * This method allows you retrieve a <T> bean passing an
	 * IntegrationDataFilter as a parameter. You can pass an
	 * IntegrationPaginatedDataFilter as a parameter in order to perform counts
	 * when you have to remote pagination. Besides, the IntegrationDataFilter
	 * allows you to apply very simple conditions over <T> beans in order to
	 * filter the results to be retrieved. Use this method when your query has
	 * to return always a result (and just one result). In other case use
	 * retrieveAll returning a List. If no results are found or non unique, a
	 * exception is thrown.
	 * 
	 * @param filter
	 * @return The T bean retrieved
	 * @throws NoResultException
	 *             if there is no result
	 * @throws NonUniqueResultException
	 *             if more than one result
	 */
	T retrieve(final IntegrationDataFilter filter) throws Exception;

	/**
	 * Retrieves a <T> bean using its primary key
	 * 
	 * @param pk
	 *            The <T> bean primary key
	 * @return T The T bean retrieved
	 * @throws Exception
	 */
	T retrieve(long pk) throws Exception;

	/**
	 * This methods allows you to perform retrieval JPA operations providing
	 * direct access to the EntityManager by means a QueryJpaCallback<T> object
	 * for <T> beans. If you want predefined standard hints, set
	 * QueryJPACallback "standardHints" boolean parameter to true. Use this
	 * method when you expect your query to return just one T object. If no
	 * results are found or non unique, a exception is thrown
	 * 
	 * @param query
	 * @return
	 * @throws NoResultException
	 *             if there is no result
	 * @throws NonUniqueResultException
	 *             if more than one result
	 */
	T retrieve(QueryJpaCallback<T> query) throws Exception;

	/**
	 * Retrieves a <T> bean according to a JPQL query string. Use this method
	 * when your query has to return always a result (and just one result). In
	 * other case use retrieveAll returning a List. If no results are found or
	 * non unique, a exception is thrown.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 * @throws NoResultException
	 *             if there is no result
	 * @throws NonUniqueResultException
	 *             if more than one result
	 */
	T retrieve(final String queryString) throws Exception;

	/**
	 * Retrieves a <T> bean according to a JPQL query string and a map with
	 * parameters. Use this method when your query has to return always a result
	 * (and just one result). In other case use retrieveAll returning a List. If
	 * no results are found or non unique, a exception is thrown.
	 * 
	 * @param queryString
	 * @param parameters
	 * @return
	 * @throws NoResultException
	 *             if there is no result
	 * @throws NonUniqueResultException
	 *             if more than one result
	 */
	T retrieve(final String queryString, final Map<String, Object> parameters)
			throws Exception;

	/**
	 * Retrieves a <T> bean according to a JPQL query byName and the set of
	 * parameters the query requires. Queries byName must be added in your
	 * orm.xml file. Use this method when your query has to return always a
	 * result (and just one result). In other case use retrieveAll returning a
	 * List. If no results are found or non unique, a exception is thrown.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 * @throws NoResultException
	 *             if there is no result
	 * @throws NonUniqueResultException
	 *             if more than one result
	 */
	T retrieve(final String queryName, final Object... values) throws Exception;

	/**
	 * Retrieves a <T> bean using its primary key passing the full object as a
	 * parameter
	 * 
	 * @param pk
	 *            The <T> bean primary key
	 * @return T The T bean retrieved
	 * @throws Exception
	 */
	T retrieve(T bean) throws Exception;

	/**
	 * This method retrieve all <T> beans
	 * 
	 * @return List<T> The list of <T> bean retrieved
	 * @throws Exception
	 */
	List<T> retrieveList() throws Exception;

	/**
	 * This method allows you retrieve a list of <T> bean passing
	 * IntegrationDataFilter as a parameter. You can pass an
	 * IntegrationPaginatedDataFilter as a parameter in order to perform counts
	 * related to remote pagination. The IntegrationDataFilter allows you to
	 * apply very simple conditions over <T> beans in order to filter the
	 * results to be retrieved.
	 * 
	 * @param filter
	 * @return List<T> The list of <T> beans retrieved
	 * @throws Exception
	 */
	List<T> retrieveList(IntegrationDataFilter filter) throws Exception;

	/**
	 * This methods allows you to perform retrieval JPA operations providing
	 * direct access to the EntityManager by means a QueryJpaCallback<T> object
	 * for <T> beans. If you want predefined standard hints, set
	 * QueryJPACallback "standardHints" boolean parameter to true.
	 * 
	 * @param query
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	List<T> retrieveList(QueryJpaCallback<T> query) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of <T>
	 * beans.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	List<T> retrieveList(String queryString) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of <T>
	 * beans and passing a map of parameters
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	List<T> retrieveList(String queryString, Map<String, Object> parameters)
			throws Exception;

	/**
	 * This method allows you to execute a JPQL query returning a List of <T>
	 * beans and passing a map of parameters and a maximum number of objects
	 * (<T> beans) and the position of the first object (<T> bean) of the group
	 * of objectes to be retrieved. This method is typically used to perform
	 * remote pagination.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @param maxRecords
	 *            Maximum number of records (<T> beans) to be retrieved
	 * @param firstResult
	 *            Starting position of the first <T> bean of the results to be
	 *            retrieved
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	List<T> retrieveList(final String queryString,
			final Map<String, Object> parameters, final int maxRecords,
			final int firstResult) throws Exception;

	/**
	 * This method allows you to retrieve objects according to a JPQL query
	 * byName and the set of parameters the query requires. Queries byName must
	 * be added in your orm.xml file
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 *            The set of parameters the query requires
	 * @return List The list containing all the objects your JPQL query
	 *         retrieves.
	 * @throws Exception
	 */
	List<T> retrieveList(final String queryName, final Object... values)
			throws Exception;
	/**
	 * This method allows you to retrieve objects according to a JPQL query
	 * byName, set of parameters the query requires and pagination attributes. 
	 * Queries byName must be added in your orm.xml file
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param maxRecords The number of records to retrieve
	 * @param firstResult The index of the first record to retrieve
	 * @param values
	 *            The set of parameters the query requires
	 * @return List The list containing all the objects your JPQL query
	 *         retrieves.
	 * @throws Exception
	 */
	List<T> retrieveListPagging(String queryName, int maxRecords, int firstResult, 
			final Object... values) throws Exception;

	/**
	 * This methods allows you to perform any JPA operation providing direct
	 * access to the EntityManager by means a QueryJpaCallback<Object[]>
	 * returning <Object[]>. Use this method when you need to return directly
	 * objects that does not match neither the type of the managed objects of
	 * your repository <T> a result of your query nor a "resultType". A typical
	 * usage is when you do not want to retrieve a full object or a combination
	 * of different attributes of different objects involved in the query or
	 * just calculated fields. In summary, use this method when you do not have
	 * an IntegrationBean or "resultType" to act as a container of the data
	 * returned by the query. In this case you get directly a List with an array
	 * of objects including the selected data. Of course, in this case, you are
	 * responsible to parse this data to objects so do not use this method if
	 * you have an specific object to hold the data.
	 * 
	 * @param query
	 * @return List<Object[]> The list of non typed data to be retrieved
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayList(QueryJpaCallback<Object[]> query)
			throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of
	 * <Object[]>. Use this method when you need to return directly objects that
	 * does not match neither the type of the managed objects of your repository
	 * <T> a result of your query nor a "resultType". A typical usage is when
	 * you do not want to retrieve a full object or a combination of different
	 * attributes of different objects involved in the query or just calculated
	 * fields. In summary, use this method when you do not have an
	 * IntegrationBean or "resultType" to act as a container of the data
	 * returned by the query. In this case you get directly a List with an array
	 * of objects including the selected data. Of course, in this case, you are
	 * responsible to parse this data to objects so do not use this method if
	 * you have an specific object to hold the data.
	 * 
	 * @param query
	 *            The JPQL query
	 * @return List<Object[]> The list of non typed data to be retrieved
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayList(final String queryString)
			throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of
	 * <Object[]> and a map of parameters. Use this method when you need to
	 * return directly objects that does not match neither the type of the
	 * managed objects of your repository <T> a result of your query nor a
	 * "resultType". A typical usage is when you do not want to retrieve a full
	 * object or a combination of different attributes of different objects
	 * involved in the query or just calculated fields. In summary, use this
	 * method when you do not have an IntegrationBean or "resultType" to act as
	 * a container of the data returned by the query. In this case you get
	 * directly a List with an array of objects including the selected data. Of
	 * course, in this case, you are responsible to parse this data to objects
	 * so do not use this method if you have an specific object to hold the
	 * data.
	 * 
	 * @param query
	 *            The JPQL query
	 * @param parameters
	 * @return List<Object[]> The list of non typed data to be retrieved
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayList(final String queryString,
			final Map<String, Object> parameters) throws Exception;

	/**
	 * This method allows you to execute a JPQL query returning a List of
	 * <Object[]> passing a map of parameters and a maximum number of objects to
	 * be retrieved and the position of the first object of the group of objects
	 * to be retrieved. This method is typically used to perform remote
	 * pagination. Use this method when you need to return directly objects that
	 * does not match neither the type of the managed objects of your repository
	 * <T> a result of your query nor a "resultType". A typical usage is when
	 * you do not want to retrieve a full object or a combination of different
	 * attributes of different objects involved in the query or just calculated
	 * fields. In summary, use this method when you do not have an
	 * IntegrationBean or "resultType" to act as a container of the data
	 * returned by the query. In this case you get directly a List with an array
	 * of objects including the selected data. Of course, in this case, you are
	 * responsible to parse this data to objects so do not use this method if
	 * you have an specific object to hold the data.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @param maxRecords
	 *            Maximum number of records (<T> beans) to be retrieved
	 * @param firstResult
	 *            Starting position of the first <T> bean of the results to be
	 *            retrieved
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayList(final String queryString,
			final Map<String, Object> parameters, final int maxRecords,
			final int firstResult) throws Exception;

	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires, returning a List of
	 * <Object[]> . Queries byName must be added in your orm.xml file. Use this
	 * method when you need to return directly objects that does not match
	 * neither the type of the managed objects of your repository <T> a result
	 * of your query nor a "resultType". A typical usage is when you do not want
	 * to retrieve a full object or a combination of different attributes of
	 * different objects involved in the query or just calculated fields. In
	 * summary, use this method when you do not have an IntegrationBean or
	 * "resultType" to act as a container of the data returned by the query. In
	 * this case you get directly a List with an array of objects including the
	 * selected data. Of course, in this case, you are responsible to parse this
	 * data to objects so do not use this method if you have an specific object
	 * to hold the data.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return List<Object[]> List of Object[] retrieved
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayList(final String queryName,
			final Object... values) throws Exception;


	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * , the set of parameters the query requires and pagination attributes, 
	 * returning a List of <Object[]> . Queries byName must be added in your orm.xml 
	 * file. Use this method when you need to return directly objects that does not 
	 * match neither the type of the managed objects of your repository <T> a result
	 * of your query nor a "resultType". A typical usage is when you do not want
	 * to retrieve a full object or a combination of different attributes of
	 * different objects involved in the query or just calculated fields. In
	 * summary, use this method when you do not have an IntegrationBean or
	 * "resultType" to act as a container of the data returned by the query. In
	 * this case you get directly a List with an array of objects including the
	 * selected data. Of course, in this case, you are responsible to parse this
	 * data to objects so do not use this method if you have an specific object
	 * to hold the data.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param maxRecords The number of records to retrieve
	 * @param firstResult The index of the first record to retrieve
	 * @param values
	 * @return List<Object[]> List of Object[] retrieved
	 * @throws Exception
	 */
	List<Object[]> retrieveObjectArrayListPagging(final String queryName, final int maxRecords,
			final int firstResult, final Object... values) throws Exception;
	
	/**
	 * This methods allows you to perform any JPA operation providing direct
	 * access to the EntityManager by means a QueryJpaCallback<U> returning <U>
	 * beans that extend ResultIntegrationBean. Use this method when you need to
	 * return as IntegrationBeans that does not match the type of the managed
	 * objects of your repository <T> a result of your query. You can create
	 * your own ResultIntegrationBean just extending this class.
	 * 
	 * @param query
	 * @return <U extends ResultIntegrationBean> List<U> The list of
	 *         ResultIntegrationBean to retrieve
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			QueryJpaCallback<U> query) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of <T>
	 * beans returning <U> beans that extend ResultIntegrationBean. Use this
	 * method when you need to return as IntegrationBeans that does not match
	 * the type of the managed objects of your repository <T> a result of your
	 * query. You can create your own ResultIntegrationBean just extending this
	 * class.
	 * 
	 * @param query
	 *            The JPQL query
	 * @return <U extends ResultIntegrationBean> List<U> The list of
	 *         ResultIntegrationBean to retrieve
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			String queryString) throws Exception;

	/**
	 * This methods allows you to execute a JPQL query returning a List of <T>
	 * beans returning <U> beans that extend ResultIntegrationBean. Use this
	 * method when you need to return as IntegrationBeans that does not match
	 * the type of the managed objects of your repository <T> a result of your
	 * query. You can create your own ResultIntegrationBean just extending this
	 * class.
	 * 
	 * @param query
	 *            The JPQL query
	 * @param parameters
	 * @return <U extends ResultIntegrationBean> List<U> The list of
	 *         ResultIntegrationBean to retrieve
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			String queryString, Map<String, Object> parameters)
			throws Exception;

	/**
	 * This method allows you to execute a JPQL query returning a List of <T>
	 * beans returning <U> beans that extend ResultIntegrationBean and passing a
	 * map of parameters and a maximum number of objects (<U>
	 * ResultIntegrationbeans) and the position of the first object (<U>
	 * ResultIntegrationBean) of the group of objects to be retrieved. Use this
	 * method when you need to return as IntegrationBeans that does not match
	 * the type of the managed objects of your repository <T> a result of your
	 * query. This method is typically used to perform remote pagination.
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @param maxRecords
	 *            Maximum number of records (<T> beans) to be retrieved
	 * @param firstResult
	 *            Starting position of the first <T> bean of the results to be
	 *            retrieved
	 * @return List<T> List of retrieved <T> beans
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryString, final Map<String, Object> parameters,
			final int maxRecords, final int firstResult) throws Exception;

	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires, returning a List of <T>
	 * beans returning <U> beans that extend ResultIntegrationBean. Queries
	 * byName must be added in your orm.xml file. Use this method when you need
	 * to return as IntegrationBeans that does not match the type of the managed
	 * objects of your repository <T> a result of your query.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryName, final Object... values) throws Exception;
	
	/**
	 * This method allows you to perform counts according to a JPQL query byName
	 * and the set of parameters the query requires, returning a List of <T>
	 * beans returning <U> beans that extend ResultIntegrationBean. Queries
	 * byName must be added in your orm.xml file. Use this method when you need
	 * to return as IntegrationBeans that does not match the type of the managed
	 * objects of your repository <T> a result of your query.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param maxRecords The number of records to retrieve
	 * @param firstResult The index of the first record to retrieve
	 * @param values
	 * @return
	 * @throws Exception
	 */
	<U extends ResultIntegrationBean> List<U> retrieveResultTypeListPagging(
			final String queryName, final int maxRecords,
			final int firstResult, final Object... values) throws Exception;
	
	/**
	 * Executes an update JPQL query
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @throws Exception
	 */
	void update(String queryString) throws Exception;

	/**
	 * Executes an update JPQL query supporting a map of parameters
	 * 
	 * @param queryString
	 *            The JPQL query
	 * @param parameters
	 * @throws Exception
	 */
	void update(String queryString, Map<String, Object> parameters)
			throws Exception;

	/**
	 * Executes an update JPQL query supporting a map of parameters
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param parameters
	 * @throws Exception
	 */

	/**
	 * Updates a <T> bean according to a JPQL query string. Use this method when
	 * your query.
	 * 
	 * @param queryName
	 *            Name of the query in your orm.xml file
	 * @param values
	 * @return
	 */
	void update(final String queryName, final Object... values)
			throws Exception;

	/**
	 * This methods allows you to perform any JPA update providing direct access
	 * to the EntityManager by means a QueryJpaCallback<T>
	 * 
	 * @param query
	 * @return void
	 * @throws Exception
	 */
	void update(UpdateJpaCallback<T> query) throws Exception;

    /**
     * Wrapper of EntityManager unwrap method that provides the JPA provider
     * underlying session
     * @see javax.persistence.EntityManager#unwrap(Class)
     */
    <S> S unwrap(Class<S> cls);
}