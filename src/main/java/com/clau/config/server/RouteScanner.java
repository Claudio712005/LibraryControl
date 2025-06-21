package com.clau.config.server;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.middleware.AuthMiddleware;
import com.clau.middleware.ExceptionMiddleware;
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

        GroupPrefix groupPrefix = clazz.getAnnotation(GroupPrefix.class);

        for (Method method : clazz.getDeclaredMethods()) {
          if (method.isAnnotationPresent(Route.class)) {
            Route route = method.getAnnotation(Route.class);
            HttpHandler handler = new MethodHttpHandler(controllerInstance, method);

            boolean isPrivate = method.isAnnotationPresent(SecurityRoute.class);

            if (isPrivate) {
              handler = new AuthMiddleware(handler, method.getAnnotation(SecurityRoute.class).roles());
            }

            handler = new ExceptionMiddleware(handler);

            String prefix = (groupPrefix != null) ? groupPrefix.value() : "";
            String completePath = prefix + route.path();
            String methodName = route.method().name();

            Router.getInstance().addRoute(completePath, methodName, handler);
            System.out.println("Rota registrada: " + Router.getInstance().getApiPrefix() + completePath + " -> " + method.getName() + " (" + (isPrivate ? "Privada" : "Pública") + ") - " + methodName );
          }
        }

      } catch (Exception e) {
        throw new RuntimeException("Erro ao registrar " + className, e);
      }
    }
  }

}
