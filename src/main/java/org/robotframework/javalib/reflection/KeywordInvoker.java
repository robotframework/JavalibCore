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

package org.robotframework.javalib.reflection;

import java.lang.reflect.Method;

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

    public String[] getParameterNames() {
        if (method.isAnnotationPresent(ArgumentNames.class)) {
            return method.getAnnotation(ArgumentNames.class).value();
        }
        return getParameterNamesFromParanamer();
    }

    public Object invoke(Object[] args) {
        try {
            Object[] groupedArguments = createArgumentGrouper().groupArguments(args);
            return method.invoke(obj, groupedArguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getDocumentation() {
        return method.getAnnotation(RobotKeyword.class).value();
    }

    IArgumentGrouper createArgumentGrouper() {
        return new ArgumentGrouper(method.getParameterTypes());
    }

    private String[] getParameterNamesFromParanamer() {
        try {
            return parameterNames.lookupParameterNames(method);
        } catch (ParameterNamesNotFoundException e) {
            return null;
        }
    }
}
