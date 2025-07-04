package com.clau.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {

  public static List<String> getClassNamesFromPackage(String packageName) throws IOException {
    String path = packageName.replace('.', '/');
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(path);

    if (resource == null) {
      throw new RuntimeException("Pacote não encontrado: " + packageName);
    }

    List<String> classNames = new ArrayList<>();

    if (resource.getProtocol().equals("file")) {
      File directory = new File(resource.getFile());
      File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
      if (files != null) {
        for (File file : files) {
          String className = file.getName().replace(".class", "");
          classNames.add(packageName + "." + className);
        }
      }
    } else if (resource.getProtocol().equals("jar")) {
      String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
      try (JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          String entryName = entry.getName();
          if (entryName.startsWith(path) && entryName.endsWith(".class") && !entry.isDirectory()) {
            String className = entryName.replace('/', '.').replace(".class", "");
            classNames.add(className);
          }
        }
      }
    } else {
      throw new RuntimeException("Protocolo não suportado: " + resource.getProtocol());
    }

    return classNames;
  }

  public static List<Class> listarClassesPacote(String pacote) throws IOException {
    List<String> classNames = getClassNamesFromPackage(pacote);
    List<Class> classes = new ArrayList<>();
    for (String className : classNames) {
      try {
        Class<?> clazz = Class.forName(className);
        classes.add(clazz);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Erro ao carregar classe " + className, e);
      }
    }
    return classes;
  }

}
