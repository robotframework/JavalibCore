package org.robotframework.javalib.reflection;

public class ArgumentConverter implements IArgumentConverter {

    private final Class<?>[] parameterTypes;

    public ArgumentConverter(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /** {@inheritDoc} */
    public Object[] convertArguments(Object[] args) {
        if (!shouldConvert(args)) {
            return args;
        }
        return internalConvert(args);
    }

    /**
     * @param args
     * @return
     */
    private Object[] internalConvert(Object[] args) {
        Object[] convertedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (!isArrayArgument(args[i])) {
                convertedArgs[i] = convertToType(parameterTypes[i], args[i]);
            } else {
                convertedArgs[i] = args[i];
            }
        }
        return convertedArgs;
    }

    /**
     * @param args
     * @return
     */
    private boolean shouldConvert(Object[] args) {
        return args != null;
    }

    private boolean isArrayArgument(Object object) {
        return object.getClass().isArray();
    }

    private Object convertToType(Class<?> clazz, Object object) {
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
        return null;
    }

}
