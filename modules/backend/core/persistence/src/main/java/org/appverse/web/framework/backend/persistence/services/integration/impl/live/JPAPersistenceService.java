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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.apache.commons.beanutils.PropertyUtils;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.api.model.integration.IntegrationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.api.services.integration.NotUniqueResultFoundException;
import org.appverse.web.framework.backend.persistence.services.integration.IJPAPersistenceService;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.PersistenceMessageBundle;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallback;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.QueryJpaCallbackHint;
import org.appverse.web.framework.backend.persistence.services.integration.helpers.UpdateJpaCallback;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.util.StringUtils;

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

	private JpaTemplate jpaTemplate;

	@AutowiredLogger
	private static Logger logger;


	private StringBuilder buildQueryString(final IntegrationDataFilter filter) {
		return buildQueryString(filter, false);
	}

	private StringBuilder buildQueryString(
			final IntegrationDataFilter filter, final boolean isCount) {

		if (filter != null) {
			checkMaxFilterConditionsColumnsDeep(filter);
			checkMaxFilterColumnsToSortDeep(filter);
		}

		final StringBuilder queryString = new StringBuilder();

		if (isCount) {
			// We are interested just in the total rows number
			queryString.append("select count(distinct(p)) from ")
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

	/**
	 * Create a JpaTemplate for the given EntityManagerFactory. Only invoked if
	 * populating the DAO with a EntityManagerFactory reference!
	 * <p>
	 * Can be overridden in subclasses to provide a JpaTemplate instance with
	 * different configuration, or a custom JpaTemplate subclass.
	 *
	 * @param entityManagerFactory
	 *            the JPA EntityManagerFactory to create a JpaTemplate for
	 * @return the new JpaTemplate instance
	 * @see #setEntityManagerFactory
	 */
	private JpaTemplate createJpaTemplate(
			final EntityManagerFactory entityManagerFactory) {
		return new JpaTemplate(entityManagerFactory);
	}

	@Override
	public void delete(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REMOVE);
		this.jpaTemplate.remove(beanP);
	}

	@Override
	public void deleteAll() throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_REMOVEALL, getClassP());

		final StringBuilder queryString = new StringBuilder();
		queryString.append("delete from ").append(getClassP().getSimpleName())
				.append(" p ");
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		this.jpaTemplate.execute(query);
	}

	@Override
	public List<T> execute(QueryJpaCallback<T> query) throws Exception {
		final List<T> list = getJpaTemplate().execute(query);
		for (final T item : list) {
			if (item instanceof AbstractIntegrationBean) {
				getJpaTemplate().refresh(item);
			}
		}
		return list;
	}

	@Override
	public List<T> execute(final String queryString) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		final List<T> list = getJpaTemplate().execute(query);
		for (final T item : list) {
			if (item instanceof AbstractIntegrationBean) {
				getJpaTemplate().refresh(item);
			}
		}
		return list;
	}

	@Override
	public List<T> execute(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		query.setNamedParameters(parameters);
		final List<T> list = getJpaTemplate().execute(query);
		for (final T item : list) {
			if (item instanceof AbstractIntegrationBean) {
				getJpaTemplate().refresh(item);
			}
		}
		return list;
	}

	@Override
	public List<T> execute(final String queryString,
			final Map<String, Object> parameters, final int maxRecords,
			final int firstResult) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		query.setNamedParameters(parameters);
		query.setFirstResult(firstResult);
		query.setMaxRecords(maxRecords);
		final List<T> list = getJpaTemplate().execute(query);
		for (final T item : list) {
			if (item instanceof AbstractIntegrationBean) {
				getJpaTemplate().refresh(item);
			}
		}
		return list;
	}

	@Override
	public int executeCount(final IntegrationDataFilter filter)
			throws Exception {
		final StringBuilder queryString = buildQueryString(filter, true);
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
				queryString.toString(), true);
		query.setIndexedParameters(filter.getValues().toArray());
		return query.countInJpa(getJpaTemplate().getEntityManagerFactory()
				.createEntityManager());
	}

	@Override
	public int executeCount(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(queryString,
				true);
		query.setNamedParameters(parameters);
		return query.countInJpa(getJpaTemplate().getEntityManagerFactory()
				.createEntityManager());
	}

	@Override
	public void executeUpdate(final String queryString) throws Exception {
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		this.jpaTemplate.execute(query);
	}

	@Override
	public void executeUpdate(final String queryString,
			final Map<String, Object> parameters) throws Exception {
		final UpdateJpaCallback<T> query = new UpdateJpaCallback<T>(
				queryString.toString());
		query.setNamedParameters(parameters);
		this.jpaTemplate.execute(query);
	}

	@Override
	public void flush() throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_FLUSH);
		this.jpaTemplate.flush();
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

	private JpaTemplate getJpaTemplate() {
		return this.jpaTemplate;
	}

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
			this.jpaTemplate.persist(bean);
			beanId = (Long) PropertyUtils.getProperty(bean, this.BEAN_PK_NAME);
		} else {
			// update
			this.jpaTemplate.merge(bean);
		}
		PropertyUtils.setProperty(bean, this.BEAN_PK_NAME, beanId);
		return beanId;
	}

	@Override
	public void refresh(final T beanP) throws PersistenceException {
		getJpaTemplate().refresh(beanP);
	}

	@Override
	public T retrieve(final IntegrationDataFilter filter) throws Exception {
		final List<T> list = retrieveAll(filter);
		if (list == null || list.size() == 0) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new NotUniqueResultFoundException();
		}
	}

	@Override
	public T retrieve(final long pk) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEBY, pk);
		final Class<T> classP = getClassP();
		return this.jpaTemplate.find(classP, pk);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T retrieve(final T beanP) throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN_ERROR_P2,
				new Object[] { beanP, this.BEAN_PK_NAME });
		Object beanId;
		try {
			beanId = PropertyUtils.getProperty(beanP, this.BEAN_PK_NAME);
			return (T) this.jpaTemplate.find(beanP.getClass(), beanId);
		} catch (final Exception e) {
			logger.error(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN_ERROR_P1,
					this.BEAN_PK_NAME);
			throw new PersistenceException(
					PersistenceMessageBundle.MSG_DAO_RETRIEVEBYBEAN
							+ this.BEAN_PK_NAME);
		}
	}

	@Override
	public List<T> retrieveAll() throws Exception {
		return retrieveAll(new ArrayList<QueryJpaCallbackHint>(), null);
	}

	@Override
	public List<T> retrieveAll(final IntegrationDataFilter filter)
			throws Exception {
		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEFILTERED,
				new Object[] { getClassP(), filter });
		try {
			final StringBuilder queryString = buildQueryString(filter);
			final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
					queryString.toString(), false);
			query.setIndexedParameters(filter.getValues().toArray());

			return this.jpaTemplate.execute(query);
		} catch (final Throwable e) {
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

	@Override
	public List<T> retrieveAll(final IntegrationPaginatedDataFilter filter)
			throws Exception {
		return retrieveAll(new ArrayList<QueryJpaCallbackHint>(), filter);
	}

	private List<T> retrieveAll(List<QueryJpaCallbackHint> hints,
			final IntegrationPaginatedDataFilter filter)
			throws PersistenceException {
		final Class<T> classP = getClassP();

		logger.trace(PersistenceMessageBundle.MSG_DAO_RETRIEVEALL, classP);
		// final List<QueryJpaCallbackParameter> parameters = new
		// ArrayList<QueryJpaCallbackParameter>();
		final StringBuilder queryString = buildQueryString(filter);

		// final StringBuilder queryString = new StringBuilder();
		//
		// queryString.append("select p from ").append(classP.getSimpleName())
		// .append(" p");

		if (hints == null) {
			hints = new ArrayList<QueryJpaCallbackHint>();
		}
		hints.add(QueryJpaCallbackHint.FETCH_SIZE_1024);

		final QueryJpaCallback<T> query = new QueryJpaCallback<T>(
				queryString.toString(), true);
		// query.setNamedParameters(parameters);
		query.setHints(hints);

		if (filter != null) {
			query.setIndexedParameters(filter.getValues().toArray());
			query.setFirstResult(filter.getOffset());
			query.setMaxRecords(filter.getLimit());
		}

		return this.jpaTemplate.execute(query);
	}

	/**
	 * Set the JPA EntityManagerFactory to be used by this DAO. Will
	 * automatically create a JpaTemplate for the given EntityManagerFactory.
	 *
	 * @see #createJpaTemplate
	 */
	@Override
	public final void setEntityManagerFactory(
			final EntityManagerFactory entityManagerFactory) {
		if (this.jpaTemplate == null
				|| entityManagerFactory != this.jpaTemplate
						.getEntityManagerFactory()) {
			this.jpaTemplate = createJpaTemplate(entityManagerFactory);
		}
	}
}