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
import org.appverse.web.framework.backend.api.services.integration.ListDiffersSizeException;
import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDozerB2IBeanConverter<BusinessBean extends AbstractBusinessBean, IntegrationBean extends AbstractIntegrationBean>
		implements IB2IBeanConverter<BusinessBean, IntegrationBean> {

	private Class<IntegrationBean> integrationBeanClass;
	private Class<BusinessBean> businessBeanClass;

	private String SCOPE_WITHOUT_DEPENDENCIES = "default-scope-without-dependencies";

	private String SCOPE_COMPLETE = "default-scope-complete";

	private String SCOPE_CUSTOM = "default-scope-custom";

	@Autowired
	protected DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean;

	public AbstractDozerB2IBeanConverter() {
	}

	@Override
	public IntegrationBean convert(BusinessBean bean) throws Exception {
		return convert(bean, ConversionType.Complete);
	}

	@Override
	public IntegrationBean convert(BusinessBean businessBean,
			ConversionType conversionType) throws Exception {
        return convert(businessBean, getScope(conversionType));
	}

    @Override
    public IntegrationBean convert(BusinessBean businessBean,
                                   String scope) throws Exception {
        return ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                businessBean, integrationBeanClass, scope);
    }

	@Override
	public void convert(final BusinessBean businessBean,
			IntegrationBean integrationBean) throws Exception {
		convert(businessBean, integrationBean, ConversionType.Complete);
	}

	@Override
	public void convert(final BusinessBean businessBean,
			IntegrationBean integrationBean, ConversionType conversionType)
			throws Exception {

        convert(businessBean, integrationBean, getScope(conversionType));
	}

    @Override
    public void convert(final BusinessBean businessBean,
                        IntegrationBean integrationBean, String scope)
            throws Exception{
        ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(businessBean,
                                                          integrationBean, scope);
    }

	@Override
	public BusinessBean convert(IntegrationBean bean) throws Exception {
		return convert(bean, ConversionType.Complete);
	}

	@Override
	public void convert(final IntegrationBean integrationBean,
			BusinessBean businessBean) throws Exception {
		convert(integrationBean, businessBean, ConversionType.Complete);
	}

	@Override
	public void convert(final IntegrationBean integrationBean,
			BusinessBean businessBean, ConversionType conversionType)
			throws Exception {
        convert(integrationBean, businessBean, getScope(conversionType));
	}

    @Override
    public void convert(final IntegrationBean integrationBean,
                        BusinessBean businessBean, String scope)
            throws Exception {
        ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(integrationBean,
                businessBean, scope);
    }

	@Override
	public BusinessBean convert(IntegrationBean integrationBean,
			ConversionType conversionType) throws Exception {
        return convert(integrationBean, getScope(conversionType));
	}

    @Override
    public BusinessBean convert(IntegrationBean integrationBean,
                                String scope) throws Exception {
        return ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                integrationBean, businessBeanClass, scope);
    }

	@Override
	public List<IntegrationBean> convertBusinessList(
			List<BusinessBean> businessBeans) throws Exception {
		List<IntegrationBean> integrationBeans = new ArrayList<IntegrationBean>();
		for (BusinessBean businessBean : businessBeans) {
			IntegrationBean integrationBean = convert(businessBean,
					ConversionType.Complete);
			integrationBeans.add(integrationBean);
		}
		return integrationBeans;
	}

	@Override
	public List<IntegrationBean> convertBusinessList(
			List<BusinessBean> businessBeans, ConversionType conversionType)
			throws Exception {
        return convertBusinessList(businessBeans, getScope(conversionType));
	}

    @Override
    public List<IntegrationBean> convertBusinessList(
            List<BusinessBean> businessBeans, String scope)
            throws Exception {
        List<IntegrationBean> integrationBeans = new ArrayList<IntegrationBean>();
        for (BusinessBean businessBean : businessBeans) {
            IntegrationBean integrationBean = ((Mapper) dozerBeanMapperFactoryBean
                    .getObject()).map(businessBean, integrationBeanClass,
                    scope);
            integrationBeans.add(integrationBean);
        }
        return integrationBeans;
    }


	@Override
	public void convertBusinessList(final List<BusinessBean> businessBeans,
			List<IntegrationBean> integrationBeans) throws Exception {
		if (businessBeans.size() != integrationBeans.size()) {
			throw new ListDiffersSizeException();
		}
		for (int i = 0; i < businessBeans.size(); i++) {
			convert(businessBeans.get(i), integrationBeans.get(i),
					ConversionType.Complete);
		}
	}

	@Override
	public void convertBusinessList(final List<BusinessBean> businessBeans,
			List<IntegrationBean> integrationBeans,
			ConversionType conversionType) throws Exception {
        convertBusinessList(businessBeans, integrationBeans, getScope(conversionType));
	}

    @Override
    public void convertBusinessList(final List<BusinessBean> businessBeans,
                                    List<IntegrationBean> integrationBeans,
                                    String scope) throws Exception {
        if (businessBeans.size() != integrationBeans.size()) {
            throw new ListDiffersSizeException();
        }
        for (int i = 0; i < businessBeans.size(); i++) {
            ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                    businessBeans.get(i), integrationBeans.get(i),
                    scope);
        }
    }


	@Override
	public List<BusinessBean> convertIntegrationList(
			List<IntegrationBean> integrationBeans) throws Exception {
		List<BusinessBean> businessBeans = new ArrayList<BusinessBean>();
		for (IntegrationBean integrationBean : integrationBeans) {
			BusinessBean businessBean = convert(integrationBean,
					ConversionType.Complete);
			businessBeans.add(businessBean);
		}
		return businessBeans;
	}

	@Override
	public List<BusinessBean> convertIntegrationList(
			List<IntegrationBean> integrationBeans,
			ConversionType conversionType) throws Exception {
        return convertIntegrationList(integrationBeans, getScope(conversionType));
	}

    @Override
    public List<BusinessBean> convertIntegrationList(
            List<IntegrationBean> integrationBeans,
            String scope) throws Exception {
        List<BusinessBean> businessBeans = new ArrayList<BusinessBean>();
        for (IntegrationBean integrationBean : integrationBeans) {
            BusinessBean businessBean = ((Mapper) dozerBeanMapperFactoryBean
                    .getObject()).map(integrationBean, businessBeanClass,
                    scope);
            businessBeans.add(businessBean);
        }
        return businessBeans;
    }


	@Override
	public void convertIntegrationList(
			final List<IntegrationBean> integrationBeans,
			List<BusinessBean> businessBeans) throws Exception {
		if (integrationBeans.size() != businessBeans.size()) {
			throw new ListDiffersSizeException();
		}
		for (int i = 0; i < integrationBeans.size(); i++) {
			convert(integrationBeans.get(i), businessBeans.get(i),
					ConversionType.Complete);
		}
	}

	@Override
	public void convertIntegrationList(
			final List<IntegrationBean> integrationBeans,
			List<BusinessBean> businessBeans, ConversionType conversionType)
			throws Exception {
        convertIntegrationList(integrationBeans, businessBeans, getScope(conversionType));
	}

    @Override
    public void convertIntegrationList(
            final List<IntegrationBean> integrationBeans,
            List<BusinessBean> businessBeans, String scope)
            throws Exception {
        if (integrationBeans.size() != businessBeans.size()) {
            throw new ListDiffersSizeException();
        }
        for (int i = 0; i < integrationBeans.size(); i++) {
            ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                    integrationBeans.get(i), businessBeans.get(i),
                    scope);
        }
    }

	@Override
	public String getScope(ConversionType conversionType) {
		String scope = null;
		if (conversionType == ConversionType.WithoutDependencies) {
			scope = SCOPE_WITHOUT_DEPENDENCIES;
		} else if (conversionType == ConversionType.Custom) {
			scope = SCOPE_CUSTOM;
		} else {
			scope = SCOPE_COMPLETE;
		}
		return scope;
	}

	public void setBeanClasses(Class<BusinessBean> businessBeanClass,
			Class<IntegrationBean> integrationBeanClass) {
		this.integrationBeanClass = integrationBeanClass;
		this.businessBeanClass = businessBeanClass;
	}

	@Override
	public void setScopes(String... scopes) {
		if (scopes.length > 0) {
			this.SCOPE_COMPLETE = scopes[0];
		}
		if (scopes.length > 1) {
			this.SCOPE_WITHOUT_DEPENDENCIES = scopes[1];
		}
		if (scopes.length > 2) {
			this.SCOPE_CUSTOM = scopes[2];
		}
	}

}
