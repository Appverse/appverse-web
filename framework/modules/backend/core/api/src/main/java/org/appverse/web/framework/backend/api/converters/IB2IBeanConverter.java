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
package org.appverse.web.framework.backend.api.converters;

import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;

import java.util.List;

public interface IB2IBeanConverter<BusinessBean extends AbstractBusinessBean, IntegrationBean extends AbstractIntegrationBean>
		extends IBeanConverter {

	IntegrationBean convert(BusinessBean businessBean) throws Exception;

	IntegrationBean convert(BusinessBean businessBean,
			ConversionType conversionType) throws Exception;

    IntegrationBean convert(BusinessBean businessBean,
                                   String scope) throws Exception;

	void convert(BusinessBean businessBean, IntegrationBean integrationBean)
			throws Exception;

	void convert(BusinessBean businessBean, IntegrationBean integrationBean,
			ConversionType conversionType) throws Exception;

    void convert(final BusinessBean businessBean, IntegrationBean integrationBean, String scope)
            throws Exception;

	BusinessBean convert(IntegrationBean integrationBean) throws Exception;

	void convert(IntegrationBean integrationBean, BusinessBean businessBean)
			throws Exception;

	void convert(IntegrationBean integrationBean, BusinessBean businessBean,
			ConversionType conversionType) throws Exception;

    void convert(final IntegrationBean integrationBean,
                        BusinessBean businessBean, String scope) throws Exception;

	BusinessBean convert(IntegrationBean integrationBean,
			ConversionType conversionType) throws Exception;

    BusinessBean convert(IntegrationBean integrationBean,
                                String scope) throws Exception;

	List<IntegrationBean> convertBusinessList(List<BusinessBean> businessBeans)
			throws Exception;

	List<IntegrationBean> convertBusinessList(List<BusinessBean> businessBeans,
			ConversionType conversionType) throws Exception;

    List<IntegrationBean> convertBusinessList(
            List<BusinessBean> businessBeans, String scope) throws Exception;

	void convertBusinessList(final List<BusinessBean> businessBeans,
			List<IntegrationBean> integrationBeans) throws Exception;

	void convertBusinessList(final List<BusinessBean> businessBeans,
			List<IntegrationBean> integrationBeans,
			ConversionType conversionType) throws Exception;

    void convertBusinessList(final List<BusinessBean> businessBeans,
                                    List<IntegrationBean> integrationBeans,
                                    String scope) throws Exception;

	List<BusinessBean> convertIntegrationList(
			List<IntegrationBean> integrationBeans) throws Exception;

	List<BusinessBean> convertIntegrationList(
			List<IntegrationBean> integrationBeans,
			ConversionType conversionType) throws Exception;

    List<BusinessBean> convertIntegrationList(
            List<IntegrationBean> integrationBeans,
            String scope) throws Exception;

	void convertIntegrationList(final List<IntegrationBean> integrationBeans,
			List<BusinessBean> businessBeans) throws Exception;

	void convertIntegrationList(final List<IntegrationBean> integrationBeans,
			List<BusinessBean> businessBeans, ConversionType conversionType)
			throws Exception;

    void convertIntegrationList(
            final List<IntegrationBean> integrationBeans,
            List<BusinessBean> businessBeans, String scope) throws Exception;

}
