package com.clau.util;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class PackageUtil {

  public static File[] getClassFromPackage(String pathPackage) {
    String path = pathPackage.replace('.', '/');
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(path);

    if (resource == null) {
      throw new RuntimeException("Pacote não encontrado: " + pathPackage);
    }

    File directory = new File(resource.getFile());
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));

    return files;
  }

  public static void initializeClassesInPackage(String packageName) {
    File[] files = getClassFromPackage(packageName);

    if (files == null) return;

    for (File file : files) {
      String className = packageName + "." + file.getName().replace(".class", "");
      try {
        Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Erro ao inicializar classe " + className, e);
      }
    }
  }

  public static Package[] findAllPackages() {
    return Package.getPackages();
  }

  public static Package[] findPackagesInPackage(String packageName) {
    Package[] allPackages = findAllPackages();
    return Arrays.stream(allPackages)
            .filter(pkg -> pkg.getName().startsWith(packageName))
            .toArray(Package[]::new);
  }
}
