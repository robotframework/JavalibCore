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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;

import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;

public class KeywordInvoker implements IKeywordInvoker {
    protected Paranamer parameterNames = new CachingParanamer();

    private final Method method;
    private final Object obj;

    public KeywordInvoker(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
    }

    public List<String> getParameterNames() {
        if (method.isAnnotationPresent(ArgumentNames.class)) {
            return Arrays.asList(method.getAnnotation(ArgumentNames.class).value());
        }
        return getParameterNamesFromParanamer();
    }

    public Object invoke(List args, Map kwargs) {
        try {
            List groupedArguments = createArgumentGrouper().groupArguments(args);
            List convertedArguments = createArgumentConverter().convertArguments(groupedArguments);
            if (kwargs != null && kwargs.size() > 0) {
                convertedArguments.add(kwargs);
            }
            Object[] reflectionArgs = convertedArguments != null ? convertedArguments.toArray() : null; 
            return method.invoke(obj, reflectionArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getDocumentation() {
        return method.getAnnotation(RobotKeyword.class).value();
    }

    IArgumentConverter createArgumentConverter() {
        return new ArgumentConverter(method.getParameterTypes());
    }
    
    IArgumentGrouper createArgumentGrouper() {
        return new ArgumentGrouper(method.getParameterTypes());
    }

    private List<String> getParameterNamesFromParanamer() {
        try {
            return Arrays.asList(parameterNames.lookupParameterNames(method));
        } catch (ParameterNamesNotFoundException e) {
            return null;
        }
    }
}
