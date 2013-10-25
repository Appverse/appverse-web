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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.appverse.web.framework.backend.api.model.presentation.PresentationDataFilter;
import org.appverse.web.framework.backend.api.model.presentation.PresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.api.model.presentation.PresentationPaginatedResult;
import org.appverse.web.showcases.jsf2showcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.jsf2showcase.backend.services.presentation.UserServiceFacade;
import org.richfaces.component.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@ViewScoped
public class UserManagementBean implements Serializable {

	private static final long serialVersionUID = -6237417487105926855L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ManagedProperty(value = "#{usersSorterBean}")
	private UsersSorterBean usersSorterBean;

	@ManagedProperty(value = "#{userServiceFacade}")
	private UserServiceFacade userServiceFacade;

	private List<UserVO> userVOList = null;

	public UserServiceFacade getUserServiceFacade() {
		return userServiceFacade;
	}

	public UsersSorterBean getUsersSorterBean() {
		return usersSorterBean;
	}

	public List<UserVO> getUserVOList() {
		
		logger.debug("getUserVOList");

		try {
			usersSorterBean.getSortPriorities();

			PresentationPaginatedDataFilter presentationPaginatedDataFilter = new PresentationPaginatedDataFilter();
			for (String columnToSort : usersSorterBean.getSortPriorities()) {
				SortOrder sortOrder = usersSorterBean.getSortsOrders().get(
						columnToSort);
				presentationPaginatedDataFilter
						.addSortingColumn(
								columnToSort,
								sortOrder.equals(SortOrder.ascending) ? PresentationDataFilter.ASC
										: PresentationDataFilter.DESC);
			}

			// Current function
			PresentationPaginatedResult<UserVO> paginatedResult = userServiceFacade
					.loadUsers(presentationPaginatedDataFilter);
			userVOList = paginatedResult.getData();
		} catch (Throwable e) {
			// TODO: Review real error handling this is a minimal exception handling mechanism in JSF2
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ERROR [" + e.getMessage() + "]", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, fm);
		}
		return userVOList;
	}


	public void setUserServiceFacade(UserServiceFacade userServiceFacade) {
		this.userServiceFacade = userServiceFacade;
	}

	public void setUsersSorterBean(UsersSorterBean usersSorterBean) {
		this.usersSorterBean = usersSorterBean;
	}

	public void setUserVOList(List<UserVO> userVOList) {
		this.userVOList = userVOList;
	}

	@PostConstruct
	public void initIt() {		
		logger.info("*** BEAN POST CONSTRUCTION: " + this.getClass().toString());
	}

	@PreDestroy
	public void cleanUp() {
		logger.info("*** BEAN POST DESTRUCTION: " + this.getClass().toString());
	}
}