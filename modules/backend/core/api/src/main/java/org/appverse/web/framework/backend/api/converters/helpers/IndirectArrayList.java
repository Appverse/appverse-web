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
package org.appverse.web.framework.backend.api.converters.helpers;

import java.util.ArrayList;
import java.util.List;

import org.appverse.web.framework.backend.api.common.AbstractBean;
import org.appverse.web.framework.backend.api.converters.IBeanConverter;

/**
 * Wraps a ArrayList class addind properties to manage detach and undetach
 * operations needed to perform lazy loading operations to ArrayList collections
 * 
 * @see org.appverse.web.framework.backend.api.converters.helpers.ConverterUtils
 * @see org.appverse.web.framework.backend.aop.aspects.ConverterAspect
 * 
 */
public class IndirectArrayList<E, F> extends ArrayList<E> {

	private static final long serialVersionUID = -1841312699228737735L;
	private boolean detached = true;
	private List<F> source = null;
	private Class<E> itemType = null;
	private IBeanConverter itemConverter = null;

	/**
	 * Creates a new LazyArrayList with a source Collection and a itemType and
	 * itemConverter. The ConverterAspect will use the itemType and
	 * itemConverter to convert the source collection to actual collection
	 * 
	 * @param source
	 *            Source collection to be converted
	 * @param itemType
	 *            Type of items of actual collection
	 * @param itemConverter
	 *            Coverter to be used to convert items from source collection to
	 *            actual collection
	 */
	public IndirectArrayList(List<F> source, Class<E> itemType,
			IBeanConverter itemConverter) {
		super();
		detached = true;
		this.source = source;
		this.itemType = itemType;
		this.itemConverter = itemConverter;
	}

	/**
	 * Returns the element at the specified position in this list.
	 * ConverterAspect need to LazyArrayList override this method
	 * 
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public E get(int index) {
		return super.get(index);
	}

	public IBeanConverter getItemConverter() {
		return itemConverter;
	}

	public Class<?> getItemType() {
		return itemType;
	}

	@SuppressWarnings("unchecked")
	public List<AbstractBean> getSource() {
		return (List<AbstractBean>) source;
	}

	/**
	 * Returns if collection is a proxy that need be converted (detached true)
	 * or conversion has been performed and list has the actual items (detached
	 * false)
	 * 
	 * @return
	 */
	public boolean isDetached() {
		return detached;
	}

	/**
	 * Clean the elements used to helps lazy loading. This method is called when
	 * the conversion has been performed
	 * 
	 */
	public void undetach() {
		detached = false;
		source = null;
		itemType = null;
		itemConverter = null;
	}
}
