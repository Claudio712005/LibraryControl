package com.clau.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
  String message() default "O campo não pode estar em branco";
}
