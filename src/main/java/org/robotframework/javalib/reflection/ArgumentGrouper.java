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

import java.util.ArrayList;
import java.util.List;

import org.robotframework.javalib.util.ArrayUtil;

public class ArgumentGrouper implements IArgumentGrouper {
    private final Class<?>[] parameterTypes;

    public ArgumentGrouper(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List groupArguments(List ungroupedArguments) {
        if (shouldGroupArguments(ungroupedArguments)) {
            return extractArguments(asStrings(ungroupedArguments));
        } else {
            return ungroupedArguments;
        }
    }

    private boolean shouldGroupArguments(List ungroupedArguments) {
        return !shouldNotGroupArguments(ungroupedArguments);
    }
    
    private boolean shouldNotGroupArguments(List ungroupedArguments) {
        return ungroupedArguments == null || parameterTypes.length == 0 ||
            parameterTypes.length == ungroupedArguments.size() && !lastArgIsAnArray();
    }
    
    private List asStrings(List ungroupedArguments) {
        List argsAsString = new ArrayList<String>();
        for (int i = 0; i < ungroupedArguments.size(); i++) {
            argsAsString.add(ungroupedArguments.get(i).toString());
        }
        return argsAsString;
    }
    
    private List extractArguments(List list) {
        List extractedArguments = list.subList(0, parameterTypes.length - 1);
        extractedArguments.add(list.subList(parameterTypes.length, list.size()-1));
        return extractedArguments;
    }

    private boolean lastArgIsAnArray() {
        return parameterTypes[parameterTypes.length - 1].isArray();
    }
}
