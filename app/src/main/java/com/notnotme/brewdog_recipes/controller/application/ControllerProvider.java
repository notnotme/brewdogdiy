package com.notnotme.brewdog_recipes.controller.application;

import android.app.Application;

import java.lang.reflect.Method;
import java.util.HashMap;

public final class ControllerProvider {

    private ControllerProvider() {
    }

    private static final HashMap<Class<? extends Controller>, Class<? extends Controller>> sControllers = new HashMap<>();

    public static void registerController(Application application, Class<? extends Controller> controllerType, Class<? extends Controller> clazz) throws Exception {
        if (sControllers.containsKey(controllerType)) {
            throw new IllegalArgumentException("This provider type is already registered");
        }

        if (! controllerType.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " not instance of " + controllerType.getSimpleName());
        }

        sControllers.put(controllerType, clazz);
        Method m = clazz.getMethod("applicationInit", Application.class);
        m.invoke(null, application);
    }

    public static <T extends Controller> T createController(Class<T> controllerType) throws Exception {
        if (! sControllers.containsKey(controllerType)) {
            throw new IllegalArgumentException("Controller of class " + controllerType.getSimpleName() + " not found");
        }

        return (T) sControllers.get(controllerType).newInstance();
    }

}
