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

import java.util.ArrayList;
import java.util.List;

import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;
import org.appverse.web.framework.backend.api.services.integration.ListDiffersSizeException;
import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDozerP2BBeanConverter<PresentationBean extends AbstractPresentationBean, BusinessBean extends AbstractBusinessBean>
		implements IP2BBeanConverter<PresentationBean, BusinessBean> {

	@Autowired
	protected DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean;
	private Class<PresentationBean> presentationBeanClass;
	private Class<BusinessBean> businessBeanClass;
	private String SCOPE_WITHOUT_DEPENDENCIES = "default-scope-without-dependencies";
	private String SCOPE_COMPLETE = "default-scope-complete";
	private String SCOPE_CUSTOM = "default-scope-custom";

	public AbstractDozerP2BBeanConverter() {
		super();
	}

	@Override
	public PresentationBean convert(BusinessBean bean) throws Exception {
		return convert(bean, ConversionType.Complete);
	}

	@Override
	public PresentationBean convert(BusinessBean businessBean,
			ConversionType conversionType) throws Exception {
        return convert(businessBean, getScope(conversionType));
	}

    @Override
    public PresentationBean convert(BusinessBean businessBean,
                                    String scope) throws Exception {
        return ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                businessBean, presentationBeanClass, scope);
    }


	@Override
	public void convert(final BusinessBean businessBean,
			PresentationBean presentationBean) throws Exception {
		convert(businessBean, presentationBean, ConversionType.Complete);
	}

	@Override
	public void convert(final BusinessBean businessBean,
			PresentationBean presentationBean, ConversionType conversionType)
			throws Exception {
        convert(businessBean, presentationBean, getScope(conversionType));
	}

    @Override
    public void convert(final BusinessBean businessBean,
                        PresentationBean presentationBean, String scope)
            throws Exception {
        ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(businessBean,
                presentationBean, scope);
    }

	@Override
	public BusinessBean convert(PresentationBean bean) throws Exception {
		return convert(bean, ConversionType.Complete);
	}

	@Override
	public void convert(final PresentationBean presentationBean,
			BusinessBean businessBean) throws Exception {
		convert(presentationBean, businessBean, ConversionType.Complete);
	}

	@Override
	public void convert(final PresentationBean presentationBean,
			BusinessBean businessBean, ConversionType conversionType)
			throws Exception {
        convert(presentationBean, businessBean, getScope(conversionType));
	}

    @Override
    public void convert(final PresentationBean presentationBean,
                        BusinessBean businessBean, String scope)
            throws Exception {
        ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(presentationBean,
                businessBean, scope);
    }

	@Override
	public BusinessBean convert(PresentationBean presentationBean,
			ConversionType conversionType) throws Exception {
		return convert(presentationBean, getScope(conversionType));
	}

    @Override
    public BusinessBean convert(PresentationBean presentationBean,
                                String scope) throws Exception {
        return ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                presentationBean, businessBeanClass, scope);
    }

	@Override
	public List<PresentationBean> convertBusinessList(
			List<BusinessBean> businessBeans) throws Exception {
		List<PresentationBean> presentationBeans = new ArrayList<PresentationBean>();
		for (BusinessBean businessBean : businessBeans) {
			PresentationBean presentationBean = convert(businessBean,
					ConversionType.Complete);
			presentationBeans.add(presentationBean);
		}
		return presentationBeans;
	}

	@Override
	public List<PresentationBean> convertBusinessList(
			List<BusinessBean> businessBeans, ConversionType conversionType)
			throws Exception {
        return convertBusinessList(businessBeans, getScope(conversionType));
	}

    @Override
    public List<PresentationBean> convertBusinessList(
            List<BusinessBean> businessBeans, String scope)
            throws Exception {
        List<PresentationBean> presentationBeans = new ArrayList<PresentationBean>();
        for (BusinessBean businessBean : businessBeans) {
            PresentationBean presentationBean = ((Mapper) dozerBeanMapperFactoryBean
                    .getObject()).map(businessBean, presentationBeanClass,
                    scope);
            presentationBeans.add(presentationBean);
        }
        return presentationBeans;
    }

	@Override
	public void convertBusinessList(List<BusinessBean> businessBeans,
			List<PresentationBean> presentationBeans) throws Exception {
		if (businessBeans.size() != presentationBeans.size()) {
			throw new ListDiffersSizeException();
		}
		for (int i = 0; i < businessBeans.size(); i++) {
			convert(businessBeans.get(i), presentationBeans.get(i),
					ConversionType.Complete);
		}
	}

	@Override
	public void convertBusinessList(List<BusinessBean> businessBeans,
			List<PresentationBean> presentationBeans,
			ConversionType conversionType) throws Exception {
        convertBusinessList(businessBeans, presentationBeans, getScope(conversionType));
	}

    @Override
    public void convertBusinessList(List<BusinessBean> businessBeans,
                                    List<PresentationBean> presentationBeans,
                                    String scope) throws Exception {
        if (businessBeans.size() != presentationBeans.size()) {
            throw new ListDiffersSizeException();
        }
        for (int i = 0; i < businessBeans.size(); i++) {
            ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                    businessBeans.get(i), presentationBeans.get(i),
                    scope);
        }
    }

	@Override
	public List<BusinessBean> convertPresentationList(
			List<PresentationBean> presentationBeans) throws Exception {
		List<BusinessBean> businessBeans = new ArrayList<BusinessBean>();
		for (PresentationBean presentationBean : presentationBeans) {
			BusinessBean businessBean = convert(presentationBean,
					ConversionType.Complete);
			businessBeans.add(businessBean);
		}
		return businessBeans;
	}

	@Override
	public List<BusinessBean> convertPresentationList(
			List<PresentationBean> presentationBeans,
			ConversionType conversionType) throws Exception {
        return convertPresentationList(presentationBeans, getScope(conversionType));
	}

    @Override
    public List<BusinessBean> convertPresentationList(
            List<PresentationBean> presentationBeans,
            String scope) throws Exception {
        List<BusinessBean> businessBeans = new ArrayList<BusinessBean>();
        for (PresentationBean presentationBean : presentationBeans) {
            BusinessBean businessBean = ((Mapper) dozerBeanMapperFactoryBean
                    .getObject()).map(presentationBean, businessBeanClass,
                    scope);
            businessBeans.add(businessBean);
        }
        return businessBeans;
    }

	@Override
	public void convertPresentationList(
			List<PresentationBean> presentationBeans,
			List<BusinessBean> businessBeans) throws Exception {
		if (presentationBeans.size() != businessBeans.size()) {
			throw new ListDiffersSizeException();
		}
		for (int i = 0; i < presentationBeans.size(); i++) {
			convert(presentationBeans.get(i), businessBeans.get(i),
					ConversionType.Complete);
		}
	}

	@Override
	public void convertPresentationList(
			List<PresentationBean> presentationBeans,
			List<BusinessBean> businessBeans, ConversionType conversionType)
			throws Exception {
        convertPresentationList(presentationBeans, businessBeans, getScope(conversionType));
	}

    @Override
    public void convertPresentationList(
            List<PresentationBean> presentationBeans,
            List<BusinessBean> businessBeans, String scope)
            throws Exception {

        if (presentationBeans.size() != businessBeans.size()) {
            throw new ListDiffersSizeException();
        }
        for (int i = 0; i < presentationBeans.size(); i++) {
            ((Mapper) dozerBeanMapperFactoryBean.getObject()).map(
                    presentationBeans.get(i), businessBeans.get(i),
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

	public void setBeanClasses(Class<PresentationBean> presentationBeanClass,
			Class<BusinessBean> businessBeanClass) {
		this.presentationBeanClass = presentationBeanClass;
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