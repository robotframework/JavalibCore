package org.robotframework.javalib.reflection;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ArgumentCollector implements IArgumentCollector {
    private final Class<?>[] parameterTypes;
    private final List<String> parameterNames;

    public ArgumentCollector(Class<?>[] parameterTypes, List<String> parameterNames) {
        this.parameterNames = parameterNames;
        this.parameterTypes = parameterTypes;
    }


    @Override
    public List collectArguments(List args, Map<String, Object> kwargs) {
        List collectedArguments = new ArrayList();
        Map<String, Object> cleanedKwargs = new HashMap<>();
        if (kwargs != null) {
            cleanedKwargs.putAll(kwargs);
        }
        boolean hasVarargs = this.keywordHasVarargs();
        boolean hasKwargs = this.keywordHasKwargs();
        if (parameterNames != null && parameterNames.size() > 0) {
            List filteredList = parameterNames.stream().filter(line -> !line.contains("*")).collect(Collectors.toList());
            for (int i = 0; i < filteredList.size(); i++) {
                collectedArguments.add(null);
            }
            List varargs = new ArrayList();

            for (int i = 0; i < parameterNames.size(); i++) {
                String parameterName = parameterNames.get(i).split("=")[0];
                boolean vararg = parameterName.contains("*");
                boolean kwarg = parameterName.contains("**");
                parameterName = parameterName.replace("*", "").replace("*", "");
                Object value = this.getParameterValue(parameterName, i, args, cleanedKwargs);
                Class<?> argumentType = parameterTypes.length > i && !vararg ? parameterTypes[i] : Object.class;
                if (!kwarg) {
                    if (vararg) {
                        if (value != null) {
                            varargs.add(convertToType(argumentType, value));
                        }
                    } else {
                        collectedArguments.set(i, convertToType(argumentType, value));
                    }
                }
            }
            if (hasVarargs && args != null && args.size() > filteredList.size()) {
                for (int i = filteredList.size()+1; i < args.size(); i++) {
                    varargs.add(args.get(i));
                }
            }
            if (hasVarargs) {
                collectedArguments.add(this.ensureCorrectVarargsType(varargs));
            }
        }

        if (hasKwargs) {
            collectedArguments.add(cleanedKwargs);
        }
        return collectedArguments;
    }

    private int getVarargsIndex() {
        int parameterSize = parameterNames != null ? parameterNames.size(): -1;
        if (parameterSize > 0 && parameterNames.get(parameterSize-1).startsWith("*") && !parameterNames.get(parameterSize-1).startsWith("**")) {
            return parameterSize-1;
        } else if (parameterSize > 1 && parameterNames.get(parameterSize-2).startsWith("*") && !parameterNames.get(parameterSize-2).startsWith("**")) {
            return parameterSize-2;
        } else {
            return -1;
        }
    }

    private Object getParameterValue(String parameterName, int i, List args, Map<String, Object> kwargs) {
        String parameterDefaultValue = this.parameterNames.get(i).contains("=") && this.parameterNames.get(i).split("=").length > 1 ? this.parameterNames.get(i).split("=")[1] : null;
        Object value = args != null && args.size() > i ? args.get(i) : parameterDefaultValue;
        if (kwargs != null && kwargs.containsKey(parameterName)) {
            value = kwargs.get(parameterName);
            kwargs.remove(parameterName);
        }
        return value;
    }

    private Object ensureCorrectVarargsType(List varargs) {
        int varargIndex = this.getVarargsIndex();
        if (parameterTypes != null && varargIndex > -1 && parameterTypes[varargIndex].isArray()) {
            Class<?> arrayClass = parameterTypes[varargIndex].getComponentType();
            Object[] varargsArray = (Object[]) Array.newInstance(arrayClass, varargs.size());
            for (int i = 0; i < varargs.size(); i++) {
                varargsArray[i] = varargs.get(i);
            }
            return varargsArray;
        } else {
            return varargs;
        }
    }

    private boolean keywordHasVarargs() {
        return this.getVarargsIndex() > -1;
    }

    private boolean keywordHasKwargs() {
        return parameterNames != null && parameterNames.size() > 0 &&
                (parameterNames.get(parameterNames.size()-1).startsWith("**"));
    }

    private Object convertToType(Class<?> clazz, Object object) {
        if (object != null) {
            if (clazz == Integer.class || clazz == Integer.TYPE) {
                return Integer.valueOf(object.toString());
            } else if (clazz == Long.class || clazz == Long.TYPE) {
                return Long.valueOf(object.toString());
            } else if (clazz == Short.class || clazz == Short.TYPE) {
                return Short.valueOf(object.toString());
            } else if (clazz == Byte.class || clazz == Byte.TYPE) {
                return Byte.valueOf(object.toString());
            } else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
                return Boolean.valueOf(object.toString());
            } else if (clazz == Float.class || clazz == Float.TYPE) {
                return Float.valueOf(object.toString());
            } else if (clazz == Double.class || clazz == Double.TYPE) {
                return Double.valueOf(object.toString());
            } else if (clazz == String.class) {
                return object.toString();
            } else if (object.getClass().isArray() && clazz.isAssignableFrom(List.class)) {
                //convert array to list. Needed at least with jrobotremotelibrary
                return Arrays.asList((Object[])object);
            } else if (List.class.isAssignableFrom(object.getClass()) && clazz.isArray()) {
                //convert list to array. Needed at least with jrobotremotelibrary
                return ((List)object).toArray();
            }
        }
        return object;
    }
}
