package com.clau.util;

import java.lang.reflect.Method;

public class MethodUtil {
  public static boolean isGetter(Method method) {
    return method.getName().startsWith("get")
            && method.getParameterCount() == 0
            && !void.class.equals(method.getReturnType());
  }

  public static String getFieldNameFromGetter(String getterName) {
    String withoutGet = getterName.substring(3);
    return Character.toLowerCase(withoutGet.charAt(0)) + withoutGet.substring(1);
  }
}
