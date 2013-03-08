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
package org.appverse.web.framework.backend.api.aop.aspects;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import org.appverse.web.framework.backend.api.common.AbstractBean;
import org.appverse.web.framework.backend.api.converters.IB2IBeanConverter;
import org.appverse.web.framework.backend.api.converters.IBeanConverter;
import org.appverse.web.framework.backend.api.converters.IP2BBeanConverter;
import org.appverse.web.framework.backend.api.converters.helpers.Detachable;
import org.appverse.web.framework.backend.api.converters.helpers.IndirectArrayList;
import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

/**
 * 
 * @see org.appverse.web.framework.backend.api.converters.helpers.ConverterUtils
 * @see org.appverse.web.framework.backend.api.converters.helpers.IndirectArrayList
 * 
 */
public aspect ConverterAspect {
	
	
	//////////////////////////////////Inter-type declarations ///////////////////////////////
		
	// Add detachable interface to AbstractBean
    declare parents : AbstractBean implements Detachable;
	
	// Add detachable fields to Abstractbean
	private transient boolean AbstractBean.detached = false;
	private transient AbstractBean AbstractBean.source = null;
	private transient IBeanConverter AbstractBean.converter = null;
	private transient String[] AbstractBean.detachableFields = new String[] { "detached", "source",
	"converter" };
	
	// Add detachable methods to AbstractBean
	
	//Returns if bean is a proxy that need be converted (detached true)
	//or conversion has been performed and bean has the actual data (detached
	//false)
	public boolean AbstractBean.isDetached() {
		return detached;
	}
	
	public AbstractBean AbstractBean.getSource(){
		return source;
	}

	public IBeanConverter AbstractBean.getConverter() {
		return converter;
	}
	
	public String[] AbstractBean.getDetachableFields() {
		return detachableFields;
	}
	
	//Clean the elements used to helps lazy loading. This method is called when
	//the conversion has been performed
	public void AbstractBean.undetach() {
		detached = false;
		source = null;
		converter = null; 
	}
	
	//Set the elements used to helps lazy loading. This method is called after AbstractBean
	//is created
	public void AbstractBean.detach(AbstractBean source, IBeanConverter converter) {
		this.detached = true;
		this.source = source;
		this.converter = converter;
	}
	
	///////////////////////////// (end of) Inter-type declarations ///////////////////////////
	

	////////////////////////////////// Pointcuts declarations ///////////////////////////////
	
	// All operations returning a object with class subcassing AbstractBean
	pointcut beanGetters():
		(call(AbstractBean+ *.*(..))) || (execution(* org.appverse.web.framework.backend.api.converters.helpers.IndirectArrayList.get(int))) ;
	
	// All operations returning a List of objects with class subcassing AbstractBean
    pointcut collectionGetters():
    	(call(List<AbstractBean+>  *.*(..))) && (call(List<? extends AbstractBean+>  *.*(..)));
       
	////////////////////////////////(end of)  Pointcuts declarations /////////////////////////
    

	////////////////////////////////// Advices declarations //////////////////////////////////
    
    // After returning a AbstractBean subclass object
   	@SuppressWarnings({ "unchecked", "rawtypes" })
    after() returning (AbstractBean bean): beanGetters(){
		try{
	    	// If bean is detached...
	    	if(bean!=null &&((Detachable)bean).isDetached()){
	    		// Get bean source
	    		AbstractBean source = (AbstractBean)((Detachable)bean).getSource();
				AbstractBean undetachedBean = null;
				if(bean instanceof AbstractPresentationBean){
					undetachedBean = ((IP2BBeanConverter)((Detachable)bean).getConverter()).convert((AbstractBusinessBean)source);
				}else if (bean instanceof AbstractBusinessBean){
					if(((Detachable)bean).getConverter() instanceof IB2IBeanConverter){
						undetachedBean = ((IB2IBeanConverter)((Detachable)bean).getConverter()).convert((AbstractIntegrationBean)source);
					}else if(((Detachable)bean).getConverter() instanceof IP2BBeanConverter){
						undetachedBean = ((IP2BBeanConverter)((Detachable)bean).getConverter()).convert((AbstractPresentationBean)source);
					}
				}
				BeanUtils.copyProperties(bean, undetachedBean);
				bean.undetach(); 		
	    	}
		}catch(Exception e){
   			//TODO: throw exceptions
    	}
    }
    
    // After returning a collection of AbstractBean subclass object
   	@SuppressWarnings({ "unchecked", "rawtypes" })
    after() returning (List<? extends AbstractBean> collection): collectionGetters() {
		try{
	    	// If collection is a IndirectArrayList
	    	if(collection!=null && collection instanceof IndirectArrayList){	
				IndirectArrayList<AbstractBean,AbstractBean> list = (IndirectArrayList<AbstractBean,AbstractBean>) collection;
		    	// If list is detached...
				if(list.isDetached()){
					// Get source list
			    	List<AbstractBean> source = list.getSource();
			    	// For each source list element...
			    	for(AbstractBean sourceBean:source){
			    		// Get the constructor of a element of target list
						Constructor<AbstractBean> constructor = (Constructor<AbstractBean>)list.getItemType().getConstructor(new Class[] {});
			    		// Create a element of target list...
						AbstractBean bean = (AbstractBean)constructor.newInstance( new Object[] {});
						// Detach target element with source and converter.
						((Detachable)bean).detach(sourceBean, list.getItemConverter());	    		
			    		// Add detached element to target list
						list.add(bean);
			    	}
			    	// Detach target list
			    	list.undetach();
		    	} 
	    	}
		}catch(Exception e){
    			//TODO: throw exceptions
    	}
    }
	//////////////////////////////// (end of)  Advices declarations ///////////////////////////    
}
