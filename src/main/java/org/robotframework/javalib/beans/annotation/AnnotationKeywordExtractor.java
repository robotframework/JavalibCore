/*
 * Copyright 2008 Nokia Siemens Networks Oyj
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.javalib.beans.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.reflection.IKeywordInvoker;
import org.robotframework.javalib.reflection.KeywordInvoker;

public class AnnotationKeywordExtractor implements IKeywordExtractor<DocumentedKeyword> {
	public Map<String, DocumentedKeyword> extractKeywords(final Object keywordBean,
			final Collection<Object> keywordBeanValues) {
		Map<String, DocumentedKeyword> extractedKeywords = new HashMap<String, DocumentedKeyword>();
		Class<?> clazz = keywordBean.getClass();
		Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (method.isAnnotationPresent(RobotKeyword.class)
					|| method.isAnnotationPresent(RobotKeywordOverload.class)) {
				createOrAddKeyword(extractedKeywords, keywordBean, method);
			}
		}
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (final Field field : fields) {
				if (field.isAnnotationPresent(Autowired.class)) {
					autowireKeywordBeanField(keywordBean, field, keywordBeanValues);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return extractedKeywords;
	}

	private void autowireKeywordBeanField(Object keywordBean, Field field, Collection<Object> keywordBeanValues) {
		try {
			Class<?> clazz = field.getDeclaringClass();
			if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(clazz.getModifiers()))
					&& !field.isAccessible()) {
				field.setAccessible(true);
			}
			for (Object keywordBeanValue : keywordBeanValues) {
				if (keywordBeanValue.getClass().equals(clazz)) {
					field.set(keywordBean, keywordBeanValue);
					return;
				}
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.format("Can't autowire field '%s' at keyword class '%s'.",
					field.getName(), keywordBean.getClass().getName()), e);
		}
		throw new IllegalArgumentException(String.format("Can't autowire field '%s' at keyword class '%s'.",
				field.getName(), keywordBean.getClass().getName()));
	}

	private void createOrAddKeyword(Map<String, DocumentedKeyword> extractedKeywords, Object keywordBean, Method method) {
		String name = method.getName();
		if (extractedKeywords.containsKey(name)) {
			extractedKeywords.put(name,
					addPolymorphToKeywordDefinition(extractedKeywords.get(name), keywordBean, method));
		} else {
			extractedKeywords.put(name, createKeyword(keywordBean, method));
		}
	}

	IKeywordInvoker createKeywordInvoker(Object keywordBean, Method method) {
		return new KeywordInvoker(keywordBean, method);
	}

	private DocumentedKeyword createKeyword(Object keywordBean, Method method) {
		IKeywordInvoker keywordInvoker = createKeywordInvoker(keywordBean, method);
		return createKeyword(keywordInvoker);
	}

	private DocumentedKeyword createKeyword(final IKeywordInvoker keywordInvoker) {
		return new DocumentedKeyword() {
			public Object execute(Object[] arguments) {
				return keywordInvoker.invoke(arguments);
			}

			public String[] getArgumentNames() {
				return keywordInvoker.getParameterNames();
			}

			public String getDocumentation() {
				return keywordInvoker.getDocumentation();
			}
		};
	}

	private DocumentedKeyword addPolymorphToKeywordDefinition(final DocumentedKeyword original,
			final Object keywordBean, final Method method) {
		final DocumentedKeyword other = createKeyword(keywordBean, method);
		final boolean isOverload = method.isAnnotationPresent(RobotKeywordOverload.class);
		if (isOverload && method.isAnnotationPresent(RobotKeyword.class))
			throw new AssertionError(
					"Method definition should not have both RobotKeyword and RobotKeywordOverload annotations");
		final int parameterTypesLength = method.getParameterTypes().length;
		return new DocumentedKeyword() {
			public Object execute(Object[] arguments) {
				if (parameterTypesLength == arguments.length) {
					return other.execute(arguments);
				}
				return original.execute(arguments);
			}

			public String[] getArgumentNames() {
				if (isOverload) {
					return original.getArgumentNames();
				}
				return other.getArgumentNames();
			}

			public String getDocumentation() {
				if (isOverload) {
					return original.getDocumentation();
				}
				return other.getDocumentation();
			}
		};
	}
}
