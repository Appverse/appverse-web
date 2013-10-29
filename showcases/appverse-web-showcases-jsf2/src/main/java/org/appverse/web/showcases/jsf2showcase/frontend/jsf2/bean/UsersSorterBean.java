/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.jsf2showcase.frontend.jsf2.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@ViewScoped
public class UsersSorterBean implements Serializable {

	// TODO: Implement database pagination
	// Use the "arreangeable" example: http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=dataTable&sample=arrangeableModel&skin=blueSky
	
	// Richfaces dataTable pagination by default is design to do pagination
	// fetching the rows
	// in-memory (assuming that the service provides all the rows). This is
	// in-memory pagination, not database pagination at all.
	// In order to provide real database pagination, you can implement your own
	// solution extending tableDataModel
	// Example links talking about this matter:
	// http://stackoverflow.com/questions/3707897/richfaces-extendedtabledatamodel-sorting-columns-retrieves-all-rows
	// http://katzmaier.blogspot.com.es/2010/03/richfaces-server-side-pagination.html

	private static final long serialVersionUID = 3481850572955137347L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, SortOrder> sortsOrders;
	private List<String> sortPriorities;
	private int clientRows;

	private static final String SORT_PROPERTY_PARAMETER = "sortProperty";

	public UsersSorterBean() {
		// Default client rows (number of rows per 'page' is 20)
		clientRows = 10;
		sortsOrders = new HashMap<String, SortOrder>();
		sortPriorities = new ArrayList<String>();
	}

	public void sort() {

		String property = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get(SORT_PROPERTY_PARAMETER);
		if (property != null) {
			SortOrder currentPropertySortOrder = sortsOrders.get(property);

			if (!sortPriorities.contains(property)) {
				sortPriorities.add(property);
			}

			if (currentPropertySortOrder == null
					|| currentPropertySortOrder.equals(SortOrder.descending)) {
				sortsOrders.put(property, SortOrder.ascending);
			} else {
				sortsOrders.put(property, SortOrder.descending);
			}
		}
	}

	public void reset() {
		sortPriorities.clear();
		sortsOrders.clear();
	}

	public List<String> getSortPriorities() {
		return sortPriorities;
	}

	public Map<String, SortOrder> getSortsOrders() {
		return sortsOrders;
	}

	public static String getSortPropertyParameter() {
		return SORT_PROPERTY_PARAMETER;
	}

	public void setSortsOrders(Map<String, SortOrder> sortsOrders) {
		this.sortsOrders = sortsOrders;
	}

	public void setSortPriorities(List<String> sortPriorities) {
		this.sortPriorities = sortPriorities;
	}

	@PostConstruct
	public void initIt() {		
		logger.info("*** BEAN POST CONSTRUCTION: " + this.getClass().toString());
	}

	@PreDestroy
	public void cleanUp() {
		logger.info("*** BEAN POST DESTRUCTION: " + this.getClass().toString());
	}
	
	public int getClientRows() {
		return clientRows;
	}

	public void setClientRows(int clientRows) {
		this.clientRows = clientRows;
	}

}