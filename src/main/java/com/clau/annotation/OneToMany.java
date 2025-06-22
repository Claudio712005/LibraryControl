package com.clau.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
  String mappedBy();
  String fkColumn();
  boolean fetch() default false;
}
