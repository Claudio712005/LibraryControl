package com.clau.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
  String nameColumn() default "";
  String foreignKey() default "";
  boolean fetch() default true;
}
