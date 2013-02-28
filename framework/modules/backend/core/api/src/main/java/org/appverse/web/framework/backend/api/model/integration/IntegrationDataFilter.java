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
package org.appverse.web.framework.backend.api.model.integration;

import java.util.ArrayList;

public class IntegrationDataFilter extends AbstractIntegrationBean {

	private static final long serialVersionUID = -9158631576861856869L;

	// This fields go in a group. If a column is added a value has to be added
	// to every array as the stragy is to match by position.
	// All constructors in this class need to respect this.
	// For instance: column 'name', value 'Mickey Mouse', negate 'false', like
	// 'true'.
	// In case negate or like does not apply for the current condition we need
	// to add a null object to keep the positions.
	private ArrayList<String> columns;
	private ArrayList<Object> values;
	private ArrayList<Boolean> negates;
	private ArrayList<Boolean> likes;
	private ArrayList<Boolean> ignoreCase;
	private ArrayList<Boolean> equalsOrGreaterThan;
	private ArrayList<Boolean> equalsOrLessThan;
	private ArrayList<String> booleanConditions;

	// This fields go in a group. If a column is added a value has to be added
	// to every array as the stragy is to match by position.
	// All constructors in this class need to respect this.
	// For instance: columnToSort 'name', sortingDirection 'ASC'
	// In case the sortingDirection is not specified, we will add a default in
	// constructor to keep the positions.
	private ArrayList<String> columnsToSort;
	private ArrayList<String> sortingDirections;

	private String defaultBooleanCondition;

	// This fields go in a group as they do not have the field 'value' and other
	// operators do not apply
	private ArrayList<String> columnsIsNull;
	private ArrayList<Boolean> negatesIsNull;
	private ArrayList<String> booleanConditionsIsNull;

	public static String ASC = "ASC";
	public static String DESC = "DESC";

	public static char WILDCARD_ALL = '%';

	public static char WILDCARD_ONE = '_';

	public static String CONDITION_AND = "AND";
	public static String CONDITION_OR = "OR";

	/**
	 * Default constructor. The default operation to concatenate conditions will
	 * be logic "AND" operation if this is not specified in the condition
	 * explicitly
	 */
	public IntegrationDataFilter() {
		defaultBooleanCondition = CONDITION_AND;
		columns = new ArrayList<String>();
		values = new ArrayList<Object>();
		negates = new ArrayList<Boolean>();
		likes = new ArrayList<Boolean>();
		ignoreCase = new ArrayList<Boolean>();
		columnsToSort = new ArrayList<String>();
		sortingDirections = new ArrayList<String>();
		equalsOrGreaterThan = new ArrayList<Boolean>();
		equalsOrLessThan = new ArrayList<Boolean>();
		booleanConditions = new ArrayList<String>();
		booleanConditionsIsNull = new ArrayList<String>();
		columnsIsNull = new ArrayList<String>();
		negatesIsNull = new ArrayList<Boolean>();
	}

	/**
	 * This constructor allows to specify the default operation to concatenate
	 * conditions (logic "AND" or "OR" operation). All conditions will be added
	 * with this operator unless the condition species the operator to use
	 * explicitly.
	 * 
	 * @param defaultConditionOperation
	 *            default logic operator to concatenate conditions to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public IntegrationDataFilter(final String defaultConditionOperation) {
		this.defaultBooleanCondition = defaultConditionOperation;
		columns = new ArrayList<String>();
		values = new ArrayList<Object>();
		negates = new ArrayList<Boolean>();
		likes = new ArrayList<Boolean>();
		ignoreCase = new ArrayList<Boolean>();
		columnsToSort = new ArrayList<String>();
		sortingDirections = new ArrayList<String>();
		equalsOrGreaterThan = new ArrayList<Boolean>();
		equalsOrLessThan = new ArrayList<Boolean>();
		booleanConditionsIsNull = new ArrayList<String>();
		columnsIsNull = new ArrayList<String>();
		negatesIsNull = new ArrayList<Boolean>();
	}

	/**
	 * This methods allows to add "EqualsOrGreaterThan" conditions: column >=
	 * value
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 */
	public void addEqualsOrGreaterThanCondition(final String column,
			final Object value) {
		addEqualsOrGreaterThanCondition(column, value, defaultBooleanCondition);
	}

	/**
	 * This methods allows to add "EqualsOrGreaterThan" conditions: column >=
	 * value specifying the logic operator ("AND" or "OR") to use to add this
	 * condition to the filter
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addEqualsOrGreaterThanCondition(final String column,
			final Object value, final String booleanCondition) {
		columns.add(column);
		values.add(value);
		negates.add(false);
		likes.add(false);
		ignoreCase.add(false);
		equalsOrGreaterThan.add(true);
		equalsOrLessThan.add(false);
		booleanConditions.add(booleanCondition);
	}

	/**
	 * This methods allows to add "EqualsOrLessThan" conditions: column <= value
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 */
	public void addEqualsOrLessThanCondition(final String column,
			final Object value) {
		addEqualsOrLessThanCondition(column, value, defaultBooleanCondition);
	}

	/**
	 * This methods allows to add "EqualsOrLessThan" conditions: column <= value
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addEqualsOrLessThanCondition(final String column,
			final Object value, final String booleanCondition) {
		columns.add(column);
		values.add(value);
		negates.add(false);
		likes.add(false);
		ignoreCase.add(false);
		equalsOrGreaterThan.add(false);
		equalsOrLessThan.add(true);
		booleanConditions.add(booleanCondition);
	}

	/**
	 * This method allows to add a IsNull condition. This will restrict the
	 * result to the columns that have a null in the specified column.
	 * 
	 * @param column
	 *            column name that provides the value that has to fullfill the
	 *            condition
	 */
	public void addIsNullCondition(final String column) {
		addIsNullCondition(column, false, defaultBooleanCondition);
	}

	/**
	 * This method allows to add a IsNull condition. This will restrict the
	 * result to the columns that have a null in the specified column.
	 * 
	 * @param column
	 *            column name that provides the value that has to fullfill the
	 *            condition
	 * @param isNegativeCondition
	 *            specifies if the condition it is added with IS NULL or IS NOT
	 *            NULL
	 */
	public void addIsNullCondition(final String column,
			final boolean isNegativeCondition) {
		addIsNullCondition(column, isNegativeCondition, defaultBooleanCondition);
	}

	/**
	 * This method allows to add a IsNull condition. This will restrict the
	 * result to the columns that have a null in the specified column.
	 * 
	 * @param column
	 *            column name that provides the value that has to fullfill the
	 *            condition
	 * @param isNegativeCondition
	 *            specifies if the condition it is added with IS NULL or IS NOT
	 *            NULL
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addIsNullCondition(final String column,
			final boolean isNegativeCondition, final String booleanCondition) {
		columnsIsNull.add(column);
		negatesIsNull.add(isNegativeCondition);
		booleanConditionsIsNull.add(booleanCondition);
	}

	/**
	 * This method allows to add 'like' conditions: column like 'value'. You can
	 * use static fields WILDCARD_ALL and WILDCARD_ONE to build your value
	 * string for different matching combinations like: column like 'value%' or
	 * column like '_value'
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 */
	public void addLikeCondition(final String column, final Object value) {
		addLikeCondition(column, value, defaultBooleanCondition);
	}

	/**
	 * This method allows to add 'like' conditions: column like 'value'. You can
	 * use static fields WILDCARD_ALL and WILDCARD_ONE to build your value
	 * string for different matching combinations like: column like 'value%' or
	 * column like '_value'
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addLikeCondition(final String column, final Object value,
			final String booleanCondition) {
		columns.add(column);
		values.add(value);
		negates.add(false);
		likes.add(true);
		ignoreCase.add(false);
		equalsOrGreaterThan.add(false);
		equalsOrLessThan.add(false);
		booleanConditions.add(booleanCondition);
	}

	/**
	 * This method allows to add 'like' conditions: column like 'value'. You can
	 * use static fields WILDCARD_ALL and WILDCARD_ONE to build your value
	 * string for different matching combinations like: column like 'value%' or
	 * column like '_value'
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 */
	public void addLikeConditionIgnoreCase(final String column,
			final Object value) {
		addLikeConditionIgnoreCase(column, value, defaultBooleanCondition);
	}

	/**
	 * This method allows to add 'like' conditions: column like 'value'. You can
	 * use static fields WILDCARD_ALL and WILDCARD_ONE to build your value
	 * string for different matching combinations like: column like 'value%' or
	 * column like '_value'
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addLikeConditionIgnoreCase(final String column,
			final Object value, final String booleanCondition) {
		columns.add(column);
		values.add(value);
		negates.add(false);
		likes.add(true);
		ignoreCase.add(true);
		equalsOrGreaterThan.add(false);
		equalsOrLessThan.add(false);
		booleanConditions.add(booleanCondition);
	}

	/**
	 * This method allows to add a sorting column to the filter and specify a
	 * sorting direction (order by columnToSort ASC | DESC). In order to specify
	 * the sorting direction you can use static fields ASC and DESC. You can add
	 * several columns to sort to a filter with different sorting directions and
	 * they will be apply in order to have more complex 'order by' order by
	 * columnToSort1 ASC, columnToSort2 DESC)
	 * 
	 * @param columnToSort
	 * @param sortingDirection
	 */
	public void addSortingColumn(final String columnToSort,
			final String sortingDirection) {
		columnsToSort.add(columnToSort);
		sortingDirections.add(sortingDirection);
	}

	/**
	 * This method allows to add an 'strict' condition. This will restrict the
	 * result to the columns that have exactly the specified value. It will add
	 * to the filter a condition like: column = value
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 */
	public void addStrictCondition(final String column, final Object value) {
		addStrictCondition(column, value, false, defaultBooleanCondition);
	}

	/**
	 * This method allows to add a 'strict' condition. This will restrict the
	 * result to the columns that do not have the specified value. It allows to
	 * specify if a condition is negative. In this case, t will add to the
	 * filter a condition like: column != value
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param isNegativeCondition
	 *            specifies if the condition it is added with = or != operator
	 */
	public void addStrictCondition(final String column, final Object value,
			final boolean isNegativeCondition) {
		addStrictCondition(column, value, isNegativeCondition,
				defaultBooleanCondition);
	}

	/**
	 * This method allows to add a 'strict' condition. This will restrict the
	 * result to the columns that do not have the specified value. It allows to
	 * specify if a condition is negative. In this case, t will add to the
	 * filter a condition like: column != value.
	 * 
	 * 
	 * @param column
	 *            column name that provides the value to compare with 'value'
	 * @param value
	 *            value to compare in the condition
	 * @param isNegativeCondition
	 *            specifies if the condition it is added with = or != operator
	 * @param booleanCondition
	 *            logic operator (AND or OR) to add this condition to the
	 *            filter. You can use static fields CONDITION_AND and
	 *            CONDITION_OR for this
	 */
	public void addStrictCondition(final String column, final Object value,
			final boolean isNegativeCondition, final String booleanCondition) {
		columns.add(column);
		values.add(value);
		negates.add(isNegativeCondition);
		likes.add(false);
		ignoreCase.add(false);
		equalsOrGreaterThan.add(false);
		equalsOrLessThan.add(false);
		booleanConditions.add(booleanCondition);
	}

	public ArrayList<String> getBooleanConditions() {
		return booleanConditions;
	}

	public ArrayList<String> getBooleanConditionsIsNull() {
		return booleanConditionsIsNull;
	}

	public ArrayList<String> getColumns() {
		return columns;
	}

	public ArrayList<String> getColumnsIsNull() {
		return columnsIsNull;
	}

	public ArrayList<String> getColumnsToSort() {
		return columnsToSort;
	}

	public String getDefaultBooleanCondition() {
		return defaultBooleanCondition;
	}

	public String getDefaultConditionOperation() {
		return defaultBooleanCondition;
	}

	public ArrayList<Boolean> getEqualsOrGreaterThan() {
		return equalsOrGreaterThan;
	}

	public ArrayList<Boolean> getEqualsOrLessThan() {
		return equalsOrLessThan;
	}

	public ArrayList<Boolean> getIgnoreCase() {
		return ignoreCase;
	}

	public ArrayList<Boolean> getLikes() {
		return likes;
	}

	public ArrayList<Boolean> getNegates() {
		return negates;
	}

	public ArrayList<Boolean> getNegatesIsNull() {
		return negatesIsNull;
	}

	public ArrayList<String> getSortingDirections() {
		return sortingDirections;
	}

	public ArrayList<Object> getValues() {
		return values;
	}

	public void reset() {
		resetConditions();
		resetSortingColumns();
		resetIsNullConditions();
	}

	public void resetConditions() {
		columns = new ArrayList<String>();
		values = new ArrayList<Object>();
		negates = new ArrayList<Boolean>();
		likes = new ArrayList<Boolean>();
		ignoreCase = new ArrayList<Boolean>();
		equalsOrGreaterThan = new ArrayList<Boolean>();
		equalsOrLessThan = new ArrayList<Boolean>();
		booleanConditions = new ArrayList<String>();
	}

	public void resetIsNullConditions() {
		columnsIsNull = new ArrayList<String>();
		negatesIsNull = new ArrayList<Boolean>();
		booleanConditionsIsNull = new ArrayList<String>();
	}

	public void resetSortingColumns() {
		columnsToSort = new ArrayList<String>();
		sortingDirections = new ArrayList<String>();
	}
}
