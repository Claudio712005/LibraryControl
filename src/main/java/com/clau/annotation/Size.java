package com.clau.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
  int min() default 0;

  int max() default Integer.MAX_VALUE;

  String message() default "Tamanho inválido";
}
