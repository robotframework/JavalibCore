package org.robotframework.javalib.reflection;

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
                Class<?> argumentType = parameterTypes.length > i && !vararg ? parameterTypes[i] : String.class;
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
        if (parameterNames.size() > 0 && parameterNames.get(parameterNames.size()-1).startsWith("*") && !parameterNames.get(parameterNames.size()-1).startsWith("**")) {
            return parameterNames.size()-1;
        } else if (parameterNames.size() > 0 && parameterNames.get(parameterNames.size()-1).startsWith("*") && !parameterNames.get(parameterNames.size()-1).startsWith("**")) {
            return parameterNames.size()-2;
        } else {
            return -1;
        }
    }

    private Object getParameterValue(String parameterName, int i, List args, Map<String, Object> kwargs) {
        String parameterDefaultValue = parameterName.contains("=") && parameterName.split("=").length > 1 ? parameterName.split("=")[1] : null;
        Object value = args != null && args.size() > i ? args.get(i) : parameterDefaultValue;
        if (kwargs != null && kwargs.containsKey(parameterName)) {
            value = kwargs.get(parameterName);
            kwargs.remove(parameterName);
        }
        return value;
    }

    private Object ensureCorrectVarargsType(List varargs) {
        if (parameterTypes != null && parameterTypes.length > 0 && parameterTypes[parameterTypes.length-1].isArray() || (parameterTypes.length > 1 && parameterTypes[parameterTypes.length-2].isArray())) {
            return varargs.toArray(new String[0]);
        } else {
            return varargs;
        }
    }

    private boolean keywordHasVarargs() {
        return parameterTypes != null && parameterTypes.length > 0 &&
                (parameterTypes[parameterTypes.length-1] == List.class || parameterTypes[parameterTypes.length-1].isArray() ||
                        (parameterTypes.length > 1 && (parameterTypes[parameterTypes.length-2] == List.class || parameterTypes[parameterTypes.length-2].isArray())));
    }

    private boolean keywordHasKwargs() {
        return parameterTypes != null && parameterTypes.length > 0 &&
                (parameterTypes[parameterTypes.length-1] == Map.class);
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
            } else if (clazz.isAssignableFrom(object.getClass())) {
                return object;
            }
        }
        return null;
    }
}
