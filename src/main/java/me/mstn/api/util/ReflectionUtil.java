package me.mstn.api.util;

import me.mstn.api.nms.NMSPackage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (!DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) continue;
            return constructor;
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
    }

    public static Constructor<?> getConstructor(String className, NMSPackage NMSPackage, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return ReflectionUtil.getConstructor(NMSPackage.getClass(className), parameterTypes);
    }

    public static Object instantiateObject(Class<?> clazz, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return ReflectionUtil.getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    public static Object instantiateObject(String className, NMSPackage NMSPackage, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return ReflectionUtil.instantiateObject(NMSPackage.getClass(className), arguments);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Method method : clazz.getMethods()) {
            if (!method.getName().equals(methodName) || !DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes))
                continue;
            return method;
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Method getMethod(String className, NMSPackage NMSPackage, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return ReflectionUtil.getMethod(NMSPackage.getClass(className), methodName, parameterTypes);
    }

    public static Object invokeMethod(Object instance, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return ReflectionUtil.getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(Object instance, Class<?> clazz, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return ReflectionUtil.getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(Object instance, String className, NMSPackage NMSPackage, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return ReflectionUtil.invokeMethod(instance, NMSPackage.getClass(className), methodName, arguments);
    }

    public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field getField(String className, NMSPackage NMSPackage, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
        return ReflectionUtil.getField(NMSPackage.getClass(className), declared, fieldName);
    }

    public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return ReflectionUtil.getField(clazz, declared, fieldName).get(instance);
    }

    public static Object getValue(Object instance, String className, NMSPackage NMSPackage, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        return ReflectionUtil.getValue(instance, NMSPackage.getClass(className), declared, fieldName);
    }

    public static Object getValue(Object instance, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return ReflectionUtil.getValue(instance, instance.getClass(), declared, fieldName);
    }

    public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        ReflectionUtil.getField(clazz, declared, fieldName).set(instance, value);
    }

    public static void setValue(Object instance, String className, NMSPackage NMSPackage, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        ReflectionUtil.setValue(instance, NMSPackage.getClass(className), declared, fieldName, value);
    }

    public static void setValue(Object instance, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        ReflectionUtil.setValue(instance, instance.getClass(), declared, fieldName, value);
    }

    public enum DataType {
        BYTE(Byte.TYPE, Byte.class),
        SHORT(Short.TYPE, Short.class),
        INTEGER(Integer.TYPE, Integer.class),
        LONG(Long.TYPE, Long.class),
        CHARACTER(Character.TYPE, Character.class),
        FLOAT(Float.TYPE, Float.class),
        DOUBLE(Double.TYPE, Double.class),
        BOOLEAN(Boolean.TYPE, Boolean.class);

        private static final Map<Class<?>, DataType> CLASS_MAP;

        static {
            CLASS_MAP = new HashMap();
            for (DataType type : DataType.values()) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        private final Class<?> primitive;
        private final Class<?> reference;

        DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }

        public static DataType fromClass(Class<?> clazz) {
            return CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(Class<?> clazz) {
            DataType type = DataType.fromClass(clazz);
            return type == null ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(Class<?> clazz) {
            DataType type = DataType.fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = DataType.getPrimitive(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = DataType.getReference(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = DataType.getPrimitive(objects[index].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = DataType.getReference(objects[index].getClass());
            }
            return types;
        }

        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0; index < primary.length; ++index) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (primaryClass.equals(secondaryClass) || primaryClass.isAssignableFrom(secondaryClass)) continue;
                return false;
            }
            return true;
        }

        public Class<?> getPrimitive() {
            return this.primitive;
        }

        public Class<?> getReference() {
            return this.reference;
        }
    }
}