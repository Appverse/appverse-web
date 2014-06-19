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

import org.apache.commons.lang.ArrayUtils;
import org.appverse.web.framework.backend.api.common.AbstractBean;
import org.appverse.web.framework.backend.api.converters.IBeanConverter;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;

/**
 * Copy properties between two objects of subclasses of abstract beans. The
 * subclasses of abstract bean must have the same amount of properties and name
 * and types must match except the type of properties that have type that
 * subclass AbstractBean. For properties of type that subclass AbstractBean and
 * for properties that are collection parametrized with types that subclass
 * AbstractBean this class creates deferred objects that with the
 * ConverterAspect implement lazyloading.
 * 
 * @see org.appverse.web.framework.backend.api.converters.helpers.IndirectArrayList
 * @see org.appverse.web.framework.backend.aop.aspects.ConverterAspect
 * 
 */
public class ConverterUtils extends org.springframework.beans.BeanUtils {

	/**
	 * Creates target bean of targetClass type copying source properties to ones
	 * with the same name at target. Beans and collections are copied as
	 * detached objects to allow lazy initialization.
	 * 
	 * @param source
	 *            Source Bean to copy
	 * @param targetClass
	 *            Type of target bean to generate
	 * @param converters
	 *            List of conveters of child bean properties
	 * @return Target bean of targetClass type with source data converted
	 * @throws Exception
	 */
	public static <TargetBean extends AbstractBean> TargetBean convert(
			AbstractBean source, Class<TargetBean> targetClass,
			IBeanConverter[] converters) throws Exception {

		// If source bean is null return null target bean
		if (source == null) {
			return null;
		}

		// Create target bean using target class default constructor
		Constructor<TargetBean> constructor = targetClass
				.getConstructor(new Class[] {});
		TargetBean target = constructor.newInstance(new Object[] {});

		// Obtain source fields (including inherited fields)
		Field[] sourceFields = getBeanFields(source);

		// Call class methods to copy simple properties, bean properties and
		// collection properties
		ConverterUtils.copyPlainFields(source, target, sourceFields);
		ConverterUtils.convertBeanFields(source, target, converters,
				sourceFields);
		ConverterUtils.convertBeanCollectionFields(source, target, converters,
				sourceFields);

		return target;
	}

	/**
	 * Copy source bean collection properties of type bean to target bean as
	 * detached bean collections
	 * 
	 * @param sourceBean
	 *            Source Bean
	 * @param targetBean
	 *            Target Bean
	 * @param childrenBeanConverters
	 *            List of converters needed to convert source bean childs of
	 *            type bean collection to target bean childs of type bean
	 *            collection
	 * @param sourceFields
	 *            Fields of source Bean
	 * @throws Exception
	 */
	private static void convertBeanCollectionFields(AbstractBean sourceBean,
			AbstractBean targetBean, IBeanConverter[] childrenBeanConverters,
			Field[] sourceFields) throws Exception {

		// Arrays to hold bean collection field names, target collection types,
		// source collection parameter types and target collection parameter
		// types
		String[] beanCollectionFieldNames = new String[0];
		Type[] targetFieldTypes = new Type[0];
		Type[] sourceFieldParameterTypes = new Type[0];
		Type[] targetFieldParameterTypes = new Type[0];

		// For each source field...
		for (Field field : sourceFields) {
			// If source field is a collection...
			if (Collection.class.isAssignableFrom(field.getType())) {
				// Get Field (Remember is a collection) parameters
				Type[] sourceFieldParameters = ((ParameterizedType) field
						.getGenericType()).getActualTypeArguments();
				// If field is a collection of something extending
				// AbstractBean...
				if (sourceFieldParameters.length == 1
						&& AbstractBean.class
								.isAssignableFrom((Class<?>) sourceFieldParameters[0])) {
					// If target field is a collection
					if (Collection.class.isAssignableFrom(targetBean.getClass()
							.getDeclaredField(field.getName()).getType())) {
						// Get target field parameter types
						Type[] targetFieldParameters = ((ParameterizedType) targetBean
								.getClass().getDeclaredField(field.getName())
								.getGenericType()).getActualTypeArguments();
						// If target field is a collection of something
						// extending
						// AbstractBean...
						if (targetFieldParameters.length == 1
								&& AbstractBean.class
										.isAssignableFrom((Class<?>) targetFieldParameters[0])) {
							// Fill Arrays with name, source field type, target
							// type and target
							// parameter type
							beanCollectionFieldNames = (String[]) ArrayUtils
									.add(beanCollectionFieldNames,
											field.getName());
							targetFieldTypes = (Type[]) ArrayUtils.add(
									targetFieldTypes, targetBean.getClass()
											.getDeclaredField(field.getName())
											.getType());
							sourceFieldParameterTypes = (Type[]) ArrayUtils
									.add(sourceFieldParameterTypes,
											sourceFieldParameters[0]);
							targetFieldParameterTypes = (Type[]) ArrayUtils
									.add(targetFieldParameterTypes,
											targetFieldParameters[0]);

						} else {
							throw new Exception(
									"Target collection parameter type mismatch");
						}
					} else {
						throw new Exception("Target parameter type mismatch");
					}
				}
			}
		}

		// For each field with collection of bean type...
		for (int i = 0; i < beanCollectionFieldNames.length; i++) {
			String beanCollectionFieldName = beanCollectionFieldNames[i];
			Type targetFieldType = targetFieldTypes[i];
			Type sourceFieldParameterType = sourceFieldParameterTypes[i];
			Type targetFieldParameterType = targetFieldParameterTypes[i];

			// Select the appropiate converter...
			IBeanConverter converter = getConverter(childrenBeanConverters,
					sourceFieldParameterType, targetFieldParameterType);

			// Create a target detached (LazyArrayList) collection using the
			// source collection, the target parameter type y the converter...
			@SuppressWarnings("unchecked")
			IndirectArrayList<AbstractBean, AbstractBean> lazyCollection = new IndirectArrayList<AbstractBean, AbstractBean>(
					(List<AbstractBean>) getGetterMethod(
							beanCollectionFieldName, sourceBean).invoke(
							sourceBean),
					(Class<AbstractBean>) targetFieldParameterType, converter);
			// Set the detached collection on target bean
			getSetterMethod(beanCollectionFieldName, targetFieldType,
					targetBean).invoke(targetBean, lazyCollection);

		}
	}

	/**
	 * Copy source bean properties of type bean to target bean as detached beans
	 * 
	 * @param sourceBean
	 *            Source Bean
	 * @param targetBean
	 *            Target Bean
	 * @param childrenBeanConverters
	 *            List of converters needed to convert source bean children of
	 *            type bean to target bean children of type bean
	 * @param sourceFields
	 *            Fields of source Bean
	 * @throws Exception
	 */
	private static void convertBeanFields(AbstractBean sourceBean,
			AbstractBean targetBean, IBeanConverter[] childrenBeanConverters,
			Field[] sourceFields) throws Exception {

		// Arrays to hold bean field names, source types and target types
		String[] beanFieldNames = new String[0];
		Type[] sourceFieldTypes = new Type[0];
		Type[] targetFieldTypes = new Type[0];

		// For each source field...
		for (Field field : sourceFields) {
			// If source field is a bean...
			if (AbstractBean.class.isAssignableFrom(field.getType())) {
				// Fill Arrays with name, source field type and target type
				beanFieldNames = (String[]) ArrayUtils.add(beanFieldNames,
						field.getName());

				sourceFieldTypes = (Type[]) ArrayUtils.add(sourceFieldTypes,
						field.getType());

				targetFieldTypes = (Type[]) ArrayUtils.add(targetFieldTypes,
						targetBean.getClass().getDeclaredField(field.getName())
								.getType());
			}
		}

		// For each field with bean type...
		for (int i = 0; i < beanFieldNames.length; i++) {
			String beanFieldName = beanFieldNames[i];
			Type sourceFieldType = sourceFieldTypes[i];
			Type targetFieldType = targetFieldTypes[i];

			// Select the proper converter...
			IBeanConverter converter = getConverter(childrenBeanConverters,
					sourceFieldType, targetFieldType);

			// Get the target bean constructor
			@SuppressWarnings("unchecked")
			Constructor<AbstractBean> constructor = ((Class<AbstractBean>) targetFieldType)
					.getConstructor(new Class[] {});
			// Create target child bean
			AbstractBean targetChildBean = constructor
					.newInstance(new Object[] {});
			// Detach target child bean with source child bean and converter
			((Detachable) targetChildBean).detach(
					(AbstractBean) getGetterMethod(beanFieldName, sourceBean)
							.invoke(sourceBean), converter);
			// Set the detached target bean
			getSetterMethod(beanFieldName, targetFieldType, targetBean).invoke(
					targetBean, targetChildBean);

		}
	}

	/**
	 * Copy source bean single properties to target bean
	 * 
	 * @param source
	 *            Source Bean
	 * @param target
	 *            Target Bean
	 * @param sourceFields
	 *            Fields of source Bean
	 * @throws Exception
	 */
	private static void copyPlainFields(AbstractBean source,
			AbstractBean target, Field[] sourceFields) throws Exception {
		String[] ignoreProperties = new String[0];

		// For each source field...
		for (Field field : sourceFields) {
			// If source field is a bean...
			if (AbstractBean.class.isAssignableFrom(field.getType())) {
				// Put it in ignore properties
				ignoreProperties = (String[]) ArrayUtils.add(ignoreProperties,
						field.getName());
			}
		}

		// Copy properties with Spring Framework copy properties
		org.springframework.beans.BeanUtils.copyProperties(source, target,
				ignoreProperties);
	}

	/**
	 * Obtain a list of field declared by a bean class and superclasses
	 * 
	 * @param bean
	 *            Bean to get the fields
	 * @return Array of fields declared by bean class and superclasses
	 * @throws Exception
	 */
	private static Field[] getBeanFields(AbstractBean bean) throws Exception {
		// Fields added to AbstractBean by ConverterAspect
		String[] ignoreFields = ((Detachable) bean).getDetachableFields();
		Class<?> clazz = bean.getClass();
		// Get the all fields declared by the class
		Field[] fields = clazz.getDeclaredFields();
		// For all superclasses until arrive to AbstractBean (including
		// AbstractBean)...
		while (clazz.getSuperclass() != null
				&& AbstractBean.class.isAssignableFrom(clazz.getSuperclass())) {
			clazz = clazz.getSuperclass();
			// Add the clazz declared fields to field list
			fields = (Field[]) ArrayUtils.addAll(fields,
					clazz.getDeclaredFields());
		}
		// Remove from the list fields added by the converters aspect to
		// Abstract
		// bean
		for (Field field : fields) {
			if (ArrayUtils.contains(ignoreFields, field.getName())) {
				fields = (Field[]) ArrayUtils.removeElement(fields, field);
			}
		}
		return fields;
	}

	/**
	 * Select a converter from a array of them based on the match of converter
	 * parameters with sourceType and targetType
	 * 
	 * @param converters
	 *            List of converters
	 * @param sourceType
	 *            First type to match
	 * @param targetType
	 *            Second type to match
	 * @return Proper converter based on sourceType and targetType
	 * @throws Exception
	 */
	private static IBeanConverter getConverter(IBeanConverter[] converters,
			Type sourceType, Type targetType) throws Exception {

		// For each converter in converter list...
		for (IBeanConverter converter : converters) {
			// Obtain type parameters
			Type[] converterBeanTypes = ((ParameterizedType) converter
					.getClass().getGenericSuperclass())
					.getActualTypeArguments();

			// Check that converter has two parameters and this parameters are
			// differs
			if (converterBeanTypes.length != 2
					|| converterBeanTypes[0].equals(converterBeanTypes[1])) {
				throw new Exception("Wrong converter parameters");
			}

			boolean wrongConverter = false;
			// For each converter type parameter...
			for (Type converterBeanType : converterBeanTypes) {
				// Check that the converter parameter type is either sourceType
				// or targetType
				if (!((Class<?>) converterBeanType)
						.isAssignableFrom((Class<?>) sourceType)
						&& !((Class<?>) converterBeanType)
								.isAssignableFrom((Class<?>) targetType)) {
					wrongConverter = true;
					break;
				}
			}
			// If converter parameter types matches with sourceType and
			// targetType returns converter
			if (!wrongConverter) {
				return converter;
			}
		}
		// If no converter found throws exception
		throw new Exception("Appropiated Conversor not found");

	}

	/**
	 * Obtain a getter method object based on a bean and a field name.
	 * 
	 * @param beanFieldName
	 *            Field name that gets the getter method
	 * @param bean
	 *            Bean to obtain the getter method
	 * @return Getter Method
	 * @throws Exception
	 */
	private static Method getGetterMethod(String beanFieldName,
			AbstractBean bean) throws Exception {
		String beanGetterName = "get"
				+ beanFieldName.substring(0, 1).toUpperCase()
				+ beanFieldName.substring(1);
		Method beanGetter = bean.getClass().getDeclaredMethod(beanGetterName);
		return beanGetter;
	}

	/**
	 * Obtain a setter method object based on a bean, a field name and a field
	 * type.
	 * 
	 * @param beanFieldName
	 *            Field name that sets the setter method
	 * @param beanFieldType
	 *            Field type that sets the setter method
	 * @param bean
	 *            Bean to obtain the setter method
	 * @return Setter Method
	 * @throws Exception
	 */
	private static Method getSetterMethod(String beanFieldName,
			Type beanFieldType, AbstractBean bean) throws Exception {
		String beanSetterName = "set"
				+ beanFieldName.substring(0, 1).toUpperCase()
				+ beanFieldName.substring(1);
		Method beanSetter = bean.getClass().getDeclaredMethod(beanSetterName,
				(Class<?>) beanFieldType);
		return beanSetter;
	}
}
