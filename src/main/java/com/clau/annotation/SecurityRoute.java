package com.clau.annotation;

import com.clau.enums.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityRoute {
  Role[] roles() default {};
}
