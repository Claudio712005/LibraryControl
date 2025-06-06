package com.clau.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

  public static List<Class> listarClassesPorAnotation(String packageName, Class annotation) {
    File[] files = PackageUtil.getClassFromPackage(packageName);
    List<Class> classes = new ArrayList<>();

    if (files == null) return classes;

    for (File file : files) {
      String className = packageName + "." + file.getName().replace(".class", "");
      try {
        Class<?> clazz = Class.forName(className);
        Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
          if (method.isAnnotationPresent(annotation)) {
            classes.add(clazz);
          }
        }

      } catch (Exception e) {
        throw new RuntimeException("Erro ao verificar classe " + className, e);
      }
    }

    return classes;
  }
}
