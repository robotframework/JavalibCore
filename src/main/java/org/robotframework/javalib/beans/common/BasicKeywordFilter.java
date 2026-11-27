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

package org.robotframework.javalib.beans.common;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A basic filter for classes that implements the IClassFilter interface.
 * This filter applies a set of conditions to determine whether a class should be accepted.
 */

public class BasicKeywordFilter implements IClassFilter {
    /**
     * The list of conditions that determine if a class is accepted.
     */
    private List<Condition> conditions = new ArrayList<>();

    /**
     * Constructs a new BasicKeywordFilter.
     * 
     * By default, adds conditions to check for public, non-abstract classes with a default constructor.
     */
    public BasicKeywordFilter() {
        conditions.add(new PublicClassCondition());
        conditions.add(new NotAbstractClassCondition());
        conditions.add(new HasDefaultConstructor());
    }

    /**
     * Checks if the given class should be accepted based on the conditions.
     * 
     * @param clazz the class to check
     * @return true if the class is accepted, false otherwise
     */
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

    /**
     * Adds a condition to the list of conditions.
     * 
     * @param condition the condition to add
     */
    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    /**
     * An interface defining a condition to check a class.
     */
    public interface Condition {
        /**
         * Checks if the given class meets the condition.
         * 
         * @param clazz the class to check
         * @return true if the class meets the condition, false otherwise
         */
        public boolean check(Class clazz);
    }

    /**
     * Checks if a class is public.
     */
    private class PublicClassCondition implements Condition {
        /**
         * Determines if the class is public.
         * 
         * @param clazz the class to check
         * @return true if the class is public
         */
        public boolean check(Class clazz) {
            return Modifier.isPublic(clazz.getModifiers());
        }
    }

    /**
     * Checks if a class is not abstract.
     */
    private class NotAbstractClassCondition implements Condition {
        /**
         * Determines if the class is not abstract.
         * 
         * @param clazz the class to check
         * @return true if the class is not abstract
         */
        public boolean check(Class clazz) {
            return !Modifier.isAbstract(clazz.getModifiers());
        }
    }

    /**
     * Checks if a class has a default constructor.
     */
    private class HasDefaultConstructor implements Condition {
        /**
         * Determines if the class has a default constructor.
         * 
         * @param clazz the class to check
         * @return true if the class has a default constructor
         */
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
