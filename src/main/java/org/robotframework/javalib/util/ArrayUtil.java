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

package org.robotframework.javalib.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;

public class ArrayUtil {
    public static boolean arrayContains(String needle, String[] haystack) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i].equals(needle)) {
                return true;
            }
        }
        return false;
    }

    public static void assertArraysContainSame(String[] expected, String[] actual) {
        Assert.assertEquals(new HashSet(Arrays.asList(expected)), new HashSet(Arrays.asList(actual)));
    }

    public static <T> void assertArraysEquals(T[] expected, T[] actual) {
        Assert.assertTrue("Expected " + Arrays.asList(expected) + " but was " + Arrays.asList(actual), Arrays.equals(expected, actual));
    }

    public static <T,U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                         Math.min(original.length - from, newLength));
        return copy;
    }

    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        return copyOfRange(original, from, to, (Class<T[]>) original.getClass());
    }

    public static <T> T[] add(T[] original, T... newElements) {
        List<T> results = new ArrayList<T>(original.length + newElements.length);
        CollectionUtils.addAll(results, original);
        CollectionUtils.addAll(results, newElements);
        return results.toArray(original);
    }
}
