package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/*
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        Class<?>[] classes = Collections.class.getDeclaredClasses();
        for (Class<?> clazz : classes){
            if (List.class.isAssignableFrom(clazz) && Modifier.isStatic(clazz.getModifiers()) && Modifier.isPrivate(clazz.getModifiers())){
                try {
                    Method method = clazz.getDeclaredMethod("get", int.class);
                    method.setAccessible(true);
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    method.invoke(constructor.newInstance(), 2);
                }
                catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {}
                catch (InvocationTargetException e){
                    if (e.getCause().toString().contains("IndexOutOfBoundsException")) return clazz;
                }
            }

        }
        return null;
    }

    public static boolean isImplementingList(Class<?> clazz) {
        ArrayList<Class<?>> classInterfaces = new ArrayList<>(Arrays.asList(clazz.getInterfaces()));
        ArrayList<Class<?>> parentInterfaces = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getInterfaces()));
        return classInterfaces.contains(List.class) || parentInterfaces.contains(List.class);
    }
}
/*

 */