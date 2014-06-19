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
package org.appverse.web.framework.backend.persistence.services.integration.helpers;

import org.appverse.web.framework.backend.api.helpers.util.GMTTimeHelper;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class EntityListener {

	public String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String username = null;
		if (authentication == null) {
			username = "batch_process";
		} else {
            Object obj = authentication.getPrincipal();
            //On unauthenticated applications obj is a simple string and it will fail
            if (obj instanceof UserDetails){
                UserDetails user = (UserDetails) authentication.getPrincipal();
                username = user.getUsername();
            }else if (obj instanceof String){
                username = (String)obj;
            }

		}
		return username;
	}

	@PrePersist
	public void prePersist(AbstractIntegrationBean integrationBean) {
        if (integrationBean instanceof AbstractIntegrationAuditedJPABean){
            AbstractIntegrationAuditedJPABean integrationAuditedBean = (AbstractIntegrationAuditedJPABean)integrationBean;

            Date timestamp = GMTTimeHelper.toGMTDate(new Date());

            integrationAuditedBean.setCreatedBy(getUsername());
            integrationAuditedBean.setCreated(timestamp);
            integrationAuditedBean.setUpdatedBy(getUsername());
            integrationAuditedBean.setUpdated(timestamp);
            integrationAuditedBean.setVersion(1);
            if (integrationAuditedBean.getStatus() == null) {
                integrationAuditedBean
                        .setStatus(AbstractIntegrationAuditedJPABean.STATUS_ACTIVE);
            }
        }
	}

	@PreUpdate
	public void preUpdate(AbstractIntegrationBean integrationBean) {
        if (integrationBean instanceof AbstractIntegrationAuditedJPABean){
            AbstractIntegrationAuditedJPABean integrationAuditedBean = (AbstractIntegrationAuditedJPABean)integrationBean;

            Date timestamp = GMTTimeHelper.toGMTDate(new Date());
            integrationAuditedBean.setUpdatedBy(getUsername());
            integrationAuditedBean.setUpdated(timestamp);
        }
	}
}
