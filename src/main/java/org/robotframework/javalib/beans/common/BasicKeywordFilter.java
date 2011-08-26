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

package org.robotframework.javalib.beans.common;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicKeywordFilter implements IClassFilter {
    private List conditions = new ArrayList();

    public BasicKeywordFilter() {
        conditions.add(new PublicClassCondition());
        conditions.add(new NotAbstractClassCondition());
        conditions.add(new HasDefaultConstructor());
    }

    public boolean accept(Class clazz) {
        Iterator iterator = conditions.iterator();
        while(iterator.hasNext()) {
            Condition condition = (Condition) iterator.next();
            if (!condition.check(clazz)) {
                return false;
            }
        }
        return true;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public interface Condition {
        public boolean check(Class clazz);
    }

    private class PublicClassCondition implements Condition {
        public boolean check(Class clazz) {
            return Modifier.isPublic(clazz.getModifiers());
        }
    }

    private class NotAbstractClassCondition implements Condition {
        public boolean check(Class clazz) {
            return !Modifier.isAbstract(clazz.getModifiers());
        }
    }

    private class HasDefaultConstructor implements Condition {
        public boolean check(Class clazz) {
            try {
                clazz.getConstructor(new Class[]{});
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }
}
