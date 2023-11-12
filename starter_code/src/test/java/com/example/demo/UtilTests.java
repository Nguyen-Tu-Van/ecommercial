package com.example.demo;

import java.lang.reflect.Field;

public class UtilTests {

    public static void injectObjects(Object target, String name, Object toInject){ ;
        boolean issPrivate = false;
        try {
            Field fileName = target.getClass().getDeclaredField(name);
            if(!fileName.isAccessible())
            {
                fileName.setAccessible(true);
                issPrivate = true;
            }
            fileName.set(target, toInject);
            if(issPrivate) fileName.setAccessible(false);
        }
        catch (NoSuchFieldException e) {e.printStackTrace();}
        catch (IllegalAccessException e) {e.printStackTrace();}
    }
}
