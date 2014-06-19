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
package org.appverse.web.framework.backend.persistence.services.integration.impl.live;

import org.apache.commons.beanutils.PropertyUtils;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.model.integration.ResultIntegrationBean;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.persistence.services.integration.IJPAPersistenceService;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.PersistenceMessageBundle;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.UpdateJpaCallback;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * This class provides an API based on Spring <code>JpaTemplate</code> for basic
 * JPA database operations. It supports DataFilters for remote pagination
 * support and strictly very easy ordering and filtering conditions. Take into
 * account that the use of this service is restricted to the aforementioned
 * cases and more complex queries need to be built by means a custom JPA query.
 * 
 * This service support only support automatic generation of JPA queries
 * filtering by <T> object first level attributes that would NOT imply express
 * JOIN indication in the generated JPQL and only allows sorting by first level
 * attributes in the root <T> object.
 * 
 * @param <T>
 *            Abstract integration Bean the persistence service will deal with
 */
public class JPAPersistenceService<T extends AbstractIntegrationBean> extends
		AbstractIntegrationService<T> implements IJPAPersistenceService<T> {

	@Value("${db.pkName}")
	// IMPORTANT NOTE: Take care that Eclipse "saveActions" (or the IDE you are
	// using) is not adding "final" automatically to this field.
	// This property is annotated so that Maven replaces the real value
	// according to your setup in a property files.
	// If the field is declared as "final", Maven it is not able to override
	// this value.
	private String BEAN_PK_NAME = "pk";

	@PersistenceContext
	private EntityManager em;

	@AutowiredLogger
	private static Logger logger;

	private StringBuilder buildQueryString(final IntegrationDataFilter filter) {
		return buildQueryString(filter, false);
	}

	private StringBuilder buildQueryString(final IntegrationDataFilter filter,
			final boolean isCount) {

		if (filter != null) {
			checkMaxFilterConditionsColumnsDeep(filter);
			checkMaxFilterColumnsToSortDeep(filter);
		}

		final StringBuilder queryString = new StringBuilder();

		if (isCount) {
			// We are interested just in the total rows number
			queryString.append("select count(distinct p) from ")
					.append(getClassP().getSimpleName()).append(" p");
		} else {
			// It is a select
			queryString.append("select p from ")
					.append(getClassP().getSimpleName()).append(" p");
		}

		boolean whereClauseAdded = false;
		if (filter != null) {
			if (filter.getColumns() != null) {
				for (int i = 0; i < filter.getColumns().size(); i++) {
					if (i == 0) {
						queryString.append(" where ");
						whereClauseAdded = true;
					} else {
						// Add "AND" or "OR" condition
						if (filter.getBooleanConditions() != null
								&& filter.getBooleanConditions().size() > i
								&& filter.getBooleanConditions().get(i) != null) {
							queryString
									.append(" ")
									.append(filter.getBooleanConditions()
											.get(i)).append(" ");
						} else {
							queryString
									.append(" ")
									.append(filter
											.getDefaultConditionOperation())
									.append(" ");
						}
					}
					String symbol = "=";
					if (filter.getNegates() != null
							&& filter.getNegates().size() > i
							&& filter.getNegates().get(i) != null
							&& filter.getNegates().get(i)) {
						symbol = "!=";
					} else if (filter.getLikes() != null
							&& filter.getLikes().size() > i
							&& filter.getLikes().get(i) != null
							&& filter.getLikes().get(i)) {
						symbol = "like";
					} else if (filter.getEqualsOrGreaterThan() != null
							&& filter.getEqualsOrGreaterThan().size() > i
							&& filter.getEqualsOrGreaterThan().get(i) != null
							&& filter.getEqualsOrGreaterThan().get(i)
									.booleanValue()) {
						symbol = ">=";
					} else if (filter.getEqualsOrLessThan() != null
							&& filter.getEqualsOrLessThan().size() > i
							&& filter.getEqualsOrLessThan().get(i) != null
							&& filter.getEqualsOrLessThan().get(i)
									.booleanValue()) {
						symbol = "<=";
					}
					boolean ignoreCase = filter.getIgnoreCase() != null
							&& filter.getIgnoreCase().size() > i
							&& filter.getIgnoreCase().get(i) != null
							&& filter.getIgnoreCase().get(i);

					// build the query string
					if (ignoreCase) {
						queryString.append("upper(");
					}
					queryString.append("p.").append(filter.getColumns().get(i));

					if (ignoreCase) {
						queryString.append(")");
					}
					queryString.append(" ").append(symbol).append(" ");

					if (ignoreCase) {
						queryString.append("upper(");
					}
					queryString.append("?").append(i + 1);
					if (ignoreCase) {
						queryString.append(")");
					}
				}
			}

			// Special treatment for IS NULL
			if (filter.getBooleanConditionsIsNull() != null) {
				if (filter.getColumnsIsNull() != null) {
					for (int i = 0; i < filter.getColumnsIsNull().size(); i++) {
						if (i == 0 && whereClauseAdded == false) {
							queryString.append(" where ");
							whereClauseAdded = true;
						} else {
							// Add "AND" or "OR" condition
							if (filter.getBooleanConditionsIsNull() != null
									&& filter.getBooleanConditionsIsNull()
											.size() > i
									&& filter.getBooleanConditionsIsNull().get(
											i) != null) {
								queryString
										.append(" ")
										.append(filter
												.getBooleanConditionsIsNull()
												.get(i)).append(" ");
							} else {
								queryString
										.append(" ")
										.append(filter
												.getDefaultConditionOperation())
										.append(" ");
							}
						}
						String symbol = "";
						if (filter.getNegates() != null
								&& filter.getNegatesIsNull().size() > i
								&& filter.getNegatesIsNull().get(i) != null) {
							if (filter.getNegatesIsNull().get(i) == false) {
								symbol = " IS NULL ";
							} else {
								symbol = " IS NOT NULL ";
							}
						}

						// build the query string
						queryString.append("p.").append(
								filter.getColumnsIsNull().get(i));
						queryString.append(" ").append(symbol).append(" ");
					}
				}
			}

			// Sorting does not apply for counts
			if (!isCount && filter.getColumnsToSort() != null) {
				for (int i = 0; i < filter.getColumnsToSort().size(); i++) {
					if (i == 0) {
						queryString.append(" order by ");
					} else {
						queryString.append(", ");
					}
					queryString.append("p.")
							.append(filter.getColumnsToSort().get(i))
							.append(" ")
							.append(filter.getSortingDirections().get(i));
				}
			}
		}
		return queryString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #refresh(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@Override
	public boolean contains(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REFRESH);
		return em.contains(beanP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#count()
	 */
	@Override
	public int count() throws Exception {
		final StringBuilder queryString = buildQueryString(null, true);
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
				queryString.toString(), true);
		return query.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #count(org.appverse.web.framework.backend.api.model.
	 * integration.IntegrationDataFilter)
	 */
	@Override
	public int count(final IntegrationDataFilter filter) throws Exception {
		final StringBuilder queryString = buildQueryString(filter, true);
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
				queryString.toString(), true);
		query.setIndexedParameters(filter.getValues().toArray());
		return query.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #count(org.appverse.web.framework.backend.persistence
	 * .services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public int count(QueryJpaCallback<T> query) throws Exception {
		return query.countInJpa(em);
	}

	@Override
	public int count(String queryString) throws Exception {
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryString);
		return queryJpaCallback.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#count(java.lang.String, java.util.Map)
	 */
	@Override
	public int count(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryString);
		queryJpaCallback.setNamedParameters(parameters);
		return queryJpaCallback.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#count(java.lang.String, java.lang.Object[])
	 */
	@Override
	public int count(final String queryName, final Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.countInJpa(em);
	}

	@Override
	public int countObjectArray(QueryJpaCallback<Object[]> query)
			throws Exception {
		return query.countInJpa(em);
	}

	@Override
	public int countObjectArray(String queryString) throws Exception {
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryString);
		return queryJpaCallback.countInJpa(em);
	}

	@Override
	public int countObjectArray(String queryString,
			Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryString);
		queryJpaCallback.setNamedParameters(parameters);
		return queryJpaCallback.countInJpa(em);
	}

	@Override
	public int countObjectArray(String queryName, Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		final QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #countResultType(org.appverse.web.framework.backend.
	 * persistence.services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public <U extends ResultIntegrationBean> int countResultType(
			QueryJpaCallback<U> query) throws Exception {
		return query.countInJpa(em);
	}

	@Override
	public <U extends ResultIntegrationBean> int countResultType(
			String queryString) throws Exception {
		final QueryJpaCallback<U> queryJpaCallback = new QueryJpaCallback<U>(
				queryString);
		return queryJpaCallback.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#countResultType(java.lang.String, java.util.Map)
	 */
	@Override
	public <U extends ResultIntegrationBean> int countResultType(
			final String queryString, final Map<String, Object> parameters)
			throws Exception {
		final QueryJpaCallback<U> queryJpaCallback = new QueryJpaCallback<U>(
				queryString);
		queryJpaCallback.setNamedParameters(parameters);
		return queryJpaCallback.countInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#countResultType(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public <U extends ResultIntegrationBean> int countResultType(
			final String queryName, final Object... values) throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		final QueryJpaCallback<U> queryJpaCallback = new QueryJpaCallback<U>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.countInJpa(em);
	}

	private void checkMaxFilterColumnsToSortDeep(
			final IntegrationDataFilter filter) {
		// Get maximum deep in "columnsToSort"
		int columnsDeep = 0;
		for (String columnPath : filter.getColumnsToSort()) {
			columnsDeep = Math.max(
					StringUtils.countOccurrencesOf(columnPath, "."),
					columnsDeep);
		}
		if (columnsDeep > 0) {
			StringBuffer e = new StringBuffer();
			e.append(
					PersistenceMessageBundle.MSG_DAO_INVALID_FILTER_ORDERING_COLUMNS)
					.append(getClassP().getSimpleName())
					.append(".")
					.append(filter.toString())
					.append(".")
					.append(PersistenceMessageBundle.MSG_DAO_INVALID_FILTER_ADVIDE);
			logger.error(e.toString());
			throw new PersistenceException(e.toString());
		}
	}

	private void checkMaxFilterConditionsColumnsDeep(
			final IntegrationDataFilter filter) {
		int columnsDeep = 0;
		// Get maximum deep in "columns"
		if (filter.getColumns() != null) {
			for (String columnPath : filter.getColumns()) {
				columnsDeep = Math.max(
						StringUtils.countOccurrencesOf(columnPath, "."),
						columnsDeep);
			}
		}
		// Get maximum deep in "columnsIsNull"
		if (filter.getColumnsIsNull() != null) {
			for (String columnPath : filter.getColumnsIsNull()) {
				columnsDeep = Math.max(
						StringUtils.countOccurrencesOf(columnPath, "."),
						columnsDeep);
			}
		}
		if (columnsDeep > 1) {
			StringBuffer e = new StringBuffer();
			e.append(
					PersistenceMessageBundle.MSG_DAO_INVALID_FILTER_CONDITIONS_COLUMNS)
					.append(getClassP().getSimpleName())
					.append(".")
					.append(filter.toString())
					.append(".")
					.append(PersistenceMessageBundle.MSG_DAO_INVALID_FILTER_ADVIDE);
			logger.error(e.toString());
			throw new PersistenceException(e.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #delete(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@Override
	public void delete(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REMOVE);
		em.remove(beanP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#deleteAll()
	 */
	@Override
	public void deleteAll() throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REMOVEALL, getClassP());

		final StringBuilder queryString = new StringBuilder();
		queryString.append("delete from ").append(getClassP().getSimpleName())
				.append(" p ");
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #refresh(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@Override
	public void detach(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REFRESH);
		em.detach(beanP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#flush()
	 */
	@Override
	public void flush() throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_FLUSH);
		em.flush();
	}

	@SuppressWarnings("unchecked")
	private Class<T> getClassP() throws PersistenceException {

		Class<T> classP = null;
		final Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			final ParameterizedType pType = (ParameterizedType) type;
			if (pType.getActualTypeArguments()[0] instanceof Class) {
				classP = (Class<T>) pType.getActualTypeArguments()[0];
			} else {
				logger.error(
						PersistenceMessageBundle.MSG_DAO_RETRIEVEBY_ERROR_PARAMETERTYPE,
						this.getClass());
				throw new PersistenceException(
						this.getClass().getSimpleName()
								+ PersistenceMessageBundle.MSG_DAO_RETRIEVEBY_ERROR_PARAMETERTYPE);
			}
		} else {
			logger.error(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEBY_ERROR_PARAMETERPATTERN,
					this.getClass());
			throw new PersistenceException(
					this.getClass()
							+ PersistenceMessageBundle.MSG_DAO_RETRIEVEBY_ERROR_PARAMETERPATTERN);

		}

		return classP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #persist(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@Override
	public long persist(final T bean) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_PERSIST);
		long beanId = -1;
		try {
			beanId = (Long) PropertyUtils.getProperty(bean, this.BEAN_PK_NAME);
		} catch (final Exception ex) {
			logger.error(PersistenceMessageBundle.MSG_DAO_PERSIST_IDNOTFOUND,
					getClass().getName());
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_PERSIST_IDNOTFOUND);
		}
		if (beanId == 0) {
			// new
			em.persist(bean);
			beanId = (Long) PropertyUtils.getProperty(bean, this.BEAN_PK_NAME);
		} else {
            Object jpaProviderDelegate = em.getDelegate();

            /* The following treatment is specific for Hibernate.
             * There is no problem with detached objects but at the moment that a JPA entity is attached to the
             * hibernate session, the version is cached. Does not matter that we override the cached version value
             * with the one coming from the business layer (the one kept in an object passed from a front end,
             * for instance, to check if the bean is stale or not) that the version kept when Hibernate generates the
             * update query is the one from the cache.
             * This implies that the version id's always match and so no OptimisticLockException is thrown.
             * EclipseLink handles this properly. Hibernate and JPA doc says that the application should not modify
             * the @Version annotated fields but in practice is quite common to retrieve an entity from the session
             * (it will be attached) and then override only the data that has been changed in the front end before
             * saving.
             * Even though detaching the object might have a small impact in performance we prefer to ensure that persist
             * method support properly JPA optimistic locking when JPA Provider is Hibernate.
             * The reason why we don't use "instance of" here is because depending on the Appverse Web persistence
             * module you are using (Hibernate or EclipseLink) you will have one classes or another but never both at
             * the same time, so using "instance of" is not a solution.
             * Another improvement that has been considered is to check if the annotation @Version is present
             * in one field or method of the class but this would imply using reflexion and visit every method
             * to see if the annotation is present or not. We think depending on the case this can affect performance
             * even more than detaching directly the object always before saving with Hibernate.
             */
            if (jpaProviderDelegate.getClass().getName().contains("hibernate")){
                em.detach(bean);
            }
			// update
			em.merge(bean);
		}
		PropertyUtils.setProperty(bean, this.BEAN_PK_NAME, beanId);
		return beanId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #refresh(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@Override
	public void refresh(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REFRESH);
		em.refresh(beanP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieve(org.appverse.web.framework.backend.api.model
	 * .integration.IntegrationDataFilter)
	 */
	@Override
	public T retrieve(final IntegrationDataFilter filter) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED,
				new Object[] { getClassP(), filter });
		try {
			final StringBuilder queryString = buildQueryString(filter);
			final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
					queryString.toString());
			if (filter != null) {
				query.setIndexedParameters(filter.getValues().toArray());
				if (filter instanceof IntegrationPaginatedDataFilter) {
					IntegrationPaginatedDataFilter iFilter = (IntegrationPaginatedDataFilter) filter;
					query.setFirstResult(iFilter.getOffset());
					query.setMaxRecords(iFilter.getLimit());
				}
			}
			return query.doInJpaSingleResult(em);
		} catch (final Exception e) {
			logger.error(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR,
					new Object[] { filter.toString(), getClassP() });
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR_P1
							+ filter.toString()
							+ PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR_P2
							+ getClassP(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieve(long)
	 */
	@Override
	public T retrieve(final long pk) throws Exception {
		final Class<T> classP = getClassP();
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYPK,
				new Object[] { classP, this.BEAN_PK_NAME });
		try {
			return em.find(classP, pk);
		} catch (final Exception e) {
			logger.error(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYPK_ERROR,
					new Object[] { classP, this.BEAN_PK_NAME });
			logger.error(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYPK_ERROR, e);
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEBYPK_ERROR
							+ this.BEAN_PK_NAME);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieve(org.appverse.web.framework.backend.persistence
	 * .services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public T retrieve(QueryJpaCallback<T> query) throws Exception {
		return query.doInJpaSingleResult(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieve(java.lang.String)
	 */
	@Override
	public T retrieve(final String queryString) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString);
		return query.doInJpaSingleResult(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieve(java.lang.String, java.util.Map)
	 */
	@Override
	public T retrieve(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString);
		query.setNamedParameters(parameters);
		return query.doInJpaSingleResult(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieve(java.lang.String, java.lang.Object[])
	 */
	@Override
	public T retrieve(final String queryName, final Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.doInJpaSingleResult(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieve(org.appverse.web.framework.backend.api.model
	 * .integration.AbstractIntegrationBean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T retrieve(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN,
				new Object[] { beanP, this.BEAN_PK_NAME });
		Object beanId;
		try {
			beanId = PropertyUtils.getProperty(beanP, this.BEAN_PK_NAME);
			return (T) em.find(beanP.getClass(), beanId);
		} catch (final Exception e) {
			logger.error(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN,
					new Object[] { beanP, this.BEAN_PK_NAME });
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList()
	 */
	@Override
	public List<T> retrieveList() throws Exception {
		IntegrationDataFilter integrationDataFilter = null;
		return retrieveList(integrationDataFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieveList(org.appverse.web.framework.backend.api
	 * .model.integration.IntegrationDataFilter)
	 */
	@Override
	public List<T> retrieveList(final IntegrationDataFilter filter)
			throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED,
				new Object[] { getClassP(), filter });
		try {
			final StringBuilder queryString = buildQueryString(filter);
			final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
					queryString.toString());
			if (filter != null) {
				query.setIndexedParameters(filter.getValues().toArray());
				if (filter instanceof IntegrationPaginatedDataFilter) {
					IntegrationPaginatedDataFilter iFilter = (IntegrationPaginatedDataFilter) filter;
					query.setFirstResult(iFilter.getOffset());
					query.setMaxRecords(iFilter.getLimit());
				}
			}
			return query.doInJpa(em);
		} catch (final Exception e) {
			logger.error(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR,
					new Object[] { filter.toString(), getClassP() });
			logger.error(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR,
					e);
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR_P1
							+ filter.toString()
							+ PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED_ERROR_P2
							+ getClassP(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieveList(org.appverse.web.framework.backend.persistence
	 * .services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public List<T> retrieveList(QueryJpaCallback<T> query) throws Exception {
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList(java.lang.String)
	 */
	@Override
	public List<T> retrieveList(final String queryString) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList(java.lang.String, java.util.Map)
	 */
	@Override
	public List<T> retrieveList(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		query.setNamedParameters(parameters);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList(java.lang.String, java.util.Map, int,
	 * int)
	 */
	@Override
	public List<T> retrieveList(final String queryString,
			final Map<String, Object> parameters, final int maxRecords,
			final int firstResult) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		query.setNamedParameters(parameters);
		query.setFirstResult(firstResult);
		query.setMaxRecords(maxRecords);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<T> retrieveList(final String queryName, final Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveList(java.lang.String, int, int, java.lang.Object[])
	 */
	@Override
	public List<T> retrieveListPagging(final String queryName, final int maxRecords,
			final int firstResult, final Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<T> queryJpaCallback = new QueryJpaCallback<T>(
				queryObject);
		queryJpaCallback.setFirstResult(firstResult);
		queryJpaCallback.setMaxRecords(maxRecords);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieveObjectArrayList(org.appverse.web.framework.
	 * backend.persistence.services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public List<Object[]> retrieveObjectArrayList(
			QueryJpaCallback<Object[]> query) throws Exception {
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveObjectArrayList(java.lang.String)
	 */
	@Override
	public List<Object[]> retrieveObjectArrayList(final String queryString)
			throws Exception {
		final QueryJpaCallback<Object[]> query = new QueryJpaCallback<Object[]>(
				queryString, true);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveObjectArrayList(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public List<Object[]> retrieveObjectArrayList(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<Object[]> query = new QueryJpaCallback<Object[]>(
				queryString, true);
		query.setNamedParameters(parameters);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveObjectArrayList(java.lang.String,
	 * java.util.Map, int, int)
	 */
	@Override
	public List<Object[]> retrieveObjectArrayList(final String queryString,
			final Map<String, Object> parameters, final int maxRecords,
			final int firstResult) throws Exception {
		final QueryJpaCallback<Object[]> query = new QueryJpaCallback<Object[]>(
				queryString, true);
		query.setNamedParameters(parameters);
		query.setFirstResult(firstResult);
		query.setMaxRecords(maxRecords);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveObjectArrayList(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public List<Object[]> retrieveObjectArrayList(final String queryName,
			final Object... values) throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<Object[]> queryJpaCallback = new QueryJpaCallback<Object[]>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.doInJpa(em);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveObjectArrayList(java.lang.String, int, int,
	 * java.lang.Object[])
	 */
	@Override
	public List<Object[]> retrieveObjectArrayListPagging(String queryName,
			int maxRecords, int firstResult, Object... values) throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<Object[]> queryJpaCallback = new QueryJpaCallback<Object[]>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		queryJpaCallback.setFirstResult(firstResult);
		queryJpaCallback.setMaxRecords(maxRecords);
		return queryJpaCallback.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #retrieveResultType(org.appverse.web.framework.backend
	 * .persistence.services.integration.helpers.QueryJpaCallback)
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			QueryJpaCallback<U> query) throws Exception {
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveResultType(java.lang.String)
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryString) throws Exception {
		final QueryJpaCallback<U> query = new QueryJpaCallback<U>(queryString,
				true);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveResultType(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryString, final Map<String, Object> parameters)
			throws Exception {
		final QueryJpaCallback<U> query = new QueryJpaCallback<U>(queryString,
				true);
		query.setNamedParameters(parameters);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveResultType(java.lang.String,
	 * java.util.Map, int, int)
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryString, final Map<String, Object> parameters,
			final int maxRecords, final int firstResult) throws Exception {
		final QueryJpaCallback<U> query = new QueryJpaCallback<U>(queryString,
				true);
		query.setNamedParameters(parameters);
		query.setFirstResult(firstResult);
		query.setMaxRecords(maxRecords);
		return query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveResultType(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeList(
			final String queryName, final Object... values) throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<U> queryJpaCallback = new QueryJpaCallback<U>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		return queryJpaCallback.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#retrieveResultType(java.lang.String, int, int,
	 * java.lang.Object[])
	 */
	@Override
	public <U extends ResultIntegrationBean> List<U> retrieveResultTypeListPagging(
			String queryName, int maxRecords, int firstResult, Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		QueryJpaCallback<U> queryJpaCallback = new QueryJpaCallback<U>(
				queryObject);
		queryJpaCallback.setIndexedParameters(values);
		queryJpaCallback.setFirstResult(firstResult);
		queryJpaCallback.setMaxRecords(maxRecords);		
		return queryJpaCallback.doInJpa(em);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#update(java.lang.String)
	 */
	@Override
	public void update(final String queryString) throws Exception {
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#update(java.lang.String, java.util.Map)
	 */
	@Override
	public void update(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		query.setNamedParameters(parameters);
		query.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService#update(java.lang.String, Object...)
	 */
	@Override
	public void update(final String queryName, final Object... values)
			throws Exception {
		Query queryObject = em.createNamedQuery(queryName);
		final UpdateJpaCallback<T> updateJpaCallback = new UpdateJpaCallback<T>(
				queryObject);
		updateJpaCallback.setIndexedParameters(values);
		updateJpaCallback.doInJpa(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.appverse.web.framework.backend.persistence.services.integration.
	 * IJPAPersistenceService
	 * #update(org.appverse.web.framework.backend.persistence
	 * .services.integration.helpers.UpdateJpaCallback)
	 */
	@Override
	public void update(UpdateJpaCallback<T> query) throws Exception {
		query.doInJpa(em);
	}


    /**
     * Wrapper of EntityManager unwrap method that provides the JPA provider
     * underlying session
     * @see javax.persistence.EntityManager#unwrap(Class)
     */
    @Override
    public <S> S unwrap(Class<S> cls) {
        return em.unwrap(cls);
    }
}
