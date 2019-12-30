/*
 * Copyright 2013 Nokia Solutions and Networks Oyj
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

package org.robotframework.javalib.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;

public class KeywordInvoker implements IKeywordInvoker {

    private final Method method;
    private final Object obj;

    public KeywordInvoker(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
    }

    public List<String> getParameterNames() {
        if (method.isAnnotationPresent(ArgumentNames.class)) {
            // We use names stricter way than earlier, so making sure that varargs are marked correctly.
            // https://github.com/robotframework/JavalibCore/wiki/AnnotationLibrary#argument-names only recommends
            // marking varargs with *
            List argumentNames = Arrays.asList(method.getAnnotation(ArgumentNames.class).value());

            return argumentNames;
        }
        return getParameterNamesFromMethod();
    }

    public List<String> getParameterTypes() {
        List<String> parameterTypes = new ArrayList<String>();
        for (Class parameterClass : method.getParameterTypes()) {
            parameterTypes.add(parameterClass.getSimpleName());
        }
        return parameterTypes;
    }

    public Object invoke(List args, Map kwargs) {
        try {
            List reflectionArgs = createArgumentCollector().collectArguments(args, kwargs);
            Object[] reflectionArgsArray = reflectionArgs != null ? reflectionArgs.toArray() : null;
            return method.invoke(obj, reflectionArgsArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getDocumentation() {
        return method.getAnnotation(RobotKeyword.class).value();
    }

    IArgumentCollector createArgumentCollector() {
        return new ArgumentCollector(method.getParameterTypes(), getParameterNames());
    }

    private List<String> getParameterNamesFromMethod() {
            List<String> parameterNameList = this.getParameterNamesWithReflection();
            // Marking varargs and kwargs correctly for RF
            if (method.getParameterCount() > 0) {
                int lastParameterIndex = method.getParameterCount() - 1;
                Class lastParameterType = method.getParameters()[lastParameterIndex].getType();
                if (lastParameterType.equals(List.class)
                        || (lastParameterType.isArray() && lastParameterType != byte[].class)) {
                    parameterNameList.set(lastParameterIndex, "*" + parameterNameList.get(lastParameterIndex));
                } else if (method.getParameters()[lastParameterIndex].getType().equals(Map.class)) {
                    if (lastParameterIndex > 1
                            && (method.getParameters()[lastParameterIndex - 1].getType().equals(List.class)
                            || (method.getParameters()[lastParameterIndex - 1].getType().isArray() && method.getParameters()[lastParameterIndex - 1].getType() != byte[].class))) {
                        parameterNameList.set(lastParameterIndex - 1, "*" + parameterNameList.get(lastParameterIndex - 1));
                    }
                    parameterNameList.set(lastParameterIndex, "**" + parameterNameList.get(lastParameterIndex));
                }
            }
            return parameterNameList;
    }

    private List<String> getParameterNamesWithReflection() {
        List<String> parameterNameList = new ArrayList<String>();
        for (Parameter parameter : method.getParameters()) {
            parameterNameList.add(parameter.getName());
        }

        return parameterNameList;
    }
}
