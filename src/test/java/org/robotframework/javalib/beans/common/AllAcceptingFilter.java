package org.robotframework.javalib.beans.common;

import org.robotframework.javalib.beans.common.IClassFilter;

public class AllAcceptingFilter implements IClassFilter {
    public boolean accept(Class clazz) {
        return true;
    }
}
