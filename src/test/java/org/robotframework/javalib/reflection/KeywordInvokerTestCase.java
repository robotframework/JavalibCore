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

import org.jmock.MockObjectTestCase;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;

public abstract class KeywordInvokerTestCase extends MockObjectTestCase {
    protected Object[] restOfArgs;

    protected Method getMethod(String methodName) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName()))
                return method;
        }
        return null;
    }

    public void keywordWithVariableArgCount(String[] varArg) {}

    @RobotKeyword("documentation")
    public String someMethod(String arg, String[] groupedArguments) {
        restOfArgs = groupedArguments;
        return arg;
    }

    @ArgumentNames({"firstArg", "*args"})
    @RobotKeyword("documentation")
    public void keywordWithArgumentAnnotation(String arg, String[] restOf) {
    }
}
