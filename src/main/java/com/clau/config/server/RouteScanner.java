package com.clau.config.server;

import com.clau.annotation.Route;
import com.clau.util.PackageUtil;
import com.sun.net.httpserver.HttpHandler;
import com.clau.config.server.MethodHttpHandler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

public class RouteScanner {

  public static void scanPackage(String packageName) {

    File[] files = PackageUtil.getClassFromPackage(packageName);

    if (files == null) return;

    for (File file : files) {
      String className = packageName + "." + file.getName().replace(".class", "");
      try {
        Class<?> clazz = Class.forName(className);
        Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
          if (method.isAnnotationPresent(Route.class)) {
            Route route = method.getAnnotation(Route.class);
            Router.getInstance().addRoute(route.path(), new MethodHttpHandler(controllerInstance, method));
            System.out.println("Rota registrada: " + route.path() + " -> " + method.getName());
          }
        }

      } catch (Exception e) {
        throw new RuntimeException("Erro ao registrar " + className, e);
      }
    }
  }

}
